FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.193"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "f5d73fd6e9f806fea129d0f26ca7c555d408ba09"
SRCREV_machine_use-head-next = "${AUTOREV}"

KCONFIG_MODE="--alldefconfig"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

# Don't delete /older/ in the link as /older/ also contains the newest file and we have a stable
# link
SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.193-rt74.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "abbe334a2ba123ad5716d5d68d9fb15d"
SRC_URI[rt-patch.sha256sum] = "821d7bf3015d90e86eace5869d5596eacc9e4b5bd80644d40207817c4b8cc4be"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG_apalis-tk1 ?= "tegra_defconfig"

export DTC_FLAGS = "-@"
