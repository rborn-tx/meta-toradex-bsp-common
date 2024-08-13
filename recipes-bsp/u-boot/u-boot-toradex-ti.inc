# SYSFW/TIFS Firmware
DEPENDS:append:k3 = " python3-jsonschema-native python3-pyelftools-native python3-pyyaml-native ti-sci-fw"
DEPENDS:append:k3r5 = " python3-jsonschema-native python3-pyelftools-native python3-pyyaml-native ti-sci-fw"

DM_FIRMWARE = "ipc_echo_testb_mcu1_0_release_strip.xer5f"

PLAT_SFX = ""
PLAT_SFX:j721e = "j721e"
PLAT_SFX:j7200 = "j7200"
PLAT_SFX:j721s2 = "j721s2"
PLAT_SFX:j784s4 = "j784s4"
PLAT_SFX:am65xx = "am65xx"
PLAT_SFX:am64xx = "am64xx"
PLAT_SFX:am62xx = "am62xx"
PLAT_SFX:am62axx = "am62axx"

PACKAGECONFIG[atf] = "BL31=${STAGING_DIR_HOST}/firmware/bl31.bin,,trusted-firmware-a"
PACKAGECONFIG[optee] = "TEE=${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware/bl32.bin,,optee-os"
PACKAGECONFIG[dm] = "DM=${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware/ti-dm/${PLAT_SFX}/${DM_FIRMWARE},,ti-dm-fw"

PACKAGECONFIG:append:aarch64 = " atf optee"
PACKAGECONFIG:append:j721e = " dm"
PACKAGECONFIG:append:j7200 = " dm"
PACKAGECONFIG:append:j721s2 = " dm"
PACKAGECONFIG:append:j784s4 = " dm"
PACKAGECONFIG:append:am62xx = " dm"
PACKAGECONFIG:append:am62axx = " dm"

BINMAN_INDIRS = "${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware"

EXTRA_OEMAKE:append = " ${PACKAGECONFIG_CONFARGS} BINMAN_INDIRS=${BINMAN_INDIRS}"

do_compile[mcdepends] += "mc::k3r5:virtual/bootloader:do_deploy"

do_compile:prepend:k3 () {
    cp ${DEPLOY_DIR_IMAGE}/tiboot3-*.bin ${STAGING_DIR_HOST}${nonarch_base_libdir}/firmware/
}

do_deploy:append:k3 () {
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            for f in ${B}/${config}/firmware-*.bin; do
                if [ -f "$f" ]; then
                    install -m 644 $f ${DEPLOYDIR}/
                fi
            done
        done
    fi
}

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
