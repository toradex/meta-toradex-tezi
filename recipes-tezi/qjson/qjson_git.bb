DESCRIPTION = "A Json library for QT"
HOMEPAGE = "http://qjson.sourceforge.net/"
SECTION = "libs"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING.lib;md5=3511d726d09144c8590aba4623ca2e9f"

SRCREV = "c870d00a5a287ccb3032d33fe01ceef13c1ec003"
PV = "0.8.1+git${SRCPV}"

PR = "r0"
S = "${WORKDIR}/git"

SRC_URI = "git://github.com/flavio/qjson.git"

FILES_${PN} = "${libdir}/qt4"

#RDEPENDS_pn += "qt4-embedded"
#DEPENDS += "cmake pkgconfig qt4-embedded"

inherit qt4e cmake
inherit autotools gettext

EXTRA_OECMAKE = "-DQT_LIBRARY_DIR=${OE_QMAKE_LIBDIR_QT} \
                 -DQT_INSTALL_LIBS=${OE_QMAKE_LIBDIR_QT} \
                 -DQT_INCLUDE_DIR=${OE_QMAKE_INCDIR_QT} \
		 -DQT_HEADERS_DIR=${OE_QMAKE_INCDIR_QT} \
                 -DQT_MOC_EXECUTABLE=${OE_QMAKE_MOC} \
                 -DQT_UIC_EXECUTABLE=${OE_QMAKE_UIC} \
                 -DQT_UIC3_EXECUTABLE=${OE_QMAKE_UIC3} \
                 -DQT_RCC_EXECUTABLE=${OE_QMAKE_RCC} \
                 -DQT_QMAKE_EXECUTABLE=${OE_QMAKE_QMAKE} \
                 -DQT_QTCORE_INCLUDE_DIR=${OE_QMAKE_INCDIR_QT}/QtCore \
                 -DQT_DBUSXML2CPP_EXECUTABLE=/usr/bin/qdbusxml2cpp \
                 -DQT_DBUSCPP2XML_EXECUTABLE=/usr/bin/qdbuscpp2xml \
		 -DQT_MKSPECS_DIR=${QMAKESPEC}/../ \
                "

# Adding our lib file to the main package
FILES_${PN} = "${libdir}/*"

do_configure_prepend() {
}

do_install_prepend() {
}
