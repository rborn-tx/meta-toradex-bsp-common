FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "a4d179b488dc0c5ebb054bebe68011940d60629a"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
