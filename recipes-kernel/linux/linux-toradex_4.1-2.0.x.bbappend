FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_append_colibri-imx7 = " \
    file://0001-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
"

SRCREV = "ec71d25521003dce3f0d93c716418a268c9ff89d"
SRCBRANCH = "toradex_4.1-2.0.x-imx-next"
