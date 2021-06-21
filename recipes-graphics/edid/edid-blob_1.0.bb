DESCRIPTION = "Injects binary edid blobs into /etc/firmware/edid"
HOMEPAGE = "http://www.toradex.com"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://1280x720.bin"

do_install() {
    install -d ${D}/lib/firmware/edid/
    install -m 0644 ${WORKDIR}/*.bin ${D}/lib/firmware/edid/
}

FILES_${PN} += " \
    /lib/firmware/edid/ \
"
