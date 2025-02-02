FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGE_ARCH:tdx = "${MACHINE_ARCH}"

SRC_URI:append:tdx = " \
    file://g1.schema.in \
    file://setup-board.sh \
"

do_install:append:tdx() {
    sed -e "s:@@PRODUCT_NAME@@:${MACHINE}:" ${UNPACKDIR}/g1.schema.in > ${UNPACKDIR}/g1.schema
    sed -i "s:IMPORT_SCHEMAS=.*:IMPORT_SCHEMAS=\"g1\":" ${D}${sysconfdir}/default/usbgx

    install -d ${D}${sysconfdir}/usbgx
    install -m 0644 ${UNPACKDIR}/g1.schema ${D}${sysconfdir}/usbgx/g1.schema

    install -d ${D}${sysconfdir}/usbgx.d
    install -m 0755 ${UNPACKDIR}/setup-board.sh ${D}${sysconfdir}/usbgx.d
}
