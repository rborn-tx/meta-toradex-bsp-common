DESCRIPTION = "udev rules for Toradex SOCs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://99-toradex.rules \
    file://10-toradex-wifi-ifnames.link \
    file://toradex-adc.sh \
"

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}${sysconfdir}/udev/scripts
    install -d ${D}${sysconfdir}/systemd/network
    install -m 0644 ${WORKDIR}/99-toradex.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0755 ${WORKDIR}/toradex-adc.sh ${D}${sysconfdir}/udev/scripts/
    install -m 0644 ${WORKDIR}/10-toradex-wifi-ifnames.link ${D}${sysconfdir}/systemd/network/
}
