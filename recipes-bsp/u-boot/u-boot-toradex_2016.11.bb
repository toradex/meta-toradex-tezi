include recipes-bsp/u-boot/u-boot-toradex.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "v2016.11-v0.5+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "785bcf4bc1b1d60b7d7558749fd4bb2eba9bcbc3"
SRCBRANCH = "2016.11-toradex"
COMPATIBLE_MACHINE = "(mx6|mx7|vf)"

SRC_URI_append = "\
    file://0001-apalis_imx6-use-SDP-if-recovery-mode-is-enabled.patch \
    file://0002-apalis_imx6-add-configuration-for-Tezi.patch \
"
