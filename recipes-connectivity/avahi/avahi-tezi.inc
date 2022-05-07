SRC_URI += "file://rfb.service"

inherit qmake5_base

PACKAGECONFIG[qt5] = "--enable-qt5,--disable-qt5,qtbase qtbase-native"
PACKAGES =+ "libavahi-qt5"

FILES:libavahi-qt5 = "${libdir}/libavahi-qt5.so.*"

PACKAGECONFIG = "qt5 dbus"

do_install:append() {
        install -m 0644 ${WORKDIR}/rfb.service ${D}${sysconfdir}/avahi/services
        rm -f ${D}${sysconfdir}/avahi/services/sftp-ssh.service
        rm -f ${D}${sysconfdir}/avahi/services/ssh.service
}
