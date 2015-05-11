require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx boards"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

LOCALVERSION = "-v2.4b1+git"
SRCBRANCH = "toradex_vf_4.0-next"
SRCREV = "fd2c7bbe3b6a93ee59b29df2b345cedb4fea0b6b"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(colibri-vf)"
