SUMMARY = "MWiFiEx AP mode"
DESCRIPTION = "Currently wifi chip fw \
doesn't support mode changes, this creates\ 
multiple interfaces on boot"

inherit systemd

LICENSE = "CLOSED"

SRC_URI = "file://mwifiex.conf \
		   "

do_install () {
	install -d ${D}/etc/modprobe.d/
	install -m 0755 ${WORKDIR}/mwifiex.conf ${D}/etc/modprobe.d/mwifiex.conf
}

