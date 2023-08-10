# SYSFW/TIFS Firmware
DEPENDS:append:k3 = " python3-jsonschema-native python3-pyelftools-native python3-pyyaml-native optee-os ti-sci-fw ti-dm-fw trusted-firmware-a"
DEPENDS:append:k3r5 = " python3-jsonschema-native python3-pyelftools-native python3-pyyaml-native ti-sci-fw ti-dm-fw"
BL31 = "${STAGING_DIR_HOST}/firmware/bl31.bin"
TEE = "${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware/bl32.bin"
BINMAN_INDIRS = "${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware"
EXTRA_OEMAKE:append:k3 = " BL31=${BL31} TEE=${TEE} BINMAN_INDIRS=${BINMAN_INDIRS}"
EXTRA_OEMAKE:append:k3r5 = " BINMAN_INDIRS=${BINMAN_INDIRS}"

do_deploy:append:k3r5 () {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            if [ x${config} = "xverdin-am62_r5_usbdfu_defconfig" ]
            then
                TARGETSUFFIX="-dfu"
            else
                TARGETSUFFIX=""
            fi
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    for f in ${B}/${config}/tiboot3-*.bin; do
                        if [ -f "$f" ]; then
                            TARGET=$(basename $f)${TARGETSUFFIX}
                            install -m 644 $f ${DEPLOYDIR}/${TARGET}
                        fi
                    done

                    for f in ${B}/${config}/sysfw*.itb; do
                            if [ -f "$f" ]; then
                                    install -m 644 $f ${DEPLOYDIR}/
                            fi
                    done
                fi
            done
            unset j
        done
        unset i
    fi
}
