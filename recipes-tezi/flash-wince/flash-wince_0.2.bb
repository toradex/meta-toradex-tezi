DESCRIPTION = "Toradex WinCE raw NAND flashing utility"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://flash-wince.c;beginline=2;endline=6;md5=6f49165cd70d5a6f743fce9338c73a12"

SRC_URI = "git://eng-git.toradex.int/cgit/ags/flash-wince.git;branch=master;protocol=http"

SRCREV = "3a05f788a5d77d04a167dd556c17a181fbddaaef"

S = "${WORKDIR}/git"

do_install() {
	oe_runmake 'DESTDIR=${D}' install
}
