FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

require linux-toradex-tezi-common.inc

KBUILD_DEFCONFIG_forcevariable = ""
SRC_URI_append = " \
    file://defconfig \
    file://0003-mmc-read-mmc-alias-from-device-tree.patch \
    file://0004-arm-dts-imx6qdl-apalis-force-fixed-ids-for-usdhc-con.patch \
    file://0005-arm-dts-imx6qdl-colibri-force-fixed-ids-for-usdhc-co.patch \
    file://0006-arm-dts-imx7d-colibri-emmc-force-fixed-ids-for-usdhc.patch \
    file://0011-arm64-dts-imx7d-colibri-emmc-Enable-Display-interfac.patch \
    file://0013-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0015-apalis-tk1-force-fixed-ids-for-sdmmc-controllers.patch \
    file://0016-apalis-tk1-mainline-usb-device-aka-gadget-specific-d.patch \
"

SRC_URI_append_apalis-tk1 = " \
    file://xusb.bin \
"

SRCREV_machine = "297c50e5de787c08f6627883000728af6cbec478"

do_configure_prepend_apalis-tk1 () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
