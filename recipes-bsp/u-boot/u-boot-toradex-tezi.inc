FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += " \
    file://fw_env_mtd4.config \
"

SRC_URI_append_colibri-imx7 += " \
    file://fw_env_emmc.config \
"

FILES_${PN}-env += " \
    ${sysconfdir}/fw_env_mtd4.config \
"

do_install_append() {
    install -D -m 644 ${WORKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/fw_env_mtd4.config
}

do_install_append_colibri-imx7() {
    rm -f ${D}${sysconfdir}/fw_env.config
    install -D -m 644 ${WORKDIR}/fw_env_emmc.config ${D}${sysconfdir}/fw_env.config
}
