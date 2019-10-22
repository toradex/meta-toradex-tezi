FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI = "file://weston.ini"

do_install () {
	install -d ${D}${sysconfdir}/xdg/weston
	install ${WORKDIR}/weston.ini ${D}${sysconfdir}/xdg/weston
}
