FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
	file://0001-Makefile-fix-generation-of-defaultenv.h-from-empty-i.patch 	\
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
