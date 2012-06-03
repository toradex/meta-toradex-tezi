DESCRIPTION = "LXDE Appearance Obconfig"
HOMEPAGE = "http://lxde.org/"
SECTION = "x11"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=9d19a9495cc76dc96b703fb4aa157183"

DEPENDS = "menu-cache lxappearance"

SRC_URI = "${SOURCEFORGE_MIRROR}/lxde/${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "7c6381bc1ff60e23ef7f31d5e70a1803"
SRC_URI[sha256sum] = "0a7390ba9e59c132b1a82dbf766a4b472007cc891e5c7574f7f03088faa332b9"

inherit autotools gettext pkgconfig

FILES_${PN} += "${datadir}/lxappearance/obconf/obconf.glade \
	${libdir}/lxappearance/plugins/obconf.so"
FILES_${PN}-dbg += "${libdir}/lxappearance/plugins/.debug/obconf.so"

FILES_${PN}-dev += "${libdir}/lxappearance/plugins/obconf.la \
	${libdir}/lxappearance/plugins/obconf.a "
