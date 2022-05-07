SUMMARY = "uuu (Universal Update Utility), mfgtools 3.0"
DESCRIPTION = "Freescale/NXP I.MX Chip image deploy tools, uuu (Universal Update Utility), mfgtools 3.0."
HOMEPAGE = "https://github.com/NXPmicro/mfgtools"
SECTION = "devel"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=38ec0c18112e9a92cffc4951661e85a5"

SRC_URI = "git://github.com/NXPmicro/mfgtools;branch=master;protocol=https \
           file://0001-CMakeLists.txt-fix-libzip-name.patch \
          "

# tag uuu_1.4.127
SRCREV = "08c58c9a968dbf1b1c0c204cfee4c307d94f0ecd"

PV = "1.4.127+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS = "libusb1 zlib bzip2 openssl"

inherit pkgconfig cmake

PACKAGECONFIG ??= ""
PACKAGECONFIG[doc] = "-DBUILD_DOC=ON,-DBUILD_DOC=OFF,doxygen-native"

BBCLASSEXTEND = "native"
