include u-boot-toradex-tezi.inc

# xxd tool is used in U-boot Makefile when
# CONFIG_USE_DEFAULT_ENV_FILE U-boot option enabled.
DEPENDS += "xxd-native"

# Dumbing the nand_padding() as it doesn't support multi-target
# u-boot configurations. Instead, TEZI builds the u-boot-nand.imx
# target directly.
# TODO: Temporary workaround. This code should be removed if either
# nand_padding() will not exist in meta-toradex-nxp anymore or it
# will support the multi-target u-boot configs.
nand_padding () {
    return
}
