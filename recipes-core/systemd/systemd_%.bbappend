FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://boot.mount"

do_install_append() {
    install -m 0644 ${WORKDIR}/boot.mount ${D}${systemd_unitdir}/system/
    install -d ${D}${sysconfdir}/systemd/system/default.target.wants
    ln -sf ${systemd_unitdir}/system/boot.mount \
        ${D}${sysconfdir}/systemd/system/default.target.wants/boot.mount
}

# This allows for udevd automounting with mounts accessible to all.
do_configure_prepend () {
    sed -i '/PrivateMounts=yes/d' ${S}/units/systemd-udevd.service.in
}

FILES_${PN} += "${sysconfdir}/systemd/system/default.target.wants/*.mount"
FILES_${PN} += "${base_libdir}/systemd/system/*.mount"
