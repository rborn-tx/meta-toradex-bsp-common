FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.34"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "1830688f426a0146c1f09ac9623d37bda6d308d4"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
