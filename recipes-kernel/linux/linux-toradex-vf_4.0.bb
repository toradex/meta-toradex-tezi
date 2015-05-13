require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx boards"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

LOCALVERSION = "-v2.4b1+git"
SRCBRANCH = "toradex_vf_4.0"
SRCREV = "3dc77dbec09e0723a2b7b003f842168cefaf2a0a"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(colibri-vf)"
