FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append_colibri-imx7 += " \
    file://fw_env_emmc.config \
"

FILES_${PN}-env += " \
    ${sysconfdir}/fw_env*.config \
"

do_install_append_colibri-imx6ull() {
    # re-name to what qt-tezi application expects for NAND based modules
    mv -f ${D}${sysconfdir}/fw_env.config -f ${D}${sysconfdir}/fw_env_mtd4.config
}

do_install_append_colibri-imx7() {
    # first re-name regular one as for BSP this is NAND machine
    mv -f ${D}${sysconfdir}/fw_env.config -f ${D}${sysconfdir}/fw_env_mtd4.config

    # install special eMMC one as regular fw_env.config like qt-tezi
    # application expects for regular block device based modules
    install -D -m 644 ${WORKDIR}/fw_env_emmc.config ${D}${sysconfdir}/fw_env.config
}
