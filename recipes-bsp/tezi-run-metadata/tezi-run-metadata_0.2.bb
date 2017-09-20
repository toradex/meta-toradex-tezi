DESCRIPTION = "Toradex Easy Installer Metadata for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690"

SRC_URI = "file://wrapup.sh \
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
           file://recovery/README"


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
	install -m 644 ${WORKDIR}/recovery/README ${DEPLOYDIR}/recovery/
}

addtask deploy before do_package after do_install

COMPATIBLE_MACHINE = "(apalis-imx6|colibri-imx6|colibri-imx7)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
