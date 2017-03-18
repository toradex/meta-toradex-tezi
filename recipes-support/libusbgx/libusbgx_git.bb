SUMMARY = "USB Gadget neXt Configfs Library"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

inherit autotools pkgconfig systemd

DEPENDS = "libconfig"

EXTRA_OECONF = "--includedir=${includedir}/usbgx"

PV = "0.1.0+git${SRCPV}"
SRCREV = "566993a7647ed5cb36098f27084fd2d5d1f0f017"
SRCBRANCH = "os_descriptors"
SRC_URI = " \
    git://github.com/toradex/libusbgx.git;branch=${SRCBRANCH} \
    file://usbg.service \
    file://g1.schema \
"

S = "${WORKDIR}/git"

do_install_append () {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/usbg.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/usbg/
    install -m 0644 ${WORKDIR}/g1.schema ${D}${sysconfdir}/usbg/g1.schema
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "usbg.service"
