FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

inherit toradex-kernel-config
include u-boot-toradex-tezi.inc  

SRC_URI += "file://toradex-recovery-u-boot.dtsi"

do_configure:prepend () {
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig    
    kconfig_configure_variable ENV_IS_IN_MMC n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IS_NOWHERE y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IMPORT_FDT y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable DEVICE_TREE_INCLUDES \"toradex-recovery-u-boot.dtsi\" ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
}

do_compile:prepend () {
    cp ${WORKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
}