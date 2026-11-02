SUMMARY = "Fixed EDID for the Toradex Easy Installer display outputs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://tezi.bin"

S = "${UNPACKDIR}"

inherit allarch

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
	install -d ${D}${nonarch_base_libdir}/firmware/edid
	install -m 0644 ${S}/tezi.bin ${D}${nonarch_base_libdir}/firmware/edid
}

FILES:${PN} = "${nonarch_base_libdir}/firmware/edid"
