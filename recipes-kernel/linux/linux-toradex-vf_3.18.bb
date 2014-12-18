require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx boards"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

LOCALVERSION = "-v2.3b5"
SRCBRANCH = "toradex_vf_3.18"
SRCREV = "068ecdab500e551f313cd6b08dccc32c9d55e137"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(colibri-vf)"
