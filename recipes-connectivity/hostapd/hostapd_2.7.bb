HOMEPAGE = "http://w1.fi/hostapd/"
SECTION = "kernel/userland"
LICENSE = "GPLv2 | BSD"
LIC_FILES_CHKSUM = "file://${B}/README;md5=9f542dba9d227d1a51f4405a415cc2b2"
DEPENDS = "libnl openssl"
SUMMARY = "User space daemon for extended IEEE 802.11 management"

inherit update-rc.d systemd
INITSCRIPT_NAME = "hostapd"

SYSTEMD_SERVICE_${PN} = "hostapd.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

SRC_URI = " \
    http://w1.fi/releases/hostapd-${PV}.tar.gz \
    file://defconfig \
    file://init \
    file://hostapd.service \
"

S = "${WORKDIR}/hostapd-${PV}"
B = "${WORKDIR}/hostapd-${PV}/hostapd"

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig ${B}/.config
}

do_compile() {
    export CFLAGS="-MMD -O2 -Wall -g -I${STAGING_INCDIR}/libnl3"
    make
}

do_install() {
    install -d ${D}${sbindir} ${D}${sysconfdir}/init.d ${D}${systemd_unitdir}/system/
    install -m 0644 ${B}/hostapd.conf ${D}${sysconfdir}
    install -m 0755 ${B}/hostapd ${D}${sbindir}
    install -m 0755 ${B}/hostapd_cli ${D}${sbindir}
    install -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/hostapd
    install -m 0644 ${WORKDIR}/hostapd.service ${D}${systemd_unitdir}/system/
    sed -i -e 's,@SBINDIR@,${sbindir},g' -e 's,@SYSCONFDIR@,${sysconfdir},g' ${D}${systemd_unitdir}/system/hostapd.service
}

CONFFILES_${PN} += "${sysconfdir}/hostapd.conf"

SRC_URI[md5sum] = "8d3799f3a3c247cff47d41503698721b"
SRC_URI[sha256sum] = "21b0dda3cc3abe75849437f6b9746da461f88f0ea49dd621216936f87440a141"

