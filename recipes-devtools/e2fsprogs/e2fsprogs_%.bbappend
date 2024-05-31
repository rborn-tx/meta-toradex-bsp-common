FILESEXTRAPATHS:prepend := "${THISDIR}/e2fsprogs:"

SRC_URI:append:tdx = " file://e2fsck.conf"

OURFILEPATH = "${@d.getVar("UNPACKDIR") or '${WORKDIR}'}"

do_install:append:tdx () {
    install -d ${D}${sysconfdir}
    install -m 0644 ${OURFILEPATH}/e2fsck.conf ${D}${sysconfdir}/e2fsck.conf
}

FILES:e2fsprogs-e2fsck:append:tdx = " ${sysconfdir}/e2fsck.conf"
