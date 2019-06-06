FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
    file://fw_env_mmcblk0boot0.config \
"
do_install_append() {
    install -m 644 ${WORKDIR}/fw_env_mmcblk0boot0.config ${D}${sysconfdir}/
}
