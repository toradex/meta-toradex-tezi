SUMMARY = "Avahi IPv4LL network address configuration daemon"
DESCRIPTION = 'Avahi is a fully LGPL framework for Multicast DNS Service Discovery. It \
allows programs to publish and discover services and hosts running on a local network \
with no specific configuration. This tool implements IPv4LL, "Dynamic Configuration of \
IPv4 Link-Local Addresses" (IETF RFC3927), a protocol for automatic IP address \
configuration from the link-local 169.254.0.0/16 range without the need for a central \
server.'
AUTHOR = "Lennart Poettering <lennart@poettering.net>"
HOMEPAGE = "http://avahi.org"
BUGTRACKER = "https://github.com/lathiat/avahi/issues"
SECTION = "network"

# major part is under LGPL-2.1-or-later, but several .dtd, .xsl, initscripts and
# python scripts are under GPL-2.0-or-later
LICENSE = "GPL-2.0-or-later & LGPL-2.1-or-later"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://avahi-common/address.h;endline=25;md5=b1d1d2cda1c07eb848ea7d6215712d9d \
                    file://avahi-core/dns.h;endline=23;md5=6fe82590b81aa0ddea5095b548e2fdcb \
                    file://avahi-daemon/main.c;endline=21;md5=9ee77368c5407af77caaef1b07285969 \
                    file://avahi-client/client.h;endline=23;md5=f4ac741a25c4f434039ba3e18c8674cf"

SRC_URI = "https://github.com/lathiat/avahi/releases/download/v${PV}/avahi-${PV}.tar.gz"

UPSTREAM_CHECK_URI = "https://github.com/lathiat/avahi/releases/"
SRC_URI[md5sum] = "229c6aa30674fc43c202b22c5f8c2be7"
SRC_URI[sha256sum] = "060309d7a333d38d951bc27598c677af1796934dbd98e1024e7ad8de798fedda"

DEPENDS = "expat libcap libdaemon glib-2.0 intltool-native"

# For gtk related PACKAGECONFIGs: gtk, gtk3
AVAHI_GTK ?= ""

PACKAGECONFIG ??= "dbus ${AVAHI_GTK}"
PACKAGECONFIG[dbus] = "--enable-dbus,--disable-dbus,dbus"
PACKAGECONFIG[gtk] = "--enable-gtk,--disable-gtk,gtk+"
PACKAGECONFIG[gtk3] = "--enable-gtk3,--disable-gtk3,gtk+3"
PACKAGECONFIG[libdns_sd] = "--enable-compat-libdns_sd --enable-dbus,,dbus"

inherit autotools pkgconfig gettext gobject-introspection

EXTRA_OECONF = "--with-avahi-priv-access-group=adm \
             --disable-stack-protector \
             --disable-dbm \
             --disable-gdbm \
             --disable-libevent \
             --disable-mono \
             --disable-monodoc \
             --disable-qt3 \
             --disable-qt4 \
             --disable-python \
             --disable-python-dbus \
             --disable-doxygen-doc \
             --enable-manpages \
             ${EXTRA_OECONF_SYSVINIT} \
             ${EXTRA_OECONF_SYSTEMD} \
           "

# The distro choice determines what init scripts are installed
EXTRA_OECONF_SYSVINIT = "${@bb.utils.contains('DISTRO_FEATURES','sysvinit','--with-distro=debian','--with-distro=none',d)}"
EXTRA_OECONF_SYSTEMD = "${@bb.utils.contains('DISTRO_FEATURES','systemd','--with-systemdsystemunitdir=${systemd_unitdir}/system/','--without-systemdsystemunitdir',d)}"

do_configure:prepend() {
    # This m4 file will get in the way of our introspection.m4 with special cross-compilation fixes
    rm "${S}/common/introspection.m4" || true
}

do_compile:prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/avahi-gobject/.libs:${B}/avahi-common/.libs:${B}/avahi-client/.libs:${B}/avahi-glib/.libs"
}

RRECOMMENDS:${PN}:append:libc-glibc = " libnss-mdns"

do_install() {
	autotools_do_install
	rm -rf ${D}/run
	rm -rf ${D}${datadir}/dbus-1/interfaces
	test -d ${D}${datadir}/dbus-1 && rmdir --ignore-fail-on-non-empty ${D}${datadir}/dbus-1
	rm -rf ${D}${libdir}/avahi
}

PACKAGES =+ "${@bb.utils.contains("PACKAGECONFIG", "libdns_sd", "libavahi-compat-libdnssd", "", d)}"

FILES:libavahi-compat-libdnssd = "${libdir}/libdns_sd.so.*"

RPROVIDES:libavahi-compat-libdnssd = "libdns-sd"
