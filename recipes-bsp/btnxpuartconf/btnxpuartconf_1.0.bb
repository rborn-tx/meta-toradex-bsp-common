SUMMARY = "Bluetooth NXP UART (btnxpuart) Configuration"

DESCRIPTION = "As the power-down pin (PD#) is shared and handled by \
mmc-pwrseq for mwifiex_sdio, make sure mwifiex gets loaded first."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://btnxpuart.conf"

do_install () {
	install -d ${D}/etc/modprobe.d/
	install -m 0755 ${WORKDIR}/btnxpuart.conf ${D}/etc/modprobe.d/btnxpuart.conf
}
