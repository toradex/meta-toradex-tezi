FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "qtdeclarative"

SRC_URI += " \
    file://require-wayland-server-for-client-test.patch \
    file://plugins-with-client-only.patch \
    file://force-decoration.patch \
    file://fix-incorrect-damage-region-for-window-decora.patch \
"
