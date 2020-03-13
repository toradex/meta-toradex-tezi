SUMMARY = "uuu (Universal Update Utility), mfgtools 3.0"
DESCRIPTION = "Freescale/NXP I.MX Chip image deploy tools, uuu (Universal Update Utility), mfgtools 3.0."
HOMEPAGE = "https://github.com/NXPmicro/mfgtools"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://README.md;md5=eab17ab7606a35858f2f781aeec08b1c"

SRC_URI = "gitsm://github.com/NXPmicro/mfgtools;branch=master \
           file://0001-CMakeLists.txt-fix-libzip-name.patch \
           file://0001-CMakeLists.txt-do-not-override-CMAKE_CXX_FLAGS.patch \
          "

# uuu_1.3.136
SRCREV = "1ecc47fc0172983ac232dedfdae9240c52ea6300"

PV = "1.3.136+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libusb1 zlib bzip2 openssl"

inherit pkgconfig cmake

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "-DBUILD_DOC=ON,-DBUILD_DOC=OFF,doxygen-native"

BBCLASSEXTEND = "native"
