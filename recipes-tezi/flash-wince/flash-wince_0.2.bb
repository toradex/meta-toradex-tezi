DESCRIPTION = "Toradex WinCE raw NAND flashing utility"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://flash-wince.c;beginline=2;endline=6;md5=6f49165cd70d5a6f743fce9338c73a12"

SRC_URI = "git://gitlab.int.toradex.com/tezi/flash-wince.git;branch=master;protocol=https"

SRCREV = "1c79234a6951a23924756b4563154c93af45a52d"

S = "${WORKDIR}/git"

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
