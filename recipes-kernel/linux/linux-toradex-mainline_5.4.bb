FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.6"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "e87dbbae257621aaca247d74bd95c2fd94e68994"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
