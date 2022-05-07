FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}_5.4-2.3.x:"

require linux-toradex-tezi-common.inc

SRC_URI:append = " \
    file://0001-arm64-dts-apalis-imx8-drop-m4-and-rpmsg-from-reserve.patch \
    file://0002-drm-add-1280x720-as-builtin-edid.patch \
    file://0002-apalis-imx8-drm-bridge-cadence-update-MHDP-bridge-driver-phy-read-timeout.patch \
"

do_configure:append () {
    kernel_configure_variable WIRELESS                  n
    kernel_configure_variable WIRELESS_EXT              n
    kernel_configure_variable CAN                       n
    kernel_configure_variable BT                        n
    kernel_configure_variable USB_CONFIGFS              y
    kernel_configure_variable USB_CONFIGFS_RNDIS        y
    kernel_configure_variable USB_CONFIGFS_MASS_STORAGE y
}


