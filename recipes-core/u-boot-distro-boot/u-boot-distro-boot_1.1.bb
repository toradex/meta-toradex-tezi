SUMMARY = "Boot script for launching TEZI with U-Boot distro boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = " \
    file://boot.cmd.in \
    ${@oe.utils.ifelse(d.getVar('UBOOT_SDP_SUPPORT') == '1', 'file://boot-sdp.cmd.in', '')} \
"

inherit deploy nopackages

do_deploy () {
    for fin in ${WORKDIR}/boot*.cmd.in
    do
        fout="$(basename $fin .in)"
        sed 's/@@INITRAMFS_FSTYPES@@/${INITRAMFS_FSTYPES}/' $fin > $fout
        sed -i 's/@@TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT@@/${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}/' $fout
    done

    uboot-mkimage -T script -C none -a 0 -e 0 \
        -n "TEZI distro boot script" -d boot.cmd ${DEPLOYDIR}/boot-tezi.scr-${MACHINE}-${PV}-${PR}
    ln -sf boot-tezi.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot-tezi.scr

    if [ -f boot-sdp.cmd ]; then
        uboot-mkimage -T script -C none -a 0 -e 0 \
            -n "TEZI recovery boot script" -d boot-sdp.cmd ${DEPLOYDIR}/boot-sdp.scr-${MACHINE}-${PV}-${PR}
        ln -sf boot-sdp.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot-sdp.scr
    fi
}
do_deploy:append:am62xx () {
    ln -sf boot-tezi.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot.scr
}

addtask deploy after do_install before do_build

PROVIDES += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"
