FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex-3.10.40:"

SRC_URI_append_apalis-tk1 = " \
    file://0001-apalis-tk1-tezi-specific-kernel-configuration.patch \
    file://tegra_xusb_firmware \
"

SRCREV = "877a32308600b065f376f8cf41e1bf9093aff64f"
SRCBRANCH = "toradex_tk1_l4t_r21.6"

do_configure_prepend () {
    cp ${WORKDIR}/tegra_xusb_firmware ${S}/firmware
}
