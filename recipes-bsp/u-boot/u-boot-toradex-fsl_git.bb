require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=c7383a594871c03da76b3707929d2919"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"

SRCREV_mx6 = "b66337d357cca761bf8627acbb1ec991f425f0b4"
SRCBRANCH_mx6 = "2015.04-toradex"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=${SRCBRANCH}"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(colibri-imx6|apalis-imx6)"
DEFAULT_PREFERENCE_apalis-imx6 = "1"
DEFAULT_PREFERENCE_colibri-imx6 = "1"

# apalis-imx6: build additionally a u-boot binary for the IT variant
SPL_BINARY_apalis-imx6  = "u-boot.imx-it"
SPL_IMAGE_apalis-imx6   = "u-boot-${MACHINE}-${PV}-${PR}.imx-it"
SPL_SYMLINK_apalis-imx6 = "u-boot-${MACHINE}.imx-it"
do_compile_append_apalis-imx6() {
    # keep u-boot with standard timings
    mv u-boot.imx u-boot-std.imx
    oe_runmake apalis_imx6_it_defconfig
    oe_runmake ${UBOOT_MAKE_TARGET}
    mv u-boot.imx ${SPL_BINARY}
    mv u-boot-std.imx u-boot.imx
}
