FILESEXTRAPATHS_prepend := "${THISDIR}/linux-toradex-4.14.x:"

SRC_URI_append = " file://0001-drm-imx-hdp-reduce-the-minimum-permitted-clock-frequency.patch"

KERNEL_ALT_IMAGETYPE = "Image.gz"
