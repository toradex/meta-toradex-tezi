SUMMARY = "Toradex Easy Inistaller init"
DESCRIPTION = "Basic init system for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

RDEPENDS_${PN} = "busybox"

SRC_URI = "file://init \
	  "

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
	install -m 0755 ${WORKDIR}/init ${D}
}

FILES_${PN} = "/init"
