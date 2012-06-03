# Extends the core U-Boot GIT recipe 
# to take the u-boot sources including the colibri stuff from our git repository
PR ="r1"
DEPENDS += "dtc-native"
 
FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-git:"

# Also overwries the license checksum to suit the updated text file in our U-Boot snapshot.
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

COMPATIBLE_MACHINE_colibri-t20 = "colibri-t20"
COMPATIBLE_MACHINE_colibri-t30 = "colibri-t30"

DEFAULT_PREFERENCE_colibri-t20 = "1"
DEFAULT_PREFERENCE_colibri-t30 = "1"

UBOOT_MACHINE_colibri-t20 = "colibri_t20_config"
UBOOT_MACHINE_colibri-t30 = "colibri_t30_config"

#gitorious git
FILESPATHPKG =. "git:"
S="${WORKDIR}/git"
SRC_URI_COLIBRI = "git://gitorious.org/colibri-t20-embedded-linux-bsp/colibri_t20-u-boot.git;protocol=git;branch=master \
	file://u-boot-warning.patch \
	file://u-boot-board-unused.patch \
"

#	file://remove-unused.patch \
#	file://u-boot_ap20warning.patch \
#	file://colibri_t30.patch \
#	file://bootaddr.patch \
#"
SRCREV_colibri-t20 = "63c37d9e1d3ea97391576384d237728c44b5e33b"
SRCREV_colibri-t30 = "63c37d9e1d3ea97391576384d237728c44b5e33b"

PV_colibri-t20 = "${PR}+gitr${SRCREV}"
PV_colibri-t30 = "${PR}+gitr${SRCREV}"

#internal SVN
#S = "${WORKDIR}/bootloader/u-boot"
#SVN_REV = 218
#SRC_URI_COLIBRI = "svn://tegradev:tegra123!@mammut.toradex.int:8090/colibri_tegra_linux/trunk;module=bootloader/u-boot;rev=${SVN_REV};proto=http \
#	file://remove-unused.patch "
	
SRC_URI_colibri-t20 = "${SRC_URI_COLIBRI} "
SRC_URI_colibri-t30 = "${SRC_URI_COLIBRI} "

#compile with -O2 not -Os as with gcc 4.5 the code does not work 
do_configure_append() {
	# sed -i -e 's/-Os/-O2 -fno-ipa-sra -fno-caller-saves -fno-schedule-insns/' ${S}/config.mk
	sed -i -e 's/-Os/-O2 -fno-ipa-sra -fno-caller-saves -fno-schedule-insns -mno-unaligned-access/' ${S}/config.mk
}


#build additionally a u-boot binary which uses/stores its environment on an T20 external sd or mmc card
do_compile_append_colibri-t20() {
	mv u-boot.bin u-boot-nand.bin
	oe_runmake colibri_t20_sdboot_config
	oe_runmake all
	mv u-boot.bin u-boot-hsmmc.bin
	mv u-boot-nand.bin u-boot.bin
}

UBOOT_IMAGE ?= "u-boot-hsmmc-${MACHINE}-${PV}-${PR}.bin"
do_deploy_append_colibri-t20 () {
	install ${S}/u-boot-hsmmc.bin ${DEPLOY_DIR_IMAGE}/u-boot-hsmmc-${MACHINE}-${PV}-${PR}.bin
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/u-boot-hsmmc-${MACHINE}-${PV}-${PR}.bin
	ln -sf u-boot-hsmmc-${MACHINE}-${PV}-${PR}.bin ${DEPLOY_DIR_IMAGE}/u-boot-hsmmc.bin
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/u-boot-hsmmc.bin
}
