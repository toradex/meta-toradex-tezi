SUMMARY = "A high-level Zeroconf interface for Qt-based client-server applications."
HOMEPAGE = "https://github.com/jonesmz/qtzeroconf"
LICENSE = "LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4fbd65380cdd255951079008b364516c"

SRCREV = "806570b8eef52be8d10780716f3d227de5636c30"
SRC_URI = " \
    git://github.com/jonesmz/qtzeroconf.git;branch=master;protocol=https \
    file://0001-project_settings.pri-add-versioning-definitions.patch \
    file://0001-dependency.pri-support-static-libraries.patch \
"

S = "${WORKDIR}/git"

PV = "0.0.1+git${SRCPV}"

DEPENDS = "avahi qtbase"

inherit pkgconfig qmake5

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
