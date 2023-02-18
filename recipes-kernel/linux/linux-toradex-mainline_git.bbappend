FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

require linux-toradex-tezi-common.inc

KBUILD_DEFCONFIG:forcevariable = ""
SRC_URI:append = " \
    file://defconfig \
    file://0003-mmc-read-mmc-alias-from-device-tree.patch \
    file://0004-arm-dts-imx6qdl-apalis-force-fixed-ids-for-usdhc-con.patch \
    file://0005-arm-dts-imx6qdl-colibri-force-fixed-ids-for-usdhc-co.patch \
    file://0006-arm-dts-imx7d-colibri-emmc-force-fixed-ids-for-usdhc.patch \
    file://0011-arm64-dts-imx7d-colibri-emmc-Enable-Display-interfac.patch \
    file://0012-arm-dts-imx6ull-colibri-emmc-Enable-Display-interfac.patch \
    file://0013-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0001-MLK-11747-mtd-nand-raw-gpmi-nand-add-the-debugfs-fil.patch \
    file://0001-ARM-dts-imx6ull-colibri-use-static-MTD-partition-lay.patch \
"
