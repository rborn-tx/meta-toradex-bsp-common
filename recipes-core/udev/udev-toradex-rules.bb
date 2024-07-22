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

SRC_URI:append:verdin-am62 = " \
    file://10-toradex-can0-ifname.link \
    file://10-toradex-can1-ifname.link \
"
SRC_URI:append:verdin-imx8mm = " \
    file://10-toradex-can0-ifname.link \
"
SRC_URI:append:verdin-imx8mp = " \
    file://10-toradex-can0-ifname.link \
    file://10-toradex-can1-ifname.link \
"

S = "${UNPACKDIR}"

do_install () {
    install -d ${D}${sysconfdir}/udev/rules.d
    install -d ${D}${sysconfdir}/udev/scripts
    install -d ${D}${sysconfdir}/systemd/network
    # 10-toradex-can*-ifname.link files are only available for the Verdin family
    if [ -f ${S}/10-toradex-can0-ifname.link ]; then
        install -m 0644 ${S}/10-toradex-can0-ifname.link ${D}${sysconfdir}/systemd/network/
    fi
    if [ -f ${S}/10-toradex-can1-ifname.link ]; then
        install -m 0644 ${S}/10-toradex-can1-ifname.link ${D}${sysconfdir}/systemd/network/
    fi
    install -m 0644 ${S}/10-toradex-wifi-ifnames.link ${D}${sysconfdir}/systemd/network/
    install -m 0644 ${S}/99-toradex.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0644 ${S}/bootpart-automount.rules ${D}${sysconfdir}/udev/rules.d/
    install -m 0755 ${S}/toradex-adc.sh ${D}${sysconfdir}/udev/scripts/
    install -m 0755 ${S}/toradex-mount-bootpart.sh ${D}${sysconfdir}/udev/scripts/

    sed -i 's|@systemd_unitdir@|${systemd_unitdir}|g' ${D}${sysconfdir}/udev/scripts/toradex-mount-bootpart.sh
    sed -i 's|@base_sbindir@|${base_sbindir}|g' ${D}${sysconfdir}/udev/scripts/toradex-mount-bootpart.sh
}
