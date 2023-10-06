FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://fw_env_emmc.config \
                   file://fw_env_mtd4.config \
                   file://toradex-recovery-u-boot.dtsi \
"

FILES:${PN}-env += " \
    ${sysconfdir}/fw_env*.config \
"

inherit toradex-kernel-config

# For modules which have a eMMC and a raw NAND variant install a fw_env*conf
# for each variant
install_emmc_nand_fw_env() {
    # install special NAND one as regular fw_env_mtd4.config for qt-tezi
    install -D -m 644 ${WORKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/fw_env_mtd4.config

    # install special eMMC one as regular fw_env.config like qt-tezi
    # application expects for regular block device based modules
    install -D -m 644 ${WORKDIR}/fw_env_emmc.config ${D}${sysconfdir}/fw_env.config
}

do_install:append:colibri-imx6ull() {
    install_emmc_nand_fw_env
}

do_install:append:colibri-imx7() {
    install_emmc_nand_fw_env
}

do_configure:prepend () {
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig    
    
    kconfig_configure_variable ENV_IS_IN_MMC n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IS_NOWHERE y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable ENV_IMPORT_FDT y ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable TDX_CFG_BLOCK_USB_GADGET_PID n ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    kconfig_configure_variable DEVICE_TREE_INCLUDES \"toradex-recovery-u-boot.dtsi\" ${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
}

do_compile:prepend () {
    cp ${WORKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
}