# Extends the core u-boot recipe 
# to take the u-boot sources including the colibri stuff from our git repository
PR ="r3"
DEPENDS += "dtc-native"
 
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

COMPATIBLE_MACHINE_colibri-t20 = "colibri-t20"
COMPATIBLE_MACHINE_colibri-t30 = "colibri-t30"

DEFAULT_PREFERENCE_colibri-t20 = "1"
DEFAULT_PREFERENCE_colibri-t30 = "1"

UBOOT_MACHINE_colibri-t20 = "colibri_t20_config"
UBOOT_MACHINE_colibri-t30 = "colibri_t30_config"

#  gitorious git  ###################################################################################################
#FILESPATHPKG =. "git:"
#S="${WORKDIR}/git"
#SRC_URI_COLIBRI = "git://gitorious.org/colibri-t20-embedded-linux-bsp/colibri_t20-u-boot.git;protocol=git;branch=master \
#	file://u-boot-warning.patch \
#	file://u-boot-board-unused.patch \
#	file://board_stackcorruption_workaround.patch \
#"
#SRCREV_COLIBRI = "63c37d9e1d3ea97391576384d237728c44b5e33b"
#####################################################################################################################

#  toradex git  ###################################################################################################
FILESPATHPKG =. "git:"
S="${WORKDIR}/git"
SRC_URI_COLIBRI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
SRCREV_COLIBRI = "16a9368a88a28252ea1daedc1df03ccc49ea5b22"
#####################################################################################################################

PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"

SRC_URI_colibri-t20 = "${SRC_URI_COLIBRI}"
SRC_URI_colibri-t30 = "${SRC_URI_COLIBRI}"
SRCREV_colibri-t20 = "${SRCREV_COLIBRI}"
SRCREV_colibri-t30 = "${SRCREV_COLIBRI}"
PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"


#compile with -O2 not -Os as with gcc 4.5 the code does not work 
# override the solution passed in from u-boot.inc as we want to set additional flags
EXTRA_OEMAKE_colibri-t20 = "CROSS_COMPILE=${TARGET_PREFIX}"
EXTRA_OEMAKE_colibri-t30 = "CROSS_COMPILE=${TARGET_PREFIX}"
do_configure_append() {
	# sed -i -e 's/-Os/-O2 -fno-ipa-sra -fno-caller-saves -fno-schedule-insns -mno-unaligned-access/' ${S}/config.mk
	sed -i -e 's/-Os/-O2/' ${S}/config.mk
}

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
