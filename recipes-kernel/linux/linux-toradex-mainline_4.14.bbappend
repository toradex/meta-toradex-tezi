FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex-mainline_4.14:"

SRC_URI_append_apalis-tk1-mainline = " \
    file://0012-apalis-tk1-mainline-tezi-specific-kernel-configurati.patch \
    file://0013-apalis-tk1-mainline-usb-device-aka-gadget-specific-d.patch \
    file://0014-usb-chipidea-tegra-Use-aligned-DMA-on-Tegra30-114-12.patch \
    file://xusb.bin \
"

do_configure_prepend () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
