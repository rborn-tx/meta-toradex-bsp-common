FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.77"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "664411bde9c033778f85f9ae3a74351406642f6a"
SRCREV_machine_use-head-next = "664411bde9c033778f85f9ae3a74351406642f6a"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
