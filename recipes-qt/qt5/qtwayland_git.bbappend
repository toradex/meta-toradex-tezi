FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS:remove = "qtdeclarative"

SRC_URI += " \
    file://0001-qtwayland-force-enable-decoration.patch \
"
