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
SRC_URI_COLIBRI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
SRCREV_COLIBRI = "658a5957747cd3f2d1d90f4983fec28a116851cd"

PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"
PV_apalis-t30 = "${PR}+gitr${SRCREV}"

SRC_URI_colibri-t20 = "${SRC_URI_COLIBRI}"
SRC_URI_colibri-t30 = "${SRC_URI_COLIBRI}"
SRC_URI_apalis-t30 = "${SRC_URI_COLIBRI}"
SRCREV_colibri-t20 = "${SRCREV_COLIBRI}"
SRCREV_colibri-t30 = "${SRCREV_COLIBRI}"
SRCREV_apalis-t30 = "${SRCREV_COLIBRI}"
PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"
PV_apalis-t30 = "${PR}+gitr${SRCREV}"

# override the solution passed in from u-boot.inc as we want to set additional flags
EXTRA_OEMAKE_colibri-t20 = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE_colibri-t30 = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE_apalis-t30 = "CROSS_COMPILE=${TARGET_PREFIX}"

#build additionally a u-boot binary which uses/stores its environment on an T20 external sd or mmc card
SPL_BINARY_colibri-t20  = "u-boot-hsmmc.bin"
SPL_IMAGE_colibri-t20   = "u-boot-hsmmc-${MACHINE}-${PV}-${PR}.${UBOOT_SUFFIX}"
SPL_SYMLINK_colibri-t20 = "u-boot-hsmmc-${MACHINE}.${UBOOT_SUFFIX}"
do_compile_append_colibri-t20() {
	mv u-boot.bin u-boot-nand.bin
	oe_runmake colibri_t20_sdboot_config
	oe_runmake all
	mv u-boot.bin u-boot-hsmmc.bin
	mv u-boot-nand.bin u-boot.bin
}
