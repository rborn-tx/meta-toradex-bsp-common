SUMMARY = "Toradex mainline Linux kernel"
SUMMARY:preempt-rt = "Toradex mainline real-Time Linux kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM ?= "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

DEPENDS += "coreutils-native lzop-native"

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

LINUX_VERSION ?= "5.18-rc3"
LINUX_VERSION:use-head-next ?= "5.18"
KBRANCH = "master"
KERNEL_VERSION_SANITY_SKIP = "1"
SRCREV_machine = "b2d229d4ddb17db541098b83524d901257e93845"
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

#######################################################################

LINUX_REPO:preempt-rt = "git://git.kernel.org/pub/scm/linux/kernel/git/rt/linux-rt-devel.git"
LINUX_VERSION:preempt-rt ?= "v5.17.1-rt17"
KBRANCH:preempt-rt = "linux-5.17.y-rt"
SRCREV_machine:preempt-rt = "cc1f02f214d8d8eea9e4cc7ed2d8ff8d894ffee6"
LINUX_VERSION:use-head-next:preempt-rt ?= "5.18.0-rt"
KBRANCH:use-head-next:preempt-rt = "linux-5.18.y-rt"
SRCREV_machine:preempt-rt:use-head-next = "${AUTOREV}"

SRC_URI:append:preempt-rt = " \
    file://preempt-rt.scc \
    file://preempt-rt-less-latency.scc \
"

#######################################################################
