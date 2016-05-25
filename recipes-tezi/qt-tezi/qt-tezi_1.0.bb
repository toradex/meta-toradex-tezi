DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://eng-git.toradex.int/cgit/ags/tez-i.git;branch=master;protocol=http"

SRCREV = "a813ba6fdcc7273298759430f7fb7d0bd4d60c65"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=47823c08586e6dbf1bf50405070bf259"

S = "${WORKDIR}/git"
inherit qt4e

TOUCH = ' ${@base_contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

DEPENDS += " \
    qjson \
"

RDEPENDS_${PN} += " \
    libqt-embedded3support4 \
    libqt-embeddeddeclarative4 \
    libqt-embeddedcore4 \
    libqt-embeddedgui4 \
    libqt-embeddedhelp4 \
    libqt-embeddedsvg4 \
    libqt-embeddedxml4 \
    qt4-embedded-fonts-ttf-dejavu \
    qt4-embedded-fonts-ttf-vera \
    qt4-embedded-plugin-imageformat-svg \
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-mousedriver-tslib \
    ${TOUCH} \
    qt4-embedded-assistant \
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
    install -m 0755 ${S}/recovery ${D}${bindir}/
}

FILES_${PN}-dbg += "${bindir}/.debug"

#inherit update-rc.d
