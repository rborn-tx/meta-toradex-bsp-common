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

# Whether or not the console baud-rate is missing from the "console" U-Boot
# variable. The following values are allowed:
#
# - "AUTO": determined at runtime by the boot script.
# - "0": baud-rate is assumed to be present.
# - "1": baud-rate is assumed to be missing.
#
DISTRO_BOOT_BAUDRATE_MISSING ??= "AUTO"

inherit deploy

do_compile() {
    cp "${WORKDIR}/boot.cmd.in" boot.cmd

    sed -e 's/@@KERNEL_BOOTCMD@@/${KERNEL_BOOTCMD}/' \
        -e 's/@@KERNEL_IMAGETYPE@@/${KERNEL_IMAGETYPE}/' \
        -e 's/@@KERNEL_DTB_PREFIX@@/${DTB_PREFIX}/' \
        -e 's/@@APPEND@@/${APPEND}/' \
        -e 's/@@FITCONF_FDT_OVERLAYS@@/${FITCONF_FDT_OVERLAYS}/' \
        -i boot.cmd

    if [ "${DISTRO_BOOT_BAUDRATE_MISSING}" = "AUTO" ]; then
        # Keep the BAUDRATE_MISSING=AUTO block and remove the BAUDRATE_MISSING=FIXED one:
        sed -e '/#+START_BLOCK(BAUDRATE_MISSING=AUTO)/,/#+END_BLOCK(BAUDRATE_MISSING=AUTO)/{/^#+/d}' \
            -e '/#+START_BLOCK(BAUDRATE_MISSING=FIXED)/,/#+END_BLOCK(BAUDRATE_MISSING=FIXED)/d' \
            -i boot.cmd
    elif [ "${DISTRO_BOOT_BAUDRATE_MISSING}" = "1" ] || \
         [ "${DISTRO_BOOT_BAUDRATE_MISSING}" = "0" ]; then
        # Remove the BAUDRATE_MISSING=AUTO block and keep the BAUDRATE_MISSING=FIXED one:
        sed -e '/#+START_BLOCK(BAUDRATE_MISSING=AUTO)/,/#+END_BLOCK(BAUDRATE_MISSING=AUTO)/d' \
            -e '/#+START_BLOCK(BAUDRATE_MISSING=FIXED)/,/#+END_BLOCK(BAUDRATE_MISSING=FIXED)/{/^#+/d}' \
            -e 's/@@BAUDRATE_MISSING@@/${DISTRO_BOOT_BAUDRATE_MISSING}/' \
            -i boot.cmd
    else
        bberror "DISTRO_BOOT_BAUDRATE_MISSING is not properly set"
    fi
}

do_deploy() {
    mkimage -T script -C none -n "Distro boot script" -d boot.cmd boot.scr
    install -m 0644 boot.scr ${DEPLOYDIR}/boot.scr-${MACHINE}
}

addtask deploy after do_install before do_build

PROVIDES += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
