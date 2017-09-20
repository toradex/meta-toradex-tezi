FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_append_colibri-imx7 = " \
    file://0001-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
"

SRCREV = "08e1330fb0b0a6a93ceb5d27cbb49b9989e0f787"
SRCBRANCH = "toradex_4.1-2.0.x-imx-next"
