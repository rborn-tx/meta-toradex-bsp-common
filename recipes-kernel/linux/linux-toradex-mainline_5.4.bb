FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.18"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "4d9a701d8bda68d6dabcb3e05e52fbf4d3ea54bc"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
