SUMMARY = "MWiFiEx Configuration"
DESCRIPTION = "AP mode: Currently wifi chip fw doesn't support mode \
changes, this createsi multiple interfaces on boot; \
Power saving disable: Currently IW416 on Verdin AM62 is not stable \
with power saving enabled, so disable it."

inherit systemd

LICENSE = "CLOSED"

# For backwards compatibility
PROVIDES += "mwifiexap"
RREPLACES:${PN} = "mwifiexap"
RPROVIDES:${PN} = "mwifiexap"
RCONFLICTS:${PN} = "mwifiexap"

SRC_URI = "file://mwifiex.conf \
"

do_install () {
	install -d ${D}/etc/modprobe.d/
	install -m 0755 ${WORKDIR}/mwifiex.conf ${D}/etc/modprobe.d/mwifiex.conf
}
