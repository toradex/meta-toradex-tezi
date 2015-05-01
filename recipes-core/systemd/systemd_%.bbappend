FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

SRC_URI += " \
    file://networkd_dont_stop_the_dhcp_server.patch \
"

PACKAGECONFIG_append = " networkd"
