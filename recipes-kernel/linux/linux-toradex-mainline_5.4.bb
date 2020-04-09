FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.28"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "3ee0633e86618d664fa7a13cb372c9999cfd6133"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
