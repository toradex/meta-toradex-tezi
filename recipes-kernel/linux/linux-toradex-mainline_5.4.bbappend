FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-5.4:"

SRC_URI_append = " \
    file://0001-Revert-ARM-dts-imx6qdl-apalis-disable-LVDS-parallel-.patch \
    file://0002-Partially-revert-ARM-dts-imx6qdl-apalis-link-the-bac.patch \
    file://0003-mmc-read-mmc-alias-from-device-tree.patch \
    file://0004-arm-dts-imx6qdl-apalis-force-fixed-ids-for-usdhc-con.patch \
    file://0005-arm-dts-imx6qdl-colibri-force-fixed-ids-for-usdhc-co.patch \
    file://0006-arm-dts-imx7d-colibri-emmc-force-fixed-ids-for-usdhc.patch \
    file://0007-arm-dts-imx6dl-q-ull-7-apalis-colibri-eval-eval-v3-v.patch \
    file://0008-arm-dts-imx6ull-colibri-usb-device-aka-gadget-specif.patch \
    file://0009-arm-dts-imx7-colibri-usb-device-aka-gadget-specific-.patch \
    file://0010-ARM-dts-imx6qdl-apalis-eval-disable-PWM-1-2-3.patch \
    file://0011-ARM-dts-imx6dl-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0012-ARM-dts-imx6ull-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0013-ARM-dts-imx7-colibri-eval-v3-disable-PWM-B-C-D.patch \
    file://0014-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
"
