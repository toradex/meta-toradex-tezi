FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit qmake5_base

SRC_URI += "file://avahi-0.7-qt5.patch"

PACKAGECONFIG[qt5] = "--enable-qt5,--disable-qt5,qtbase qtbase-native"
PACKAGES =+ "libavahi-qt5"

FILES_libavahi-qt5 = "${libdir}/libavahi-qt5.so.*"

PACKAGECONFIG = "qt5 dbus"

do_install_append() {
	rm -f ${D}${sysconfdir}/avahi/services/sftp-ssh.service
	rm -f ${D}${sysconfdir}/avahi/services/ssh.service
}

