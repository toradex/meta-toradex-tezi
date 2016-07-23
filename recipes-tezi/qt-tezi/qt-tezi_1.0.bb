DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://eng-git.toradex.int/cgit/ags/tez-i.git;branch=master;protocol=http"

SRCREV = "1d5218c1be7240cd513b662c31048baaf6ee256e"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=47823c08586e6dbf1bf50405070bf259"

S = "${WORKDIR}/git"
inherit qt4e

TOUCH = ' ${@base_contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests", "",d)}'

DEPENDS += " \
    qjson \
"

RDEPENDS_${PN} += " \
    libqt-embeddedcore4 \
    libqt-embeddedgui4 \
    qt4-embedded-fonts-ttf-vera \
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-mousedriver-tslib \
    ${TOUCH} \
"

# Ensure we have some plugins for some useful image formats
RRECOMMENDS_${PN} += "\
    qt4-embedded-translation-qt \
    qt4-embedded-plugin-imageformat-gif \
    qt4-embedded-plugin-imageformat-jpeg \
"

FILES_${PN} = " \
    ${sysconfdir} \
    ${bindir} \
    ${datadir}/tezi/ \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/
}

FILES_${PN}-dbg += "${bindir}/.debug"

#inherit update-rc.d
