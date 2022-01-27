FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.161"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "67390146eab37625023f4b2fad77cee623bfcb64"
SRCREV_machine_use-head-next = "${AUTOREV}"

KCONFIG_MODE="--alldefconfig"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

# Don't delete /older/ in the link as /older/ also contains the newest file and we have a stable
# link
SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.161-rt67.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "d57203d7b93e0ca06405d85698786495"
SRC_URI[rt-patch.sha256sum] = "7dcd9d976647e57072ab7266a268aefd93945af6519459b8d5ec130b6a88c3f6"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG_apalis-tk1 ?= "tegra_defconfig"

export DTC_FLAGS = "-@"
