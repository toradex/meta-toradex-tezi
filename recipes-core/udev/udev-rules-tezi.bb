DESCRIPTION = "udev rules for Tezi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://81-ifplugd.rules \
    file://ifplugd.agent \
    file://ifplugd.action \
    file://ifplugd.conf \
"

S = "${@d.getVar("UNPACKDIR") or '${WORKDIR}'}"

do_configure() {
	:
}

do_install () {
	install -d ${D}${sysconfdir}/udev/rules.d
	install -m 0644 ${S}/81-ifplugd.rules ${D}${sysconfdir}/udev/rules.d

	install -d ${D}${sysconfdir}/ifplugd
	install -m 0755 ${S}/ifplugd.action ${D}${sysconfdir}/ifplugd

	install -d ${D}${sysconfdir}/default
	install -m 0644 ${S}/ifplugd.conf ${D}${sysconfdir}/default/ifplugd

	install -d ${D}${base_libdir}/udev/
	install -m 0755 ${S}/ifplugd.agent ${D}${base_libdir}/udev
}
