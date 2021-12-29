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
    sed -i 's/@@INITRAMFS_FSTYPES@@/${INITRAMFS_FSTYPES}/' ${WORKDIR}/boot*.cmd
    sed -i 's/@@TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT@@/${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}/' ${WORKDIR}/boot*.cmd

    uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
        -n "Distro boot script" -d ${WORKDIR}/boot.cmd ${DEPLOYDIR}/boot-tezi.scr-${MACHINE}-${PV}-${PR}
    ln -sf boot-tezi.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot-tezi.scr

    if [ "${UBOOT_SDP_SUPPORT}" = "1" ]; then
        uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
            -n "Recovery boot script" -d ${WORKDIR}/boot-sdp.cmd ${DEPLOYDIR}/boot-sdp.scr-${MACHINE}-${PV}-${PR}
        ln -sf boot-sdp.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot-sdp.scr
    fi
}
do_deploy[depends] = "u-boot-mkimage-native:do_populate_sysroot"

addtask deploy after do_unpack before do_build

PACKAGE_ARCH = "${MACHINE_ARCH}"

PROVIDES += "u-boot-default-script"

COMPATIBLE_MACHINE = "(apalis|colibri|verdin)"
