FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.154"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "05a40bd77ab83451bd4d96e1e1d09850f11cfd06"
SRCREV_machine_use-head-next = "${AUTOREV}"

KCONFIG_MODE="--alldefconfig"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

# Don't delete /older/ in the link as /older/ also contains the newest file and we have a stable
# link
SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.154-rt65.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "1626836fc6bbff42e463c6a07bfc79b7"
SRC_URI[rt-patch.sha256sum] = "899037547ab272cbb835400633e9c96812c1172845b68479ececc3901eaf6c6d"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG_apalis-tk1 ?= "tegra_defconfig"

export DTC_FLAGS = "-@"
