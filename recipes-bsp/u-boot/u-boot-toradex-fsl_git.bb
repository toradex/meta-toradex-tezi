require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"
LIC_FILES_CHKSUM_colibri-vf = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

SRCREV_colibri-vf = "93d011ae1319c297d3cf31d40a88b21699d89d7b"
SRCBRANCH_colibri-vf = "2015.04-toradex-next"
SRCREV_mx6 = "0260e62f008aa292d87da7c1a9fbe1051a793518"
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

# apalis-imx6: build additionally a u-boot binary for the IT variant
SPL_BINARY_apalis-imx6  = "u-boot-it.imx"
SPL_IMAGE_apalis-imx6   = "u-boot-it-${MACHINE}-${PV}-${PR}.imx"
SPL_SYMLINK_apalis-imx6 = "u-boot-it-${MACHINE}.imx"
do_compile_append_apalis-imx6() {
    # keep u-boot with standard timings
    mv u-boot.imx u-boot-std.imx
    oe_runmake apalis_imx6q2g_config
    oe_runmake ${UBOOT_MAKE_TARGET}
    mv u-boot.imx u-boot-it.imx
    mv u-boot-std.imx u-boot.imx
}
