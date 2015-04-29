FILESEXTRAPATHS_prepend := "${THISDIR}/libusbg:"

PV = "0.1.0-git"

SRCREV = "93631e618436989ebd7e9df2c997c175feb14bda"
SRC_URI = "git://github.com/kopasiak/libusbg.git \
    file://usbg.service \
    file://g1.schema \
"

DEPENDS = "libconfig"

inherit autotools pkgconfig systemd

do_install_append () {
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/usbg.service ${D}${systemd_unitdir}/system

    install -d ${D}${sysconfdir}/usbg/
    install -m 0644 ${WORKDIR}/g1.schema ${D}${sysconfdir}/usbg/g1.schema
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "usbg.service"
