SUMMARY = "Scripts and systemd service file to export unused GPIO to Userspace"
LICENSE = "PD"
PR = "r2"

inherit systemd

SRC_URI =  " \
    file://gpio-export.sh \
    file://gpio-export.service \
    file://COPYING \
"

LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING;md5=1c3a7fb45253c11c74434676d84fe7dd"

do_compile () {
}

do_install () {
    install -d ${D}/${bindir}
    install -m 0755 ${WORKDIR}/*.sh ${D}/${bindir}

    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/gpio-export.service ${D}${systemd_unitdir}/system
}

NATIVE_SYSTEMD_SUPPORT = "1"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "gpio-export.service"

PACKAGE_ARCH = "${MACHINE_ARCH}"