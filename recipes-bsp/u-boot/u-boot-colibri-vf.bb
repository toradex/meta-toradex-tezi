require recipes-bsp/u-boot/u-boot.inc

PROVIDES += "u-boot"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb \
                    file://README;beginline=1;endline=22;md5=78b195c11cb6ef63e6985140db7d7bab"

PV = "${PR}+gitr${SRCREV}"
PR = "r0"

S = "${WORKDIR}/git"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
SRCREV_colibri-vf = "5017686683f7c2eb12bb966924c857622e9cdb94"

#FILESPATHPKG =. "git:"
PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "colibri-vf"
DEFAULT_PREFERENCE_colibri-vf = "1"

# colibri_vf: build additionally a u-boot binary used for sd-card boot
SPL_BINARY_colibri-vf  = "u-boot.imx"
SPL_IMAGE_colibri-vf   = "u-boot-${MACHINE}-${PV}-${PR}.imx"
SPL_SYMLINK_colibri-vf = "u-boot-${MACHINE}.imx"
do_compile_append_colibri-vf() {
    oe_runmake colibri_vf_sdboot_config
    oe_runmake 
}
