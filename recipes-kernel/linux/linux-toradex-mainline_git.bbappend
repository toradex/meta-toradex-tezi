FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-git:${THISDIR}/files:"

require linux-toradex-tezi-common.inc

SRC_URI:append = " \
    file://0001-ARM-dts-imx6qdl-apalis-Add-usdhc-aliases.patch \
    file://0002-ARM-dts-imx6qdl-colibri-Add-usdhc-aliases.patch \
    file://0003-ARM-dts-imx7d-colibri-emmc-Add-usdhc-aliases.patch \
    file://0006-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0007-ARM-dts-imx6ull-colibri-use-static-MTD-partition-lay.patch \
    file://0001-MLK-11747-mtd-nand-raw-gpmi-nand-add-the-debugfs-fil.patch \
    file://tezi.cfg \
"
