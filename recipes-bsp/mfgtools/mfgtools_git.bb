SUMMARY = "uuu (Universal Update Utility), mfgtools 3.0"
DESCRIPTION = "Freescale/NXP I.MX Chip image deploy tools, uuu (Universal Update Utility), mfgtools 3.0."
HOMEPAGE = "https://github.com/NXPmicro/mfgtools"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;md5=653656dfc74a26684d74346214abe93a"

SRC_URI = "gitsm://github.com/NXPmicro/mfgtools;branch=master \
           file://0001-CMakeLists.txt-fix-libzip-name.patch \
           file://0001-CMakeLists.txt-support-building-static-uuu.patch \
          "

# uuu_1.2.91
SRCREV = "3799f4d7b0464cc9253b4597ba7ccc54deefca20"

PV = "1.2.91+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libusb1 zlib bzip2"

inherit pkgconfig cmake

PACKAGECONFIG ??= "static"
PACKAGECONFIG[doc] = "-DBUILD_DOC=ON,-DBUILD_DOC=OFF,doxygen-native"
PACKAGECONFIG[static] = "-DBUILD_STATIC=ON,-DBUILD_STATIC=OFF"

# Do not set RPATH if static build is intended for native build.
OECMAKE_RPATH_class-native_forcevariable = "${@bb.utils.contains('PACKAGECONFIG', 'static', '', '${libdir}', d)}"

BBCLASSEXTEND = "native"
