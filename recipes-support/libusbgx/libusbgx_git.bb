SUMMARY = "USB Gadget neXt Configfs Library"

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c"

inherit autotools pkgconfig

DEPENDS = "libconfig"

PV = "0.1.0+git${SRCPV}"
SRCREV = "9e136571aa4b82fe50028d82ebf14541a7c6245f"
SRC_URI = "git://github.com/libusbgx/libusbgx.git \
          "

S = "${WORKDIR}/git"

