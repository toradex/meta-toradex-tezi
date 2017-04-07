SUMMARY = "Toradex Tester scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

RDEPENDS_${PN} = "busybox"

SRC_URI = "file://rc.local \
           file://flash_tezi.sh \
	  "
SRCREV = "7e846e4ac667cce0bcd1407ad9bc19305f2f1cfd"

do_configure() {
	:
}

do_compile() {
	:
}

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/rc.local ${D}${sysconfdir}

    install -d ${D}${bindir}/
    install -m 0755 ${WORKDIR}/flash_tezi.sh ${D}${bindir}/
}

FILES_${PN} = "${sysconfdir}/rc.local \
               ${bindir}/flash_tezi.sh \
"
