FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "qtdeclarative"

SRC_URI += " \
    file://plugins-with-client-only.patch \
    file://force-decoration.patch \
"
