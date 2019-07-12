SUMMARY = "uuu (Universal Update Utility), mfgtools 3.0"
DESCRIPTION = "Freescale/NXP I.MX Chip image deploy tools, uuu (Universal Update Utility), mfgtools 3.0."
HOMEPAGE = "https://github.com/NXPmicro/mfgtools"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;md5=ebcb60b1c2900d1d6befa33ceaa94ea2"

SRC_URI = "gitsm://github.com/NXPmicro/mfgtools;branch=master \
           file://0001-CMakeLists.txt-fix-libzip-name.patch \
           file://0001-CMakeLists.txt-do-not-override-CMAKE_CXX_FLAGS.patch \
          "

# uuu_1.2.135
SRCREV = "acaf035cf7ca930ee43cd70edd1dad9fd56395f3"

PV = "1.2.135+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libusb1 zlib bzip2"

inherit pkgconfig cmake

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "-DBUILD_DOC=ON,-DBUILD_DOC=OFF,doxygen-native"

BBCLASSEXTEND = "native"
