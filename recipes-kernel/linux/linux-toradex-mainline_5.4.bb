FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.43"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "544ddfe58bb022d0c3af4a993a567aaa3cf80f64"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"

SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.43-rt25.patch.xz;name=rt-patch \
"
SRC_URI[rt-patch.md5sum] = "0b956e922027c94d38f2281ab3c497a7"
SRC_URI[rt-patch.sha256sum] = "62a4064891de8920755be5a4ace627f2a314ba9154aa7df8ac5c6e57de023e42"
