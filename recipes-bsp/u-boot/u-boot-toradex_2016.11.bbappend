DEPENDS_append_apalis-t30-mainline = " cbootimage-native"
DEPENDS_append_apalis-tk1 = " cbootimage-native"
DEPENDS_append_apalis-tk1-mainline = " cbootimage-native"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
PV = "2016.11"
PR = "${TDX_VER_ITEM}+gitr${SRCPV}"

LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

SRCREV = "07edca0bb81857a339f26f3465d5c5602705a94d"
SRCBRANCH = "2016.11-toradex"
COMPATIBLE_MACHINE = "(mx6|mx7|tegra3|tegra124|vf)"

SRC_URI_append_apalis-t30-mainline = " \
    file://apalis_t30.img.cfg \
    file://Apalis_T30_2GB_800Mhz.bct \
"
SRC_URI_append_apalis-tk1 = " \
    file://apalis-tk1.img.cfg \
    file://PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct \
"
SRC_URI_append_apalis-tk1-mainline = " \
    file://apalis-tk1.img.cfg \
    file://PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct \
"

do_deploy_append_apalis-t30-mainline() {
    cd ${DEPLOYDIR}
    cp ${WORKDIR}/Apalis_T30_2GB_800Mhz.bct .
    cbootimage -s tegra30 ${WORKDIR}/apalis_t30.img.cfg apalis_t30.img
    rm Apalis_T30_2GB_800Mhz.bct
}
do_deploy_append_apalis-tk1() {
    cd ${DEPLOYDIR}
    cp ${WORKDIR}/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct .
    cbootimage -s tegra124 ${WORKDIR}/apalis-tk1.img.cfg apalis-tk1.img
    rm PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct
}
do_deploy_append_apalis-tk1-mainline() {
    cd ${DEPLOYDIR}
    cp ${WORKDIR}/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct .
    cbootimage -s tegra124 ${WORKDIR}/apalis-tk1.img.cfg apalis-tk1.img
    rm PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct
}
