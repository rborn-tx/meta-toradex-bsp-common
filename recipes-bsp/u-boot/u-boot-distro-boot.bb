DESCRIPTION = "Boot script for launching images with U-Boot distro boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INHIBIT_DEFAULT_DEPS = "1"
DEPENDS = "u-boot-mkimage-native"

SRC_URI = " \
    file://boot.cmd.in \
"

APPEND ?= ""

KERNEL_BOOTCMD ??= "bootz"
KERNEL_BOOTCMD:aarch64 ?= "booti"

DTB_PREFIX ??= "${@d.getVar('KERNEL_DTB_PREFIX').replace("/", "_") if d.getVar('KERNEL_DTB_PREFIX') else ''}"

FITCONF_FDT_OVERLAYS ??= ""

inherit deploy

do_compile() {
    sed -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
        -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@KERNEL_DTB_PREFIX@@/${DTB_PREFIX}/' \
        -e 's/@@APPEND@@/${APPEND}/' \
        -e 's/@@FITCONF_FDT_OVERLAYS@@/${FITCONF_FDT_OVERLAYS}/' \
        "${WORKDIR}/boot.cmd.in" > boot.cmd
}

do_deploy() {
    mkimage -T script -C none -n "Distro boot script" -d boot.cmd boot.scr
    install -m 0644 boot.scr ${DEPLOYDIR}/boot.scr-${MACHINE}
}

addtask deploy after do_install before do_build

PROVIDES += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
