FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://fw_env_emmc.config \
                   file://fw_env_mtd4.config \
"

FILES:${PN}-env += " \
    ${sysconfdir}/fw_env*.config \
"

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
