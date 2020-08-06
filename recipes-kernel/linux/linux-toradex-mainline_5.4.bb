FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.54"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "c0969469ee07b570b054df2d47a787f2732a362a"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"

SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.54-rt32.patch.xz;name=rt-patch \
"
SRC_URI[rt-patch.md5sum] = "f83f14b18f66873f6a3a26925a62d0c4"
SRC_URI[rt-patch.sha256sum] = "3385c8a64233f7f34c509626aaa6b5084bf75b1b27a0dc0dd5024d9736270b03"
