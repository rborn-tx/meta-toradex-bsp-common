FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.2:"

LINUX_VERSION ?= "5.2.7"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "74e785ce1024471e77b6fb9da83ffd60762c8cac"

KBRANCH = "toradex_5.2.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
