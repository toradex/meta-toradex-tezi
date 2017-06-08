#FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex_4.1-2.0.x:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://0001-ARM-dts-imx6qdl-apalis-configure-LVDS-VGA-and-dual-m.patch \
    file://0002-ARM-dts-imx6qdl-apalis-configure-STMPE811-in-analog-.patch \
"

SRCREV = "50e26af30f04d24f4783dae15206c5dba889fd2b"
SRCBRANCH = "toradex_4.1-2.0.x-imx"
