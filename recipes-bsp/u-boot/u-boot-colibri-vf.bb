require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb \
                    file://README;beginline=1;endline=22;md5=78b195c11cb6ef63e6985140db7d7bab"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
SRCREV_colibri-vf50 = "ac13ca97df9f8c17d1c89d425041690484ee0fdf"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "colibri-vf50"
DEFAULT_PREFERENCE_colibri-vf50 = "1"

# colibri_vf50: build additionally a u-boot binary used for nand boot
#SPL_BINARY_colibri-vf50  = "u-boot.nand"
#SPL_IMAGE_colibri-vf50   = "u-boot-${MACHINE}-${PV}-${PR}.nand"
#SPL_SYMLINK_colibri-vf50 = "u-boot-${MACHINE}.nand"
#do_compile_append_colibri-vf50() {
#    # keep boot-hsmmc
#    mv u-boot.${UBOOT_SUFFIX} u-boot-mmc.${UBOOT_SUFFIX}
#    oe_runmake colibri_vf50_nand_config
#    oe_runmake ${UBOOT_MAKE_TARGET}
#    mv u-boot.${UBOOT_SUFFIX} u-boot-nand.${UBOOT_SUFFIX}
#    mv u-boot-mmc.${UBOOT_SUFFIX} u-boot.${UBOOT_SUFFIX}
#
#    # prepare the u-boot for nand
#    dd if=/dev/zero of=u-boot-pad bs=1024 count=1
#    cat u-boot-pad u-boot-nand.${UBOOT_SUFFIX} > ${SPL_BINARY}
#}

# colibri_vf50: build additionally a u-boot binary used for sd-card boot
SPL_BINARY_colibri-vf50  = "u-boot.imx"
SPL_IMAGE_colibri-vf50   = "u-boot-${MACHINE}-${PV}-${PR}.imx"
SPL_SYMLINK_colibri-vf50 = "u-boot-${MACHINE}.imx"
do_compile_append_colibri-vf50() {
    oe_runmake colibri_vf50_sdboot_config
    oe_runmake 
}
