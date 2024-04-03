FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/files:"

require linux-toradex-tezi-common.inc

SRC_URI:append = " \
    file://0001-MLK-11747-mtd-nand-raw-gpmi-nand-add-the-debugfs-fil.patch \
    file://0002-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0003-ARM-dts-imx6ull-colibri-use-static-MTD-partition-lay.patch \
    file://tezi.cfg \
"
