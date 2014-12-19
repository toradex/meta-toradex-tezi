require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx boards"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

LOCALVERSION = "-v2.3b5"
SRCBRANCH = "toradex_vf_3.18"
SRCREV = "2d3459a0b77892017238715a7ad2471c375cd656"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(colibri-vf)"
