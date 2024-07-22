SUMMARY = "Boot script for launching TEZI with U-Boot distro boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "file://boot.cmd.in"

DTB_PREFIX ??= "${@d.getVar('KERNEL_DTB_PREFIX').replace("/", "_") if d.getVar('KERNEL_DTB_PREFIX') else ''}"

inherit deploy nopackages

do_deploy () {
    sed 's/@@INITRAMFS_FSTYPES@@/${INITRAMFS_FSTYPES}/' ${WORKDIR}/boot.cmd.in > boot.cmd
    sed -i 's/@@TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT@@/${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}/' boot.cmd
    sed -i 's/@@KERNEL_DTB_PREFIX@@/${DTB_PREFIX}/' boot.cmd

    uboot-mkimage -T script -C none -a 0 -e 0 \
        -n "TEZI distro boot script" -d boot.cmd ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot.scr
}

addtask deploy after do_install before do_build

PROVIDES += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE:k3r5 = "(^$)"
