FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "qtdeclarative"

SRC_URI += " \
    file://0001-qtwayland-force-enable-decoration.patch \
"
