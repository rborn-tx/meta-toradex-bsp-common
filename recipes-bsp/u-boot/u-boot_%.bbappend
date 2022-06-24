FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRCREV:use-head-next = "${AUTOREV}"
# See commit fba0882bcd ("Add valgrind headers to U-Boot") (after 2022.04)
LIC_FILES_CHKSUM:use-head-next = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
SRC_URI:append:use-nxp-bsp:colibri-imx7 = " \
    file://0001-colibri_imx7-boot-linux-kernel-in-secure-mode.patch \
"

PADDING_DIR = "${B}"
nand_padding () {
    # pad the end of U-Boot with 0x00 up to the the end of the CSF area
    #PAD_END=$(echo -n "0x"; od -X  -j 0x24 -N 4 u-boot.imx | sed -e '/................/!d' -e 's/........\(.*\)/\1/')
    #PAD_END=$(( $PAD_END - 0x400 ))
    #objcopy -I binary -O binary --pad-to $PAD_END u-boot.imx u-boot.imx.zero-padded
    # assume that the above never need more than 10k of padding and skip the
    # shell magic to get a correct size.
    dd bs=10k count=1 if=/dev/zero | cat ${PADDING_DIR}/u-boot.imx - > ${PADDING_DIR}/u-boot.imx.zero-padded

    # U-Boot is flashed 1k into a NAND block, create a binary which prepends
    # U-boot with 1k of zeros to ease flashing
    dd bs=1024 count=1 if=/dev/zero | cat - ${PADDING_DIR}/u-boot.imx.zero-padded > ${PADDING_DIR}/u-boot-nand.imx
}

deploy_uboot_with_spl () {
    for config in ${UBOOT_MACHINE}; do
        if [ -f "${B}/${config}/u-boot-with-spl.imx" ]; then
            install -D -m 644 ${B}/${config}/u-boot-with-spl.imx ${DEPLOYDIR}/u-boot-with-spl.imx
        fi
    done
}

# build imx-boot from within U-Boot
inherit ${@oe.utils.ifelse(d.getVar('UBOOT_PROVIDES_BOOT_CONTAINER') == '1', 'imx-boot-container', '')}
DEPENDS:imx-boot-container += "bc-native bison-native dtc-native lzop-native python3-setuptools-native swig-native"

do_compile:append:colibri-imx6ull () {
    nand_padding
}

do_compile:append:colibri-imx7 () {
    nand_padding
}

do_compile:append:colibri-vf () {
    nand_padding
}

do_deploy:append:colibri-imx6 () {
    deploy_uboot_with_spl
}

do_deploy:append:apalis-imx6 () {
    deploy_uboot_with_spl
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

do_deploy:append:imx-boot-container() {
    # Deploy imx-boot
    if [ -n "${UBOOT_CONFIG}" ]
    then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    install -d ${DEPLOYDIR}
                    install -m 0644 ${B}/${config}/flash.bin ${DEPLOYDIR}/imx-boot-${MACHINE}-${type}
                    ln -sf imx-boot-${MACHINE}-${type} ${DEPLOYDIR}/imx-boot
                fi
            done
            unset  j
        done
        unset  i
    fi
}
