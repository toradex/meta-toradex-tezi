FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

include u-boot-toradex-tezi.inc
inherit toradex-kernel-config

SRC_URI += "file://toradex-recovery-u-boot.dtsi"

# xxd tool is used in U-boot Makefile when
# CONFIG_USE_DEFAULT_ENV_FILE U-boot option enabled.
DEPENDS += "xxd-native"

do_configure:prepend () {
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig

    kconfig_configure_variable ENV_IS_IN_MMC n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IS_NOWHERE y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable TDX_CFG_BLOCK_USB_GADGET_PID n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IMPORT_FDT y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable DEVICE_TREE_INCLUDES \"toradex-recovery-u-boot.dtsi\" ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
}

do_compile:prepend () {
    cp ${WORKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
}

# Dumbing the nand_padding() as it doesn't support multi-target
# u-boot configurations. Instead, TEZI builds the u-boot-nand.imx
# target directly.
# TODO: Temporary workaround. This code should be removed if either
# nand_padding() will not exist in meta-toradex-nxp anymore or it
# will support the multi-target u-boot configs.
nand_padding () {
    return
}
