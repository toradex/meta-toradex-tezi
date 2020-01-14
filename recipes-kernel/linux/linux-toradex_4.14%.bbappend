FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex-4.14.x:"

require linux-toradex-tezi-common.inc

SRC_URI_append = " file://0001-drm-imx-hdp-reduce-the-minimum-permitted-clock-frequency.patch"

do_configure_append () {
    kernel_configure_variable LOGO n
}
