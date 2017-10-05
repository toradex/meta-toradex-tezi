FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_append_colibri-imx7 = " \
    file://0001-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
"

SRCREV = "b1555bfbf38818bc6fed8d921b55b7b207249c53"
SRCBRANCH = "toradex_4.1-2.0.x-imx"
