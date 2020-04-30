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

SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.34-rt21.patch.xz;name=rt-patch \
"
SRC_URI[rt-patch.md5sum] = "0b11e1509cb4095b52a27fc0dc4087f8"
SRC_URI[rt-patch.sha256sum] = "e4e73861fb95e326f3c6aafa9746906fa33d9ee5eeb8ad538745a4ea4dd1f0dd"
