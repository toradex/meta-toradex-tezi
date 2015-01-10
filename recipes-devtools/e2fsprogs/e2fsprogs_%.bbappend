FILESEXTRAPATHS_prepend := "${THISDIR}/e2fsprogs:"
SRC_URI += "file://e2fsck.conf"

do_install_append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/e2fsck.conf ${D}${sysconfdir}/e2fsck.conf
}

FILES_e2fsprogs-e2fsck += "${sysconfdir}/e2fsck.conf"
