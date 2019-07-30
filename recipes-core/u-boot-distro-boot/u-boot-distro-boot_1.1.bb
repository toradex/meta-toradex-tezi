SUMMARY = "Generate uboot scripts"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://boot.cmd \
    ${@oe.utils.ifelse(d.getVar('UBOOT_SDP_SUPPORT') == '1', 'file://boot-sdp.cmd', '')} \
"

INHIBIT_DEFAULT_DEPS = "1"

inherit deploy nopackages

do_deploy () {
    sed -i 's/@@TEZI_INITRD_IMAGE@@/${TEZI_INITRD_IMAGE}/' ${WORKDIR}/boot*.cmd

    uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
        -n "Distro boot script" -d ${WORKDIR}/boot.cmd ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot.scr

    if [ "${UBOOT_SDP_SUPPORT}" = "1" ]; then
        uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
            -n "Recovery boot script" -d ${WORKDIR}/boot-sdp.cmd ${DEPLOYDIR}/boot-sdp.scr-${MACHINE}-${PV}-${PR}
        ln -sf boot-sdp.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot-sdp.scr
    fi
}
do_deploy[depends] = "u-boot-mkimage-native:do_populate_sysroot"

addtask deploy after do_unpack before do_build

PACKAGE_ARCH = "${MACHINE_ARCH}"

# apalis-tk1 will include apalis-tk1-mainline as well
COMPATIBLE_MACHINE = "(apalis-imx6|apalis-imx8|apalis-t30|apalis-tk1|colibri-imx6|colibri-imx7|colibri-imx8x)"
