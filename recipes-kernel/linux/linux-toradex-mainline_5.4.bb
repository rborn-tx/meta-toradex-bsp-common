FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.28"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "9ef9ba76540e1778510af18b07582d0a32c50ebf"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
