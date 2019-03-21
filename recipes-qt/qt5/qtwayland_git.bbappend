FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "qtdeclarative"

SRC_URI += " \
    file://require-wayland-server-for-client-test.patch \
"
