SECTION = "network"
DESCRIPTION = "RNDIS usb client configuration and startup"
RDEPENDS_${PN} = ""
# The license is meant for this recipe and the files it installs.
# RNDIS is part of the kernel, udhcpd is part of busybox
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

PR = "r1"

# Tegra Kernels:
# The kernel provides with CONFIG_USB_G_ANDROID a composite gadget driver among other with RNDIS functionality.
# Others:
# The kernel provides with  CONFIG_USB_ETH_RNDIS a USB gadget driver which provides RNDIS functionality.

# This package contains systemd files to configure RNDIS at startup, configures a fix IP localy and provides a dhcp server on the new interface.
# Local IP is 192.168.11.2, remote IP is 192.168.11.1

inherit allarch systemd

SRC_URI_COMMON = " \
    file://start-rndis.sh \
    file://usb-rndis.service \
    file://udhcpd-usb-rndis.conf \
"

SRC_URI = " \
    ${SRC_URI_COMMON} \
"

SRC_URI_tegra = " \
    ${SRC_URI_COMMON} \
    file://usb-rndis.rules \
"

do_install() {
    install -d ${D}/${sysconfdir} ${D}/${bindir}
    install -m 0755 ${WORKDIR}/start-rndis.sh ${D}/${bindir}/
    install -m 0644 ${WORKDIR}/udhcpd-usb-rndis.conf ${D}/${sysconfdir}/

    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/usb-rndis.service ${D}${systemd_unitdir}/system
}

do_install_append_tegra() {
    install -d ${D}/${sysconfdir}/udev/rules.d
    install -m 0644 ${WORKDIR}/usb-rndis.rules ${D}/${sysconfdir}/udev/rules.d
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "usb-rndis.service"
SYSTEMD_AUTO_ENABLE = "disable"

