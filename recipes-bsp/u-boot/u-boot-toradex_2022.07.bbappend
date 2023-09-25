FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

include u-boot-toradex-tezi.inc

SRC_URI += "file://toradex-recovery-u-boot.dtsi"

# xxd tool is used in U-boot Makefile when
# CONFIG_USE_DEFAULT_ENV_FILE U-boot option enabled.
DEPENDS += "xxd-native"

# Assign/change a config variable
# $1 - config variable to be set
# $2 - value [n/y/value]
# $3 - config file
#
configure_variable() {
	# Remove the original config, to avoid reassigning it.
	sed -i -e "/CONFIG_$1[ =]/d" $3

	# Assign the config value
	if [ "$2" = "n" ]; then
		echo "# CONFIG_$1 is not set" >> $3
	else
		echo "CONFIG_$1=$2" >> $3
	fi
}

do_configure:prepend () {
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig

    configure_variable ENV_IS_IN_MMC n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    configure_variable ENV_IS_NOWHERE y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    configure_variable TDX_CFG_BLOCK_USB_GADGET_PID n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    configure_variable ENV_IMPORT_FDT y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    configure_variable DEVICE_TREE_INCLUDES \"toradex-recovery-u-boot.dtsi\" ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
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
