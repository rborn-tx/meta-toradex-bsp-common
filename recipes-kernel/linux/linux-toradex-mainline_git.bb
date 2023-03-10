SUMMARY = "Toradex mainline Linux kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-toradex-mainline-git:"

DEPENDS += "coreutils-native"

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "libyaml-native"

PACKAGECONFIG ??= ""
PACKAGECONFIG[dt-validation] = ",,python3-dtschema-native"
# we need the wrappers if validation isn't in the packageconfig
DEPENDS += "${@bb.utils.contains('PACKAGECONFIG', 'dt-validation', '', 'python3-dtschema-wrapper-native', d)}"

PV = "${LINUX_VERSION}+git${SRCPV}"

LINUX_REPO = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git"
SRC_URI = " \
    ${LINUX_REPO};protocol=https;branch=${KBRANCH};name=machine \
    file://defconfig \
"

SRC_URI:append = " \
    file://0001-thermal-imx-Update-critical-temp-threshold.patch \
    file://0001-Revert-drm-panel-simple-drop-use-of-data-mapping-pro.patch \
    file://0002-drivers-chipidea-disable-runtime-pm-for-imx6ul.patch \
"

LINUX_VERSION ?= "6.3-rc"
KBRANCH = "master"
KERNEL_VERSION_SANITY_SKIP = "1"
SRCREV_machine = "fe15c26ee26efa11741a7b632e9f23b01aca4cc6"
SRCREV_machine:use-head-next = "${AUTOREV}"

S = "${WORKDIR}/git"

KCONFIG_MODE="--alldefconfig"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

inherit kernel-yocto kernel pkgconfig toradex-kernel-localversion

# Additional file deployed by recent mainline kernels
FILES:${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo"

KERNEL_CONFIG_NAME ?= "${KERNEL_PACKAGE_NAME}-config-${KERNEL_ARTIFACT_NAME}"
KERNEL_CONFIG_LINK_NAME ?= "${KERNEL_PACKAGE_NAME}-config"

export DTC_FLAGS = "-@"

# kconfiglib.KconfigError: init/Kconfig:70: error: couldn't parse 'default $(shell,$(srctree)/scripts/rust-version.sh $(RUSTC))': macro expanded to blank string
do_kernel_configcheck[noexec] = "1"

do_deploy:append() {
    cp -a ${B}/.config ${DEPLOYDIR}/${KERNEL_CONFIG_NAME}
    ln -sf ${KERNEL_CONFIG_NAME} ${DEPLOYDIR}/${KERNEL_CONFIG_LINK_NAME}
}
