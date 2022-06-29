SUMMARY = "USB Gadget neXt Configfs Library"

LICENSE = "GPL-2.0-only & LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

inherit autotools pkgconfig systemd

DEPENDS = "libconfig"

EXTRA_OECONF = "--includedir=${includedir}/usbgx"

PV = "0.2.0+git${SRCPV}"
SRCREV = "060784424609d5a4e3bce8355f788c93f09802a5"
SRCBRANCH = "master"
SRC_URI = " \
    git://github.com/libusbgx/libusbgx.git;branch=${SRCBRANCH};protocol=https \
    file://usbg.service \
    file://g1.schema.in \
"

S = "${WORKDIR}/git"

MACHINE_NAME ?= "${MACHINE}"
do_compile:append () {
    sed -e "s:@@PRODUCT_NAME@@:${MACHINE_NAME}:" ${WORKDIR}/g1.schema.in > ${WORKDIR}/g1.schema
}

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/usbg.service ${D}${systemd_unitdir}/system
    fi

    install -d ${D}${sysconfdir}/usbg/
    install -m 0644 ${WORKDIR}/g1.schema ${D}${sysconfdir}/usbg/g1.schema
}

SYSTEMD_PACKAGES = "${PN}-examples"
SYSTEMD_SERVICE:${PN}-examples = "usbg.service"

PACKAGES =+ "${PN}-examples"
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILES:${PN}-examples = " \
    ${bindir}/gadget-* \
    ${bindir}/show-gadgets \
    ${bindir}/show-udcs \
    ${sysconfdir}/usbg/g1.schema \
"
