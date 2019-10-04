FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

LINUX_VERSION ?= "5.3.2"

SRCREV_machine = "1ec6cce5d96a6faad11f0f6c4c5bd36c69c7a3d7"

KBRANCH = "toradex_5.3.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"
