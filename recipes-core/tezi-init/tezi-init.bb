SUMMARY = "Toradex Easy Inistaller init"
DESCRIPTION = "Basic init system for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

RDEPENDS_${PN} = "busybox"

SRC_URI = "file://init \
           file://udhcpd.conf \
           file://ifplugd.dhcp.action \
           file://ifplugd.usb.action \
	  "

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -d ${D}${sysconfdir}
	install -m 0755 ${WORKDIR}/init ${D}
	install -m 0755 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}
	install -d ${D}${sysconfdir}/ifplugd
	install -m 0755 ${WORKDIR}/ifplugd.dhcp.action ${D}${sysconfdir}/ifplugd
	install -m 0755 ${WORKDIR}/ifplugd.usb.action ${D}${sysconfdir}/ifplugd
}

FILES_${PN} = "/init \
               ${sysconfdir}/udhcpd.conf \
               ${sysconfdir}/ifplugd \
"
