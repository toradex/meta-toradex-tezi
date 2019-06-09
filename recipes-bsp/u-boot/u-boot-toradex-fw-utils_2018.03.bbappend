FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PROVIDES += "u-boot-fw-utils"
RPROVIDES_${PN} += "u-boot-fw-utils"

SRC_URI_append = " \
    file://fw_env_mmcblk0boot0.config \
"
do_install_append() {
    install -m 644 ${WORKDIR}/fw_env_mmcblk0boot0.config ${D}${sysconfdir}/
}
