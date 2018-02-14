FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

SRC_URI += " \
    file://0001-systemd-udevd.service.in-set-MountFlags-shared.patch \
"
