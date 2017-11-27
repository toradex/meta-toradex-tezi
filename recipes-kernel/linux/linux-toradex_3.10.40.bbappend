FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex_3.10.40:"

SRC_URI_append_apalis-tk1 = " \
    file://0001-apalis-tk1-tezi-specific-kernel-configuration.patch \
    file://tegra_xusb_firmware \
"

SRCREV = "5ed678c0982f3b611c09a4830e81672fa87372b8"
SRCBRANCH = "toradex_tk1_l4t_r21.5"

do_configure_prepend () {
    cp ${WORKDIR}/tegra_xusb_firmware ${S}/firmware
}
