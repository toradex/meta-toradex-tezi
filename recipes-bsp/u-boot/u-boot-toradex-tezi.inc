FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append += " \
    file://fw_env.config \
    file://fw_env_mtd4.config \
"

FILES_${PN}-env += " \
    ${sysconfdir}/fw_env.config \
    ${sysconfdir}/fw_env_mtd4.config \
    ${sysconfdir}/fw_env_mmcblk0boot0.config \
    ${sysconfdir}/fw_env_emmc-boot0.config \
"

do_install_append() {
    if [ -e ${WORKDIR}/fw_env_mtd4.config ] ; then
        [ -e ${D}${sysconfdir}/fw_env_mtd4.config ] || \
            install -D -m 644 ${WORKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/fw_env_mtd4.config
    fi
    if [ -e ${WORKDIR}/fw_env.config ] ; then
        [ -e ${D}${sysconfdir}/fw_env.config ] || \
            install -D -m 644 ${WORKDIR}/fw_env.config ${D}${sysconfdir}/fw_env.config
        ln -s fw_env.config ${D}${sysconfdir}/fw_env_mmcblk0boot0.config
        ln -s fw_env.config ${D}${sysconfdir}/fw_env_emmc-boot0.config
   fi
}
