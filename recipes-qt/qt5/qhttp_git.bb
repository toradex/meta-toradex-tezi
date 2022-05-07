SUMMARY = "A light-weight and asynchronous HTTP library (both server & client) in Qt5 and c++14"
DESCRIPTION = "QHttp is a lightweight, asynchronous and fast HTTP library in c++14 / Qt5,\
containing both server and client side classes for managing connections, parsing\
and building HTTP requests and responses."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c758fe2787c648b41befe35a54326b03"

SRC_URI = "gitsm://github.com/Hypelab/qhttp;branch=master;protocol=https \
           file://0001-Fix-QWebSocketServer-header-path.patch \
           file://0001-examples-add-websockets-module.patch \
"

SRCREV = "67c9e9c44ababcfae54420dd385cd5b858f91d54"

PV = "3.2+git${SRCPV}"

S = "${WORKDIR}/git"

DEPENDS += "qtwebsockets qtbase"

EXTRA_QMAKEVARS_PRE += "PREFIX=${prefix} ENABLE_QHTTP_CLIENT=0"

PACKAGES =+ "${PN}-examples"

FILES:${PN}-examples = "${bindir}/*"

inherit qmake5
