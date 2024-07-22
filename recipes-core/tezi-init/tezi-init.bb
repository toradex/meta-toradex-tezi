SUMMARY = "Toradex Easy Inistaller init"
DESCRIPTION = "Basic init system for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

RDEPENDS:${PN} = "busybox"

SRC_URI = " \
    file://init \
    file://initrd \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
	install -m 0755 ${WORKDIR}/initrd ${D}/init
	install -d ${D}${base_sbindir}
	install -m 0755 ${WORKDIR}/init ${D}/${base_sbindir}/init
}

FILES:${PN} = " \
    /init \
    ${base_sbindir}/init \
"
