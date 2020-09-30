FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PROVIDES += "u-boot-fw-utils"
RPROVIDES_${PN} += "u-boot-fw-utils"

SRC_URI_append = " \
    file://fw_env_mmcblk0boot0.config \
    file://0001-tools-env-build-without-default-environment-for-2018.patch \
"
do_install_append() {
    install -m 644 ${WORKDIR}/fw_env_mmcblk0boot0.config ${D}${sysconfdir}/
}
