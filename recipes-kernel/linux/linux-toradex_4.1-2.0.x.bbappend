FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-4.1-2.0.x/${MACHINE}:${THISDIR}/${PN}-4.1-2.0.x:"

SRC_URI_append_apalis-imx6 = " \
    file://0002-ARM-dts-imx6qdl-apalis-eval-disable-PWM-1-2-3.patch \
"

SRC_URI_append_colibri-imx6 = " \
    file://0001-ARM-dts-imx6dl-colibri-eval-v3-disable-PWM-B-C-D.patch \
"

SRC_URI_append_colibri-imx7 = " \
    file://0001-ARM-dts-imx7-colibri-use-static-MTD-partition-layout.patch \
    file://0003-ARM-dts-imx7-colibri-eval-v3-disable-PWM-B-C-D.patch \
"

SRCREV = "f3637519a6aad71901edd43d8e85d13708a7a76e"
SRCBRANCH = "toradex_4.1-2.0.x-imx"
