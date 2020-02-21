SUMMARY = "A high-level Zeroconf interface for Qt-based client-server applications."
HOMEPAGE = "https://github.com/jonesmz/qtzeroconf"
LICENSE = "LGPL-2.1"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4fbd65380cdd255951079008b364516c"

SRCREV = "1e51e93b3f1805599e04c01c1abd94e6d346d870"
SRC_URI = " \
    git://github.com/jonesmz/qtzeroconf.git;protocol=https \
    file://0001-project_settings.pri-add-versioning-definitions.patch \
    file://0001-dependency.pri-support-static-libraries.patch \
"

S = "${WORKDIR}/git"

PV = "0.0.1+git${SRCPV}"

DEPENDS = "avahi"

inherit qmake5

EXTRAQCONFFUNCS += "qtzeroconf_preconfigure"

qtzeroconf_preconfigure() {
	if [ -n "${CONFIGURESTAMPFILE}" -a -e "${CONFIGURESTAMPFILE}" ]; then
		rm -rf ${S}/bin
	fi
}

do_install () {
	install -d ${D}${libdir}
	cp -fP ${S}/bin/* ${D}${libdir}

	install -d ${D}${includedir}/qtzeroconf
	cp -fP ${S}/include/qtzeroconf/* ${D}${includedir}/qtzeroconf/
}
