SUMMARY = "Toradex Easy Installer Console Control Tool"
DESCRIPTION = "Toradex Easy Installer Console Control Tool"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://github.com/toradex/qt-tezictl.git;branch=${SRCBRANCH};protocol=https"

SRCREV = "8feb64026685eeb49b5b60ab85e1a78b1b131d2b"
SRCBRANCH = "master"
SRCREV:use-head-next = "${AUTOREV}"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=1611c5652037dacc6ac585f2443a1979"

S = "${WORKDIR}/git"

PV = "${TDX_VERSION}+git${SRCPV}"

EXTRA_QMAKEVARS_PRE:append = " DEFINES+=VERSION_NUMBER=\\\\\\\"${TDX_VERSION}\\\\\\\""

inherit qmake5

DEPENDS += " \
    qtbase \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/../build/tezictl ${D}${bindir}/
}
