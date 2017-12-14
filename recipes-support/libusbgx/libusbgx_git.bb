SUMMARY = "USB Gadget neXt Configfs Library"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

inherit autotools pkgconfig systemd

DEPENDS = "libconfig"

EXTRA_OECONF = "--includedir=${includedir}/usbgx"

PV = "0.1.0+git${SRCPV}"
SRCREV = "904b04c4b46ec673197ad1d8291a0aabe6a34f52"
SRCBRANCH = "master"
SRC_URI = " \
    git://github.com/libusbgx/libusbgx.git;branch=${SRCBRANCH} \
    file://usbg.service \
    file://g1.schema \
"

S = "${WORKDIR}/git"

do_install_append () {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/usbg.service ${D}${systemd_unitdir}/system
    fi

    install -d ${D}${sysconfdir}/usbg/
    install -m 0644 ${WORKDIR}/g1.schema ${D}${sysconfdir}/usbg/g1.schema
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "usbg.service"

PACKAGES =+ "${PN}-examples"

FILES_${PN}-examples = " \
    ${bindir}/gadget-* \
    ${bindir}/show-gadgets \
    ${bindir}/show-udcs \
    ${systemd_unitdir}/system/usbg.service \
    ${sysconfdir}/usbg/g1.schema \
"
