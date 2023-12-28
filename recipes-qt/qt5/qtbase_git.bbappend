FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://0026-QSslSocket-make-it-work-with-OpenSSL-v3.patch \
"
