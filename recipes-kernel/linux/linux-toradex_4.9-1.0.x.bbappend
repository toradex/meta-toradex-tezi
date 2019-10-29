FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-4.9-1.0.x/${MACHINE}:${THISDIR}/${PN}-4.9-1.0.x:"

require linux-configure-variable.inc

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

SRCREV = "def4502031f6127d41ef4a6fd80bf437623bec80"
SRCBRANCH = "toradex_4.9-1.0.x-imx"

do_configure_append () {
    kernel_configure_variable LOGO n
}
