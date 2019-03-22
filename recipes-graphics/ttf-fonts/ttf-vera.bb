SUMMARY = "Vera truetype fonts"
DESCRIPTION = "The Vera fonts is a font family designed by Jim Lyles from Bitstream. \
It is a TrueType font with full hinting instructions, which improve its \
rendering quality on low-resolution devices such as computer monitors."

SECTION = "fonts"
LICENSE = "BitstreamVera"
LIC_FILES_CHKSUM = "file://COPYRIGHT.TXT;md5=27d7484b1e18d0ee4ce538644a3f04be"
PV = "1.0"
PR = "r1"

inherit allarch fontcache

FONT_PACKAGES = "${PN}"

SRC_URI = "file://bitstream_vera_sans.zip"
SRC_URI[sha256sum] = "73e73a29b8fa26a4bd65a4ab37b0dd33d9297ccc513fc922f64dcd6c5a62361e"

S = "${WORKDIR}"

do_install () {
# Install to both standard and Qt-friendly directories
    install -d ${D}${datadir}/fonts/truetype/
    install -d ${D}${libdir}/fonts/
    for font in *.ttf; do
	install -m -644 $font ${D}${datadir}/fonts/truetype/${font}
	install -m -644 $font ${D}${libdir}/fonts/${font}
    done

    install -d ${D}${datadir}/doc/${BPN}/
    install -m 0644 COPYRIGHT.TXT ${D}${datadir}/doc/${BPN}
}

PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir} ${datadir} ${libdir}"
