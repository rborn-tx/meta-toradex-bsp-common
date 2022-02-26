DESCRIPTION = "Toradex Easy Installer Metadata"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://prepare.sh \
    file://wrapup.sh \
    file://toradexlinux.png \
    file://marketing.tar;unpack=false \
    file://LA_OPT_NXP_SW.html \
"

# We want to always check the latest EULA file in image_type_tezi.bbclass
# So we put LA_OPT_NXP_SW.html to sstate allow overlap files, this
# ensures it could be deployed to ${DEPLOY_DIR_IMAGE} as a backup even
# it already existed.
SSTATE_ALLOW_OVERLAP_FILES:prepend = "${DEPLOY_DIR_IMAGE}/LA_OPT_NXP_SW.html "

inherit deploy nopackages

do_deploy () {
    install -m 644 ${WORKDIR}/prepare.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/wrapup.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/toradexlinux.png ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/marketing.tar ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/LA_OPT_NXP_SW.html ${DEPLOYDIR}
}

addtask deploy before do_build after do_install

PACKAGE_ARCH = "${MACHINE_ARCH}"
