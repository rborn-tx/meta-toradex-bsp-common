PADDING_DIR = "${B}"
nand_padding () {
    # pad the end of U-Boot with 0x00 up to the end of the CSF area
    #PAD_END=$(echo -n "0x"; od -X  -j 0x24 -N 4 u-boot.imx | sed -e '/................/!d' -e 's/........\(.*\)/\1/')
    #PAD_END=$(( $PAD_END - 0x400 ))
    #objcopy -I binary -O binary --pad-to $PAD_END u-boot.imx u-boot.imx.zero-padded
    # assume that the above never needs more than 10k of padding and skip the
    # shell magic to get a correct size.
    dd bs=10k count=1 if=/dev/zero | cat ${PADDING_DIR}/u-boot.imx - > ${PADDING_DIR}/u-boot.imx.zero-padded

    # U-Boot is flashed 1k into a NAND block, create a binary which prepends
    # U-boot with 1k of zeros to ease flashing
    dd bs=1024 count=1 if=/dev/zero | cat - ${PADDING_DIR}/u-boot.imx.zero-padded > ${PADDING_DIR}/u-boot-nand.imx
}

# build imx-boot from within U-Boot
inherit ${@oe.utils.ifelse(d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '1', 'imx-boot-container', '')}

do_compile:append:colibri-imx6ull () {
    nand_padding
}

do_compile:append:colibri-imx7 () {
    nand_padding
}

BOOT_TOOLS = "imx-boot-tools"
do_deploy:append:mx8m-generic-bsp() {
    # Deploy u-boot-nodtb.bin and fsl-imx8m*-XX.dtb for mkimage to generate boot binary
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/arch/arm/dts/${UBOOT_DTB_NAME}  ${DEPLOYDIR}/${BOOT_TOOLS}
                    install -m 0777 ${B}/${config}/u-boot-nodtb.bin  ${DEPLOYDIR}/${BOOT_TOOLS}/u-boot-nodtb.bin-${MACHINE}-${type}
                fi
            done
            unset  j
        done
        unset  i
    fi
}
