DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://eng-git.toradex.int/cgit/ags/tez-i.git;branch=master;protocol=http"

SRCREV = "a529ea16e1bd715ae9297e5b234ca85b1386479e"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=47823c08586e6dbf1bf50405070bf259"

S = "${WORKDIR}/git"
inherit qt4e

TOUCH = ' ${@base_contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

DEPENDS += " \
    qjson \
"

RDEPENDS_${PN} += " \
    libqt-embedded3support4 \
    libqt-embeddedcore4 \
    libqt-embeddedgui4 \
    libqt-embeddedhelp4 \
    qt4-embedded-fonts-ttf-dejavu \
    qt4-embedded-fonts-ttf-vera \
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-mousedriver-tslib \
    ${TOUCH} \
"

# Ensure we have some plugins for some useful image formats
RRECOMMENDS_${PN} += "\
    qt4-embedded-plugin-imageformat-gif \
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-imageformat-tiff \
"

FILES_${PN} = " \
    ${sysconfdir} \
    ${bindir} \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/tezi ${D}${bindir}/
}

FILES_${PN}-dbg += "${bindir}/.debug"

#inherit update-rc.d
