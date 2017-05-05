DESCRIPTION = "Toradex Easy Installer Metadata"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

SRC_URI = " \
    file://prepare.sh \
    file://wrapup.sh \
    file://toradexlinux.png \
    file://marketing.tar;unpack=false \
"

inherit deploy

do_deploy () {
    install -m 644 ${WORKDIR}/prepare.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/wrapup.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/toradexlinux.png ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/marketing.tar ${DEPLOYDIR}
}

addtask deploy before do_package after do_install

COMPATIBLE_MACHINE = "(apalis-imx6|colibri-imx6|colibri-imx7)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
