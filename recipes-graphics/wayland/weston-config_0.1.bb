SUMMARY = "Weston configuration"
DESCRIPTION = "Configuration files for the weston compositor"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://weston.ini"
SRC_URI[md5sum] = "e3bb1942a786866dc91f9888a6228b7e"
SRC_URI[sha256sum] = "2fe72b93dd1e63907f9248b8e1c9f10d32d04f8ee6f01de9a17b887c2c659373"

do_install() {
	install -d ${D}${sysconfdir}/xdg/weston
	install ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston
}

FILES_${PN} = "${sysconfdir}/xdg/weston ${sysconfdir}/xdg/weston/weston.ini"
