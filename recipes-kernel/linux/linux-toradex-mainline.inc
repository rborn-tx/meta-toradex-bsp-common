SUMMARY = "Toradex mainline Linux kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM ?= "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel toradex-kernel-config toradex-kernel-localversion

SRC_URI = " \
    git://git.toradex.com/linux-toradex.git;protocol=https;branch=${KBRANCH};name=machine \
"

S = "${WORKDIR}/git"

DEPENDS += "coreutils-native"

kernel_do_configure_append_preempt-rt() {
    # switch to use PREEMPT_RT
    kernel_configure_variable PREEMPT_NONE n
    kernel_configure_variable PREEMPT_VOLUNTARY n
    kernel_configure_variable PREEMPT n
    kernel_configure_variable PREEMPT_RT y
    oe_runmake olddefconfig
}

# Additional file deployed by recent mainline kernels
FILES_${KERNEL_PACKAGE_NAME}-base += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.builtin.modinfo"

KERNEL_CONFIG_NAME ?= "${KERNEL_PACKAGE_NAME}-config-${KERNEL_ARTIFACT_NAME}"
KERNEL_CONFIG_LINK_NAME ?= "${KERNEL_PACKAGE_NAME}-config"

do_deploy_append() {
    cp -a ${B}/.config ${DEPLOYDIR}/${KERNEL_CONFIG_NAME}
    ln -sf ${KERNEL_CONFIG_NAME} ${DEPLOYDIR}/${KERNEL_CONFIG_LINK_NAME}
}
