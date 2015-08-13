require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx Computer on Modules"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

LOCALVERSION = "-v2.5b1"
SRCBRANCH = "toradex_vf_4.1"
SRCREV = "4d054fbf42d98a54bdb330eebfe419af8a825118"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(vf)"
