FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append = " file://fstrim.service file://fstrim.timer"

inherit systemd

OURFILEPATH = "${@d.getVar("UNPACKDIR") or '${WORKDIR}'}"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} = "fstrim.service fstrim.timer"
SYSTEMD_AUTO_ENABLE = "disable"

do_install:append() {
        install -d ${D}${systemd_unitdir}/system
        install -m 0644 ${OURFILEPATH}/fstrim.service ${D}${systemd_unitdir}/system/
        install -m 0644 ${OURFILEPATH}/fstrim.timer ${D}${systemd_unitdir}/system/
        sed -i -e 's,@SBINDIR@,${sbindir},g' \
                -e 's,@SYSCONFDIR@,${sysconfdir},g' \
                ${D}${systemd_unitdir}/system/*.service
}
