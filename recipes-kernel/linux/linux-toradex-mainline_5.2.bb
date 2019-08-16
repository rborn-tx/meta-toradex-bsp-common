FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

LINUX_VERSION ?= "5.2.7"

SRCREV_machine = "74e785ce1024471e77b6fb9da83ffd60762c8cac"

KBRANCH = "toradex_5.2.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
