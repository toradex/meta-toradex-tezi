FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}_5.4-2.3.x:"

require linux-toradex-tezi-common.inc

do_configure_append () {
    kernel_configure_variable WIRELESS                  n
    kernel_configure_variable WIRELESS_EXT              n
    kernel_configure_variable CAN                       n
    kernel_configure_variable BT                        n
    kernel_configure_variable USB_CONFIGFS              y
    kernel_configure_variable USB_CONFIGFS_RNDIS        y
    kernel_configure_variable USB_CONFIGFS_MASS_STORAGE y
}


