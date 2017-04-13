#FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex_4.1-2.0.x:"
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += " \
    file://0001-Revert-video-mxc-ldb-Add-support-for-LVDS-configurat.patch \
    file://0002-ARM-dts-imx6qdl-apalis-configure-LVDS-VGA-and-dual-m.patch \
    file://0001-iio-adc-support-IIO_CHAN_INFO_SCALE.patch \
"
