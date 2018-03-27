DESCRIPTION = "Toradex Easy Installer Metadata for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

SRC_URI = " \
    http://sources.toradex.com/tezi/${PN}_${PV}.tar.xz \
    file://wrapup.sh \
    file://tezi.png \
    file://tezi.its \
    file://recovery-linux.sh \
    file://recovery-windows.bat \
    file://recovery/imx_usb.conf \
    file://recovery/mx6_usb_rom.conf \
    file://recovery/mx6_usb_sdp_spl.conf \
    file://recovery/mx6_usb_sdp_uboot.conf \
    file://recovery/mx7_usb_rom.conf \
    file://recovery/mx7_usb_sdp_uboot.conf \
    file://recovery/mx6ull_usb_rom.conf \
    file://recovery/mx6ull_usb_sdp_uboot.conf \
"
SRC_URI_append_apalis-tk1 = " \
    file://recovery/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct \
"
SRC_URI_append_apalis-tk1-mainline = " \
    file://recovery/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct \
"

SRC_URI[md5sum] = "11cf6d9b4b18b7f35f06ed91bfc0b3a8"
SRC_URI[sha256sum] = "064bff2e4cb4a0c0f8bceeaf6dd2cef1e682869e0b22d49e40bc6a8326ec14c9"

inherit deploy

do_deploy () {
    install -m 644 ${WORKDIR}/wrapup.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.png ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.its ${DEPLOYDIR}

    install -m 755 ${WORKDIR}/recovery-linux.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/recovery-windows.bat ${DEPLOYDIR}
    install -d ${DEPLOYDIR}/recovery
    install -m 644 ${WORKDIR}/recovery/imx_usb.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_rom.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_sdp_spl.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6_usb_sdp_uboot.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx7_usb_rom.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx7_usb_sdp_uboot.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6ull_usb_rom.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/mx6ull_usb_sdp_uboot.conf ${DEPLOYDIR}/recovery/
    install -m 644 ${S}/README.imx_usb ${DEPLOYDIR}/recovery/README
    install -m 755 ${S}/imx_usb ${DEPLOYDIR}/recovery/
    install -m 644 ${S}/imx_usb.exe ${DEPLOYDIR}/recovery/
}

do_deploy_apalis-tk1 () {
    install -m 644 ${WORKDIR}/wrapup.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.png ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.its ${DEPLOYDIR}

    install -m 755 ${WORKDIR}/recovery-linux.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/recovery-windows.bat ${DEPLOYDIR}
    install -d ${DEPLOYDIR}/recovery
    install -m 644 ${WORKDIR}/recovery/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct ${DEPLOYDIR}/recovery/
    install -m 644 ${S}/README.tegrarcm ${DEPLOYDIR}/recovery/README
    install -m 755 ${S}/tegrarcm ${DEPLOYDIR}/recovery/
}

do_deploy_apalis-tk1-mainline () {
    install -m 644 ${WORKDIR}/wrapup.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.png ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.its ${DEPLOYDIR}

    install -m 755 ${WORKDIR}/recovery-linux.sh ${DEPLOYDIR}
    install -m 644 ${WORKDIR}/recovery-windows.bat ${DEPLOYDIR}
    install -d ${DEPLOYDIR}/recovery
    install -m 644 ${WORKDIR}/recovery/PM375_Hynix_2GB_H5TC4G63AFR_RDA_924MHz.bct ${DEPLOYDIR}/recovery/
    install -m 644 ${S}/README.tegrarcm ${DEPLOYDIR}/recovery/README
    install -m 755 ${S}/tegrarcm ${DEPLOYDIR}/recovery/
}

addtask deploy before do_package after do_install

# apalis-tk1 will include apalis-tk1-mainline as well
COMPATIBLE_MACHINE = "(apalis-imx6|apalis-tk1|colibri-imx6|colibri-imx7)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
