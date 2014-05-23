require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=025bf9f768cbcb1a165dbe1a110babfb"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

SRCREV_colibri-vf = "0242d1cf0d907c6f61351bb6cc799dd5bafe4932"
SRCREV_apalis-imx6 = "77fee01c7c84e888883be17bba0f36c6bbc593e0"
SRCBRANCH = "2014.04-toradex"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=${SRCBRANCH}"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(colibri-vf|apalis-imx6)"
DEFAULT_PREFERENCE_colibri-vf = "1"
DEFAULT_PREFERENCE_apalis-imx6 = "1"

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
