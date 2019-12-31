FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.5"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "a592a95575cdfa3f04fbfd7c10cd7baef5e0125e"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
