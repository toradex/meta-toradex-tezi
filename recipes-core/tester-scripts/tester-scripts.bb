SUMMARY = "Toradex Tester scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

RDEPENDS_${PN} = "busybox"

SRC_URI = "git://eng-git.toradex.int/toradex-tester.git;branch=master;protocol=git \
           file://rc.local \
           file://flash_tezi.sh \
	  "
SRCREV = "${AUTOREV}"

RDEPENDS_${PN} = " \
        python3 \
        python3-ctypes \
        python3-importlib \
        python3-pyserial \
        python3-textutils \
    " 

S = "${WORKDIR}/git"

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

    install -d ${D}${bindir}/
    install -m 0755 ${S}/tdxtester ${D}${bindir}/
    install -d ${D}${libdir}/tdxtester/
    install -m 0644 ${S}/modules/* ${D}${libdir}/tdxtester/
}

FILES_${PN} = "${sysconfdir}/rc.local \
               ${bindir}/tdxtester \
               ${bindir}/flash_tezi.sh \
               ${libdir}/tdxtester/ \
"
