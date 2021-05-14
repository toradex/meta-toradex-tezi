FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# xxd tool is used in U-boot Makefile when
# CONFIG_USE_DEFAULT_ENV_FILE U-boot option enabled.
DEPENDS += "xxd-native"

SRC_URI_append = " \
	file://fw_env.config							\
	file://fw_env_mmcblk0boot0.config					\
"

FILES_${PN}-env += " \
	${sysconfdir}/fw_env_mmcblk0boot0.config	\
	${base_sbindir}/fw_setenv			\
"

do_install_append() {
	install -d ${D}${sysconfdir}
	install -m 644 ${WORKDIR}/fw_env_mmcblk0boot0.config ${D}${sysconfdir}/fw_env_mmcblk0boot0.config
	install -d ${D}${base_sbindir}
	ln -s /usr/bin/fw_setenv ${D}${base_sbindir}/fw_setenv
}
