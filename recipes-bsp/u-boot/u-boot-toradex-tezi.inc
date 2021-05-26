FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += " \
    file://fw_env_mtd4.config \
"

FILES_${PN}-env += " \
    ${sysconfdir}/fw_env_mtd4.config \
"

do_install_append() {
    if [ -e ${WORKDIR}/fw_env_mtd4.config ] ; then
        [ -e ${D}${sysconfdir}/fw_env_mtd4.config ] || \
            install -D -m 644 ${WORKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/fw_env_mtd4.config
    fi
}
