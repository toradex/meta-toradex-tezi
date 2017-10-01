SECTION = "network"
SUMMARY = "RNDIS usb client configuration and startup"
# The license is meant for this recipe and the files it installs.
# RNDIS is part of the kernel, systemd-networkd is part of systemd
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PR = "r1"

# Tegra Kernels:
# The kernel provides with CONFIG_USB_G_ANDROID a composite gadget driver
# among other with RNDIS functionality.
# i.MX6 Kernels:
# The kernel provides with CONFIG_USB_ETH_RNDIS an USB gadget driver which
# provides RNDIS functionality.
# Vybrid Kernels:
# The kernel provides with CONFIG_USB_CONFIGFS_RNDIS an USB gadget driver
# which provides RNDIS functionality. RNDIS needs to be configured and
# enabled through configfs, which is done by libusbg (usbg.service)

# This package contains systemd files to configure RNDIS at startup (Tegra
# and i.MX6), configures a fix IP locally and provides a DHCP server using
# systemd-networkd
# Local IP is 192.168.11.1, remote IP is 192.168.11.2

inherit allarch systemd

SRC_URI = " \
    file://start-rndis.sh \
    file://usb-rndis.service \
"

do_install() {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/start-rndis.sh ${D}/${bindir}/

    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/usb-rndis.service ${D}${systemd_unitdir}/system
}

FILES_${PN} += " \
    ${systemd_unitdir}/system \
"

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "usb-rndis.service"
SYSTEMD_AUTO_ENABLE_mx6 = "disable"
