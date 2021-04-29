FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
	file://0001-Makefile-fix-generation-of-defaultenv.h-from-empty-i.patch \
	file://fw_env.config \
"
