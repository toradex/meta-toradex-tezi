SECTION = "debug"
DESCRIPTION = "glmark2 is a benchmark for OpenGL (ES) 2.0. It uses only the subset of the OpenGL 2.0 API that is compatible with OpenGL ES 2.0."
HOMEPAGE = "https://launchpad.net/glmark2"
LICENSE ="GPLv3"
RDEPENDS_${PN} = ""
DEPENDS_${PN} = "python-native libpng12 virtual/jpeg"

SRC_URI = "https://launchpad.net/${PN}/trunk/${PV}/+download/${PN}-${PV}.tar.gz \
    file://gl-char.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

PR = "r4"

do_configure() {
}

do_compile() {
    ./waf configure --enable-glesv2 --prefix=/usr
    ./waf
    mkdir -p tmp/
    cp build/src/glmark2* tmp/
    ./waf configure --enable-glesv2 --prefix=${D}/usr
    ./waf
    rm build/src/glmark2*
    cp tmp/glmark2* build/src/
}

FILES_${PN} += "/usr"
do_install() {
    ./waf install
}

SRC_URI[md5sum] = "f924a8019df2494222b718c47f6cbdc3"
#SRC_URI[sha256sum] = "252390e4bc687957f09f334095904c8cc53b39c7b663ed47861ae1d11aef5946"
