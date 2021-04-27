FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

require linux-toradex-tezi-common.inc

SRC_URI_append = " \
    file://0001-Revert-ARM-dts-imx6qdl-apalis-disable-LVDS-parallel-.patch \
    file://0002-Partially-revert-ARM-dts-imx6qdl-apalis-link-the-bac.patch \
    file://0003-mmc-read-mmc-alias-from-device-tree.patch \
    file://0004-arm-dts-imx6qdl-apalis-force-fixed-ids-for-usdhc-con.patch \
    file://0005-arm-dts-imx6qdl-colibri-force-fixed-ids-for-usdhc-co.patch \
    file://0006-arm-dts-imx7d-colibri-emmc-force-fixed-ids-for-usdhc.patch \
    file://0009-ARM-dts-imx6qdl-apalis-eval-disable-PWM-1-2-3.patch \
    file://0010-ARM-dts-imx6dl-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0011-ARM-dts-imx6ull-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0012-ARM-dts-imx7-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0013-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0014-apalis-t30-mainline-force-fixed-ids-for-sdmmc-contro.patch \
    file://0015-apalis-tk1-force-fixed-ids-for-sdmmc-controllers.patch \
    file://0016-apalis-tk1-mainline-usb-device-aka-gadget-specific-d.patch \
    file://0017-apalis-t30-mainline-usb-device-aka-gadget-specific-d.patch \
"

SRC_URI_append_apalis-t30-mainline = " \
    file://0001-apalis-t30-mainline-apply-pcie-fix-hacks.patch \
    file://xusb.bin \
"

SRC_URI_append_apalis-tk1 = " \
    file://xusb.bin \
"

do_configure_prepend_apalis-t30-mainline () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
do_configure_prepend_apalis-tk1 () {
    mkdir -p ${S}/firmware/nvidia/tegra124
    cp ${WORKDIR}/xusb.bin ${S}/firmware/nvidia/tegra124/
}
