SUMMARY = "Toradex mainline Linux kernel"
SUMMARY:preempt-rt = "Toradex mainline real-Time Linux kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.4:"

DEPENDS += "coreutils-native lzop-native"

LINUX_VERSION ?= "5.4.154"
PV = "${LINUX_VERSION}+git${SRCPV}"

KBRANCH = "toradex_5.4.y"
SRC_URI = " \
    git://git.toradex.com/linux-toradex.git;protocol=https;branch=${KBRANCH};name=machine \
"

SRCREV_machine = "dd7439734dc9ea17674a9c36f0bfa725e7d10736"
SRCREV_machine:use-head-next = "${AUTOREV}"

S = "${WORKDIR}/git"

KCONFIG_MODE="--alldefconfig"
KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG:apalis-tk1 ?= "tegra_defconfig"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

inherit kernel-yocto kernel toradex-kernel-localversion

# Additional file deployed by recent mainline kernels
FILES:${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo"

KERNEL_CONFIG_NAME ?= "${KERNEL_PACKAGE_NAME}-config-${KERNEL_ARTIFACT_NAME}"
KERNEL_CONFIG_LINK_NAME ?= "${KERNEL_PACKAGE_NAME}-config"

export DTC_FLAGS = "-@"

do_deploy:append() {
    cp -a ${B}/.config ${DEPLOYDIR}/${KERNEL_CONFIG_NAME}
    ln -sf ${KERNEL_CONFIG_NAME} ${DEPLOYDIR}/${KERNEL_CONFIG_LINK_NAME}
}

#######################################################################

# Don't delete /older/ in the link as /older/ also contains the newest
# file and we have a stable link
SRC_URI:append:preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.154-rt65.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "1626836fc6bbff42e463c6a07bfc79b7"
SRC_URI[rt-patch.sha256sum] = "899037547ab272cbb835400633e9c96812c1172845b68479ececc3901eaf6c6d"

#######################################################################
