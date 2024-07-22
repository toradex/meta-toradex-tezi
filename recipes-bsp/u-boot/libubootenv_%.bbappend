FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://fw_env_emmc.config \
    file://fw_env_mtd4.config \
"

# For the NAND/eMMC modules qt-tezi expects fw_env.config for use on
# a eMMC based module and fw_env_mtd4.config for a NAND based one.
copy_emmc_env_cfg () {
    # replace the NAND fw_env.config with the one for eMMC
    cp ${UNPACKDIR}/fw_env_emmc.config ${UNPACKDIR}/fw_env.config
}

install_mtd4_cfg () {
    install -d ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/
}

do_install:prepend:colibri-imx6ull () {
    copy_emmc_env_cfg
}

do_install:append:colibri-imx6ull () {
    install_mtd4_cfg
}

do_install:prepend:colibri-imx7 () {
    copy_emmc_env_cfg
}

do_install:append:colibri-imx7 () {
    install_mtd4_cfg
}
