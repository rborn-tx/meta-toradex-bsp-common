DESCRIPTION = "udev rules for Toradex SOCs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://10-toradex-wifi-ifnames.link \
    file://99-toradex.rules \
    file://bootpart-automount.rules \
    file://toradex-adc.sh \
    file://toradex-mount-bootpart.sh \
"

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}${sysconfdir}/udev/scripts
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/10-toradex-wifi-ifnames.link ${D}${sysconfdir}/systemd/network/
    install -m 0644 ${WORKDIR}/99-toradex.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${WORKDIR}/bootpart-automount.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0755 ${WORKDIR}/toradex-adc.sh ${D}${sysconfdir}/udev/scripts/
    install -m 0755 ${WORKDIR}/toradex-mount-bootpart.sh ${D}${sysconfdir}/udev/scripts/

    sed -i 's|@systemd_unitdir@|${systemd_unitdir}|g' ${D}${sysconfdir}/udev/scripts/toradex-mount-bootpart.sh
    sed -i 's|@base_sbindir@|${base_sbindir}|g' ${D}${sysconfdir}/udev/scripts/toradex-mount-bootpart.sh
}
