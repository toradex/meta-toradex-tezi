# Extends the core u-boot recipe 
# to take the u-boot sources including the colibri stuff from our git repository
PR ="r7"
DEPENDS += "dtc-native"
 
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

COMPATIBLE_MACHINE_colibri-t20 = "colibri-t20"
COMPATIBLE_MACHINE_colibri-t30 = "colibri-t30"
COMPATIBLE_MACHINE_apalis-t30 = "apalis-t30"

DEFAULT_PREFERENCE_colibri-t20 = "1"
DEFAULT_PREFERENCE_colibri-t30 = "1"
DEFAULT_PREFERENCE_apalis-t30 = "1"

FILESPATHPKG =. "git:"
S="${WORKDIR}/git"
SRC_URI_COLIBRI =  "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
#SRC_URI_COLIBRI += "file://u-boot-dont-build-standalone.patch"
SRCREV_COLIBRI = "e40f10a4d146c8eab11d89fd01d2e098b2e30031"

PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"
PV_apalis-t30 = "${PR}+gitr${SRCREV}"

SRC_URI_colibri-t20 = "${SRC_URI_COLIBRI}"
SRC_URI_colibri-t30 = "${SRC_URI_COLIBRI}"
SRC_URI_apalis-t30 = "${SRC_URI_COLIBRI}"

SRCREV_colibri-t20 = "${SRCREV_COLIBRI}"
SRCREV_colibri-t30 = "${SRCREV_COLIBRI}"
SRCREV_apalis-t30 = "${SRCREV_COLIBRI}"

# override the solution passed in from u-boot.inc as we want to set additional flags
EXTRA_OEMAKE_colibri-t20 = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE_colibri-t30 = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE_apalis-t30 = "CROSS_COMPILE=${TARGET_PREFIX}"

# colibri-t20: build additionally a u-boot binary which uses/stores its environment on an T20 external sd or mmc card
SPL_BINARY_colibri-t20  = "u-boot-hsmmc.bin"
SPL_IMAGE_colibri-t20   = "u-boot-hsmmc-${MACHINE}-${PV}-${PR}.bin"
SPL_SYMLINK_colibri-t20 = "u-boot-hsmmc-${MACHINE}.bin"
do_compile_append_colibri-t20() {
    # keep u-boot-nand
    mv u-boot.bin u-boot-nand.bin
    oe_runmake colibri_t20_sdboot_config
    oe_runmake ${UBOOT_MAKE_TARGET}
    mv u-boot.bin u-boot-hsmmc.bin
    mv u-boot-nand.bin u-boot.bin
}

#do_install_append() {
#}
