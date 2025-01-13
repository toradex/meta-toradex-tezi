FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGECONFIG:remove = "hwdb"

SRC_URI:append = " \
    file://0001-random-util.c-sync-dev_urandom-implementation-to-sys.patch \
    "
