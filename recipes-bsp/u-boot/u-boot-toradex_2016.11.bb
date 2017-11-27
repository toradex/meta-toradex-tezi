include recipes-bsp/u-boot/u-boot-toradex.inc

DEPENDS_append_apalis-tk1 = " cbootimage-native"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "2016.11"

PR = "${TDX_VER_INT}-gitr${@d.getVar("SRCREV", False)[0:7]}"
LOCALVERSION ?= "-${TDX_VER_INT}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "1b121c6ab548a9af0a27876e9eaa0c654c1dc3e1"
SRCBRANCH = "2016.11-toradex"
COMPATIBLE_MACHINE = "(mx6|mx7|tegra124|vf)"

SRC_URI_append = "\
    file://0001-apalis_imx6-add-configuration-for-Tezi.patch \
    file://0002-colibri_imx6-add-configuration-for-Tezi.patch \
    file://0003-mtest-disable-physical-memory-cell-test.patch \
    file://0001-colibri_imx7-add-configuration-for-Tezi.patch \
    file://0002-colibri_imx7_emmc-add-configuration-for-Tezi.patch \
    file://0003-colibri_imx7-add-configuration-for-Tezi-recovery.patch \
    file://0001-colibri-imx6ull-add-configuration-for-Tezi.patch \
    file://0001-apalis-tk1-add-configuration-for-tezi.patch \
    file://0002-apalis-tk1-integrate-tezi-recovery.patch \
"
SRC_URI_append_apalis-tk1 = " \
    file://apalis-tk1.img.cfg \
    file://PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct \
"

do_deploy_append_apalis-tk1() {
    cd ${DEPLOYDIR}
    cp ${WORKDIR}/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct .
    cbootimage -s tegra124 ${WORKDIR}/apalis-tk1.img.cfg apalis-tk1.img
    rm PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct
}
