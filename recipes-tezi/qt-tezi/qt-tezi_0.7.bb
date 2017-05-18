DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://eng-git.toradex.int/cgit/qt-tezi.git;branch=master;protocol=http \
    file://defaults \
    file://rc.local \
    file://udhcpd.conf \
    file://ifplugd.dhcp.action \
    file://ifplugd.usb.action \
"

SRCREV = "04a0bb68f854fcd2af2ac1bf7e07f9247d6b3fb3"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=0643dd948aaba20e02e69d3d487dc6cf"

S = "${WORKDIR}/git"
inherit qt4e

TOUCH = ' ${@bb.utils.contains("MACHINE_FEATURES", "touchscreen", "tslib tslib-calibrate tslib-tests qt4-embedded-plugin-mousedriver-tslib", "",d)}'

DEPENDS += " \
    qjson \
    libusbgx \
"

RDEPENDS_${PN} += " \
    flash-wince \
    util-linux-sfdisk \
    util-linux-blkid \
    util-linux-blkdiscard \
    libqt-embeddedcore4 \
    libqt-embeddedgui4 \
    qt4-embedded-fonts-ttf-vera \
    qt4-embedded-plugin-mousedriver-tslib \
    ${TOUCH} \
"

# Ensure we have some plugins for some useful image formats
RRECOMMENDS_${PN} += "\
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-gfxdriver-gfxvnc \
"

FILES_${PN} = " \
    ${sysconfdir} \
    ${sysconfdir}/rc.local \
    ${sysconfdir}/defaults/tezi \
    ${sysconfdir}/udhcpd.conf \
    ${sysconfdir}/ifplugd \
    ${bindir} \
    ${datadir}/tezi/ \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/rc.local ${D}${sysconfdir}

    install -d ${D}${sysconfdir}/defaults/
    install -m 0755 ${WORKDIR}/defaults ${D}${sysconfdir}/defaults/tezi

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/ifplugd
    install -m 0755 ${WORKDIR}/ifplugd.dhcp.action ${D}${sysconfdir}/ifplugd
    install -m 0755 ${WORKDIR}/ifplugd.usb.action ${D}${sysconfdir}/ifplugd

}

FILES_${PN}-dbg += "${bindir}/.debug"
