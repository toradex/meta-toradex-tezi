FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:"

require linux-toradex-tezi-common.inc

KBUILD_DEFCONFIG:forcevariable = ""
SRC_URI:append = " \
    file://defconfig \
    file://0001-arm-dts-imx6qdl-apalis-force-fixed-ids-for-usdhc-con.patch \
    file://0002-arm-dts-imx6qdl-colibri-force-fixed-ids-for-usdhc-co.patch \
    file://0003-arm-dts-imx7d-colibri-emmc-force-fixed-ids-for-usdhc.patch \
    file://0004-arm-dts-imx7d-colibri-emmc-Enable-Display-interfaces.patch \
    file://0005-arm-dts-imx6ull-colibri-emmc-Enable-Display-interfac.patch \
    file://0006-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0007-ARM-dts-imx6ull-colibri-use-static-MTD-partition-lay.patch \
    file://0001-MLK-11747-mtd-nand-raw-gpmi-nand-add-the-debugfs-fil.patch \
"
