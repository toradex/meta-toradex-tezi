DESCRIPTION = "NVIDIAS tegrastats output in a gtk title bar"
LICENSE = "NVIDIA Propriatry"

DEPENDS = "gtk+"
PR = "r1"

S = "${WORKDIR}"

SRC_URI="file://stats.c file://main.c  file://Makefile"
LIC_FILES_CHKSUM = "file://main.c;endline=9;md5=2fa47532d931bff0348b6d6835bf86ed"

do_install () {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/tegrastats-gtk ${D}${bindir}/
}

FILES_${PN} = "${bindir}/tegrastats-gtk"