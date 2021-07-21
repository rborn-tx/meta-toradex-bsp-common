FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append_ = " file://fstrim.service file://fstrim.timer"

inherit systemd

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "fstrim.service fstrim.timer"
SYSTEMD_AUTO_ENABLE = "disable"

do_install_append() {
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${WORKDIR}/fstrim.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/fstrim.timer ${D}${systemd_unitdir}/system/
        sed -i -e 's,@SBINDIR@,${sbindir},g' \
                -e 's,@SYSCONFDIR@,${sysconfdir},g' \
                ${D}${systemd_unitdir}/system/*.service
}

