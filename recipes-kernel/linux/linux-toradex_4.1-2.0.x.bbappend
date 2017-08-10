#FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex_4.1-2.0.x:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://0001-ARM-dts-imx6qdl-apalis-configure-LVDS-VGA-and-dual-m.patch \
    file://0002-ARM-dts-imx6qdl-apalis-configure-STMPE811-in-analog-.patch \
"

SRCREV = "82f0f4f012a646a735d6b44de77b7c9d0712c714"
SRCBRANCH = "toradex_4.1-2.0.x-imx"
