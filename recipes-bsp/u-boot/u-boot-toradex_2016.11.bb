include recipes-bsp/u-boot/u-boot-toradex.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "2016.11"

PR = "${TDX_VER_INT}-gitr${@d.getVar("SRCREV", False)[0:7]}"
LOCALVERSION ?= "-${TDX_VER_INT}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "0e9e31afba77e478250bedaf0e6a9a2a47aa04f1"
SRCBRANCH = "2016.11-toradex"
COMPATIBLE_MACHINE = "(mx6|mx7|vf)"

SRC_URI_append = "\
    file://0001-apalis_imx6-use-SDP-if-recovery-mode-is-enabled.patch \
    file://0002-apalis_imx6-add-configuration-for-Tezi.patch \
    file://0003-colibri_imx6-use-SDP-if-recovery-mode-is-enabled.patch \
    file://0004-colibri_imx6-add-configuration-for-Tezi.patch \
    file://0001-colibri_imx7-use-SDP-if-recovery-mode-is-enabled.patch \
    file://0002-colibri_imx7-add-configuration-for-Tezi.patch \
"
