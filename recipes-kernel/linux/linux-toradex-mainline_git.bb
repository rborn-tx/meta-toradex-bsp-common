SUMMARY = "Toradex mainline Linux kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

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
    file://0001-arm-dts-colibri-imx6-usb-dual-role-switching.patch \
    file://0002-arm-dts-colibri-imx6-move-vbus-supply-to-module-leve.patch \
    file://0003-arm-dts-colibri-imx6-specify-usbh_pen-gpio-being-act.patch \
    file://0001-arm-dts-colibri-imx6ull-keep-peripherals-disabled.patch \
    file://0002-arm-dts-colibri-imx6ull-enable-default-peripherals.patch \
    file://0001-ARM-dts-colibri-imx6ull-Enable-dual-role-switching.patch \
    file://0002-drivers-chipidea-disable-runtime-pm-for-imx6ul.patch \
    file://0001-rtc-snvs-Allow-a-time-difference-on-clock-register-r.patch \
"

LINUX_VERSION ?= "6.0.6"
LINUX_VERSION:use-head-next ?= "6.0"
KBRANCH = "linux-6.0.y"
KERNEL_VERSION_SANITY_SKIP = "1"
SRCREV_machine = "3a2fa3c01fc7c2183eb3278bd912e5bcec20eb2a"
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
