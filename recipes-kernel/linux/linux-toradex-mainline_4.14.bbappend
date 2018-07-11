FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex-mainline-4.14:"

SRC_URI_append_apalis-t30-mainline = " \
    file://0034-apalis-t30-tk1-mainline-tezi-specific-kernel-configu.patch \
    file://0036-apalis-t30-mainline-usb-device-aka-gadget-specific-d.patch \
    file://xusb.bin \
"

SRC_URI_append_apalis-tk1-mainline = " \
    file://0034-apalis-t30-tk1-mainline-tezi-specific-kernel-configu.patch \
    file://0035-apalis-tk1-mainline-usb-device-aka-gadget-specific-d.patch \
    file://xusb.bin \
"

do_configure_prepend_apalis-t30-mainline () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
do_configure_prepend_apalis-tk1-mainline () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
