DESCRIPTION = "LXDE Randr graphical frontend"
HOMEPAGE = "http://lxde.org/"
SECTION = "x11"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "xrandr gtk+"

SRC_URI = "${SOURCEFORGE_MIRROR}/lxde/${PN}-${PV}.tar.gz \
	file://lxrandr_am_prog_cc_c_o.patch \
"
SRC_URI[md5sum] = "8a7391581541bba58839ac11dbf5b575"
SRC_URI[sha256sum] = "fb8139478f6cfeac6a2d8adb4e55e8cad099bfe2da7c82253c935ba719f9cf19"

do_configure_prepend () {
    mv ${S}/configure.in ${S}/configure.ac
}

inherit autotools gettext pkgconfig
