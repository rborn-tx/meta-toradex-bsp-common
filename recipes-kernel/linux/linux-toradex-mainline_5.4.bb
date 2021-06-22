FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

LINUX_VERSION ?= "5.4.77"
PV = "${LINUX_VERSION}+git${SRCPV}"

SRCREV_machine = "a2e5dc80229e114cee12a54c4717f9b2a31546c6"
SRCREV_machine_use-head-next = "${AUTOREV}"

KCONFIG_MODE="--alldefconfig"

KBRANCH = "toradex_5.4.y"

require recipes-kernel/linux/linux-toradex-mainline.inc

SRC_URI_append_preempt-rt = " \
    ${KERNELORG_MIRROR}/linux/kernel/projects/rt/5.4/older/patch-5.4.77-rt43.patch.xz;name=rt-patch \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"
SRC_URI[rt-patch.md5sum] = "cf96e01e04ec8743e4b24caec76d2c2d"
SRC_URI[rt-patch.sha256sum] = "a0966fb60ce26f28e4dea5eb9db2e62974e1391470ea1bdb88e2d884a3280dc4"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

KBUILD_DEFCONFIG ?= "toradex-imx_v6_v7_defconfig"
KBUILD_DEFCONFIG_apalis-tk1 ?= "tegra_defconfig"
