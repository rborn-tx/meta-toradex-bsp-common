FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.129"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "55192fb3c2355a8251ab3adb8043c57f0ad010fa"
SRCREV_machine_use-head-next = "${AUTOREV}"

KCONFIG_MODE="--alldefconfig"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI:append:preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/patch-5.4.129-rt61.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "a6c668577fdde9cca5060bd72a782d16"
SRC_URI[rt-patch.sha256sum] = "f8ddc34c7765bb78c9f44c4e41dae7d4196c87201212ab4ec3723a7594b9702c"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG:apalis-tk1 ?= "tegra_defconfig"

export DTC_FLAGS = "-@"
