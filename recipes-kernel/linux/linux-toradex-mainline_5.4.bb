FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.28"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "75b6d773f039ae9cc1f285c7f495292fc35ef9a7"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
