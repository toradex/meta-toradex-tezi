SUMMARY = "uuu (Universal Update Utility), mfgtools 3.0"
DESCRIPTION = "Freescale/NXP I.MX Chip image deploy tools, uuu (Universal Update Utility), mfgtools 3.0."
HOMEPAGE = "https://github.com/NXPmicro/mfgtools"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;md5=77eb0ff1922382a72698fdbdbf2df358"

SRC_URI = "git://github.com/NXPmicro/mfgtools;branch=master \
           file://0001-CMakeLists.txt-fix-libzip-name.patch \
          "

# tag uuu_1.4.43
SRCREV = "a9c099a8b4205a9c0af4b620478d393709d3575c"

PV = "1.4.43+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libusb1 zlib bzip2 openssl"

inherit pkgconfig cmake

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "-DBUILD_DOC=ON,-DBUILD_DOC=OFF,doxygen-native"

BBCLASSEXTEND = "native"
