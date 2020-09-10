FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.61"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "fc5f79d304d7323762a2378f787b39e2256ad546"
SRCREV_machine_use-head-next = "${AUTOREV}"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI += " \
    file://defconfig \
"

SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.61-rt37.patch.xz;name=rt-patch \
"
SRC_URI[rt-patch.md5sum] = "5a72e4f56ffdd79c8c668197f989f8d1"
SRC_URI[rt-patch.sha256sum] = "b2b52be0ef8b56a44a898ffc6a54515508e3cc9b2faece7a7d9f5d617a29ede1"
