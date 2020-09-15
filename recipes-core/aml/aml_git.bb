SUMMARY = "Andri's Main Loop"
DESCRIPTION = "Andri's Main Loop"
HOMEPAGE = "https://github.com/any1/aml"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=af623d135dc7dd7e8963c0051f96aa37"

SRC_URI = "git://github.com/any1/aml;protocol=https"

SRCREV = "1d8185ec15c68074cec1fd252c01c5ecad877b73"

PV = "0.2.0+git${SRCPV}"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= ""
PACKAGECONFIG[examples] = "-Dexamples=true,-Dexamples=false"

PACKAGE_BEFORE_PN += "${PN}-examples"
ALLOW_EMPTY_${PN}-examples = "1"
FILES_${PN}-examples = "${bindir}"

inherit meson pkgconfig

do_install_append () {
    if ${@bb.utils.contains('PACKAGECONFIG', 'examples', 'true', 'false', d)}; then
        install -d ${D}${bindir}
        install -m 0755 ${B}/examples/ticker ${D}${bindir}
        install -m 0755 ${B}/examples/reader ${D}${bindir}
    fi
}

BBCLASSEXTEND = "native"
