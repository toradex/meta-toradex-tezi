require avahi.inc avahi-tezi.inc

SRC_URI += "file://avahi-0.8-qt5.patch \
           file://00avahi-autoipd \
           file://99avahi-autoipd \
           file://initscript.patch \
           file://0001-Fix-opening-etc-resolv.conf-error.patch \
           "

inherit update-rc.d systemd useradd

PACKAGES =+ "libavahi-gobject avahi-daemon libavahi-common libavahi-core libavahi-client avahi-dnsconfd libavahi-glib avahi-autoipd avahi-utils"

LICENSE:libavahi-gobject = "LGPL-2.1-or-later"
LICENSE:avahi-daemon = "LGPL-2.1-or-later"
LICENSE:libavahi-common = "LGPL-2.1-or-later"
LICENSE:libavahi-core = "LGPL-2.1-or-later"
LICENSE:avahi-client = "LGPL-2.1-or-later"
LICENSE:avahi-dnsconfd = "LGPL-2.1-or-later"
LICENSE:libavahi-glib = "LGPL-2.1-or-later"
LICENSE:avahi-autoipd = "LGPL-2.1-or-later"
LICENSE:avahi-utils = "LGPL-2.1-or-later"

# As avahi doesn't put any files into PN, clear the files list to avoid problems
# if extra libraries appear.
FILES:${PN} = ""
FILES:avahi-autoipd = "${sbindir}/avahi-autoipd \
                       ${sysconfdir}/avahi/avahi-autoipd.action \
                       ${sysconfdir}/dhcp/*/avahi-autoipd \
                       ${sysconfdir}/udhcpc.d/00avahi-autoipd \
                       ${sysconfdir}/udhcpc.d/99avahi-autoipd"
FILES:libavahi-common = "${libdir}/libavahi-common.so.*"
FILES:libavahi-core = "${libdir}/libavahi-core.so.* ${libdir}/girepository-1.0/AvahiCore*.typelib"
FILES:avahi-daemon = "${sbindir}/avahi-daemon \
                      ${sysconfdir}/avahi/avahi-daemon.conf \
                      ${sysconfdir}/avahi/hosts \
                      ${sysconfdir}/avahi/services \
                      ${sysconfdir}/dbus-1 \
                      ${sysconfdir}/init.d/avahi-daemon \
                      ${datadir}/avahi/introspection/*.introspect \
                      ${datadir}/avahi/avahi-service.dtd \
                      ${datadir}/avahi/service-types \
                      ${datadir}/dbus-1/system-services"
FILES:libavahi-client = "${libdir}/libavahi-client.so.*"
FILES:avahi-dnsconfd = "${sbindir}/avahi-dnsconfd \
                        ${sysconfdir}/avahi/avahi-dnsconfd.action \
                        ${sysconfdir}/init.d/avahi-dnsconfd"
FILES:libavahi-glib = "${libdir}/libavahi-glib.so.*"
FILES:libavahi-gobject = "${libdir}/libavahi-gobject.so.*  ${libdir}/girepository-1.0/Avahi*.typelib"
FILES:avahi-utils = "${bindir}/avahi-*"

RDEPENDS:${PN}-dev = "avahi-daemon (= ${EXTENDPKGV}) libavahi-core (= ${EXTENDPKGV})"
RDEPENDS:${PN}-dev += "${@["", " libavahi-client (= ${EXTENDPKGV})"][bb.utils.contains('PACKAGECONFIG', 'dbus', 1, 0, d)]}"

RRECOMMENDS:avahi-daemon:append:libc-glibc = " libnss-mdns"

CONFFILES:avahi-daemon = "${sysconfdir}/avahi/avahi-daemon.conf"

USERADD_PACKAGES = "avahi-daemon avahi-autoipd"
USERADD_PARAM:avahi-daemon = "--system --home /run/avahi-daemon \
                              --no-create-home --shell /bin/false \
                              --user-group avahi"

USERADD_PARAM:avahi-autoipd = "--system --home /run/avahi-autoipd \
                              --no-create-home --shell /bin/false \
                              --user-group \
                              -c \"Avahi autoip daemon\" \
                              avahi-autoipd"

INITSCRIPT_PACKAGES = "avahi-daemon avahi-dnsconfd"
INITSCRIPT_NAME:avahi-daemon = "avahi-daemon"
INITSCRIPT_PARAMS:avahi-daemon = "defaults 21 19"
INITSCRIPT_NAME:avahi-dnsconfd = "avahi-dnsconfd"
INITSCRIPT_PARAMS:avahi-dnsconfd = "defaults 22 19"

SYSTEMD_PACKAGES = "${PN}-daemon ${PN}-dnsconfd"
SYSTEMD_SERVICE:${PN}-daemon = "avahi-daemon.service"
SYSTEMD_SERVICE:${PN}-dnsconfd = "avahi-dnsconfd.service"

OURFILEPATH = "${@d.getVar("UNPACKDIR") or '${WORKDIR}'}"

do_install:append() {
	install -d ${D}${sysconfdir}/udhcpc.d
	install ${OURFILEPATH}/00avahi-autoipd ${D}${sysconfdir}/udhcpc.d
	install ${OURFILEPATH}/99avahi-autoipd ${D}${sysconfdir}/udhcpc.d
}

# At the time the postinst runs, dbus might not be setup so only restart if running 
# Don't exit early, because update-rc.d needs to run subsequently.
pkg_postinst:avahi-daemon () {
if [ -z "$D" ]; then
	killall -q -HUP dbus-daemon || true
fi
}
