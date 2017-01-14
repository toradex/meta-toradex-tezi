include recipes-bsp/u-boot/u-boot-toradex.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "v2016.11-v2.7b1+git${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "f1d68687537bceed9211e84e982965bce3be421e"
SRCBRANCH = "2016.11-toradex-next"
COMPATIBLE_MACHINE = "(mx6|mx7|vf)"

SRC_URI_append = "\
    file://0001-apalis_imx6-use-SDP-if-recovery-mode-is-enabled.patch \
    file://0002-apalis_imx6-add-configuration-for-Tezi.patch \
"
