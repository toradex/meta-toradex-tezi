SUMMARY = "A high-level Zeroconf interface for Qt-based client-server applications."
HOMEPAGE = "https://github.com/jonesmz/qtzeroconf"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4fbd65380cdd255951079008b364516c"
DEPENDS += "qtbase avahi"

SRCREV = "1e51e93b3f1805599e04c01c1abd94e6d346d870"
SRC_URI = " \
    git://github.com/jonesmz/qtzeroconf.git;protocol=https \
"

S = "${WORKDIR}/git"

inherit qmake5

EXTRA_OEMAKE = "DESTDIR=${libdir}"

RDEPENDS_${PN} += "libavahi-client libavahi-qt5"

do_install_append () {
	# TODO: Fix qmake/project file to do this properly
	install -d ${D}${libdir}
	install -m 0755 ${S}/bin/libqtzeroconf-service.so.0.0.1 ${D}${libdir}
	install -m 0755 ${S}/bin/libqtzeroconf-common.so.0.0.1 ${D}${libdir}
	install -m 0755 ${S}/bin/libqtzeroconf-browser.so.0.0.1 ${D}${libdir}

	ln -sf libqtzeroconf-service.so.0.0.1 ${D}${libdir}/libqtzeroconf-service.so.0.0
	ln -sf libqtzeroconf-service.so.0.0.1 ${D}${libdir}/libqtzeroconf-service.so.0
	ln -sf libqtzeroconf-service.so.0.0.1 ${D}${libdir}/libqtzeroconf-service.so

	ln -sf libqtzeroconf-common.so.0.0.1 ${D}${libdir}/libqtzeroconf-common.so.0.0
	ln -sf libqtzeroconf-common.so.0.0.1 ${D}${libdir}/libqtzeroconf-common.so.0
	ln -sf libqtzeroconf-common.so.0.0.1 ${D}${libdir}/libqtzeroconf-common.so

	ln -sf libqtzeroconf-browser.so.0.0.1 ${D}${libdir}/libqtzeroconf-browser.so.0.0
	ln -sf libqtzeroconf-browser.so.0.0.1 ${D}${libdir}/libqtzeroconf-browser.so.0
	ln -sf libqtzeroconf-browser.so.0.0.1 ${D}${libdir}/libqtzeroconf-browser.so

	install -d ${D}${includedir}/qtzeroconf
	install -m 0644 ${S}/include/qtzeroconf/zconfservice.h ${D}${includedir}/qtzeroconf/
	install -m 0644 ${S}/include/qtzeroconf/zconfservicebrowser.h ${D}${includedir}/qtzeroconf/
	install -m 0644 ${S}/include/qtzeroconf/zconfserviceclient.h ${D}${includedir}/qtzeroconf/
}
