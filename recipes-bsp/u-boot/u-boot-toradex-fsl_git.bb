require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

SRCREV_colibri-vf = "36b44c6fc7ddde29aec7f39ebcb5cc959d24b469"
SRCBRANCH_colibri-vf = "2014.04-toradex"
SRCREV_mx6 = "36b44c6fc7ddde29aec7f39ebcb5cc959d24b469"
SRCBRANCH_mx6 = "2014.04-toradex"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=${SRCBRANCH}"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(colibri-vf|colibri-imx6|apalis-imx6)"
DEFAULT_PREFERENCE_colibri-vf = "1"
DEFAULT_PREFERENCE_apalis-imx6 = "1"
DEFAULT_PREFERENCE_colibri-imx6 = "1"

# colibri_vf: copy additional U-Boot binary for NAND
UBOOT_BINARY_NAND_colibri-vf = "u-boot-nand.imx"
UBOOT_IMAGE_NAND_colibri-vf = "u-boot-nand-${MACHINE}-${PV}-${PR}.imx"
UBOOT_SYMLINK_NAND_colibri-vf = "u-boot-nand-${MACHINE}.imx"

do_deploy_append_colibri-vf() {
    install ${S}/${UBOOT_BINARY_NAND} ${DEPLOYDIR}/${UBOOT_IMAGE_NAND}

    cd ${DEPLOYDIR}
    rm -f ${UBOOT_BINARY_NAND} ${UBOOT_SYMLINK_NAND}
    ln -sf ${UBOOT_IMAGE_NAND} ${UBOOT_SYMLINK_NAND}
    ln -sf ${UBOOT_IMAGE_NAND} ${UBOOT_BINARY_NAND}
}
