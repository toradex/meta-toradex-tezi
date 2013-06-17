DESCRIPTION = "LXDE Appearance Obconfig"
HOMEPAGE = "http://lxde.org/"
SECTION = "x11"
PR = "r1"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "menu-cache lxappearance"

SRC_URI = "${SOURCEFORGE_MIRROR}/lxde/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "8bf23c90febe6a655e0f86c80e44725d"
SRC_URI[sha256sum] = "b7cfda429b0bd6ed5cca1b3ba9fe3f7bc058f978739cad9817dddd181a1d6692"

inherit autotools gettext pkgconfig

FILES_${PN} += "${datadir}/lxappearance/obconf/obconf.glade \
	${libdir}/lxappearance/plugins/obconf.so"
FILES_${PN}-dbg += "${libdir}/lxappearance/plugins/.debug/obconf.so"
FILES_${PN}-dev += "${libdir}/lxappearance/plugins/obconf.la "
FILES_${PN}-staticdev += "${libdir}/lxappearance/plugins/obconf.a "
