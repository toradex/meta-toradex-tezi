require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

SRCREV_mx6 = "95868835ab8181591446bc3f24a15bf6fbdfd1f5"
SRCBRANCH_mx6 = "2015.04-toradex-next"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=${SRCBRANCH}"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(colibri-imx6|apalis-imx6)"
DEFAULT_PREFERENCE_apalis-imx6 = "1"
DEFAULT_PREFERENCE_colibri-imx6 = "1"

# apalis-imx6: build additionally a u-boot binary for the IT variant
SPL_BINARY_apalis-imx6  = "u-boot-it.imx"
SPL_IMAGE_apalis-imx6   = "u-boot-it-${MACHINE}-${PV}-${PR}.imx"
SPL_SYMLINK_apalis-imx6 = "u-boot-it-${MACHINE}.imx"
do_compile_append_apalis-imx6() {
    # keep u-boot with standard timings
    mv u-boot.imx u-boot-std.imx
    oe_runmake apalis_imx6_it_defconfig
    oe_runmake ${UBOOT_MAKE_TARGET}
    mv u-boot.imx u-boot-it.imx
    mv u-boot-std.imx u-boot.imx
}
