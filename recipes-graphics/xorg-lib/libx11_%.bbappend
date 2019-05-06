PACKAGES_remove = "${PN}-locale"

# locale data files would be provided by libx11-locale recipe
do_install_append() {
	rm -rf ${D}${datadir}/X11/locale
	rm -rf ${D}${libdir}/X11/locale
}
