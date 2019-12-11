FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.2"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "0a15b6b8f63335a6ca666e355daaafc186354872"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
