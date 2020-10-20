SUMMARY = "A liberally licensed VNC server library"
DESCRIPTION = "This is a liberally licensed VNC server library\
that's intended to be fast and neat. Note: This is a\
beta release, so the interface is not yet stable."
HOMEPAGE = "https://github.com/any1/neatvnc"

LICENSE = "BSD & ISC & MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

SRC_URI = "git://github.com/any1/neatvnc;protocol=https \
           file://0001-Explicitly-require-aml.patch \
           file://0001-server.c-drop-tight-zrle-encodings.patch \
"

SRCREV = "b1d32694d0a310e36da1cf84420c827bbf665755"

PV = "0.4.0+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libdrm pixman aml zlib"

PACKAGECONFIG ??= "examples"
PACKAGECONFIG[tls] = "-Dtls=enabled,-Dtls=disabled,gnutls"
PACKAGECONFIG[jpeg] = "-Djpeg=enabled,-Djpeg=disabled,libjpeg-turbo"
PACKAGECONFIG[examples] = "-Dexamples=true,-Dexamples=false,libpng"
PACKAGECONFIG[benchmarks] = "-Dbenchmarks=true,-Dbenchmarks=false,libpng"

PACKAGE_BEFORE_PN += "${PN}-examples"
ALLOW_EMPTY_${PN}-examples = "1"
FILES_${PN}-examples = "${bindir}"

NEATVNC_EXAMPLES = "draw png-server"

inherit meson pkgconfig

do_install_append () {
    if ${@bb.utils.contains('PACKAGECONFIG', 'examples', 'true', 'false', d)}; then
        install -d ${D}${bindir}
        for bin in ${NEATVNC_EXAMPLES}; do
            install -m 0755 ${B}/examples/$bin ${D}${bindir}
        done
    fi
}

BBCLASSEXTEND = "native"
