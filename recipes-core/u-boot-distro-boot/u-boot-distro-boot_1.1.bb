SUMMARY = "Boot script for launching TEZI with U-Boot distro boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "file://boot.cmd.in file://boot.cmd.in-NAND"

DTB_PREFIX ??= "${@d.getVar('KERNEL_DTB_PREFIX').replace("/", "_") if d.getVar('KERNEL_DTB_PREFIX') else ''}"

inherit deploy nopackages

TEZI_BOOT_ARGS = ""
TEZI_BOOT_ARGS:apalis-imx6 = "video=DPI-1:640x480D video=HDMI-A-1:640x480-16@60D video=LVDS-1:d video=VGA-1:640x480-16@60D"
TEZI_BOOT_ARGS:apalis-imx8 = "video=LVDS-1:d video=HDMI-A-1:640x480-16@60D"
TEZI_BOOT_ARGS:aquila-am69 = "video=DP-1:1280x720@60D"
TEZI_BOOT_ARGS:aquila-imx95 = "video=DP-1:1280x720@60D"
TEZI_BOOT_ARGS:colibri-imx6 = "video=DPI-1:640x480-16@60D video=HDMI-A-1:640x480-16@60D"
TEZI_BOOT_ARGS:colibri-imx6ull = "video=DPI-1:640x480@60D"
TEZI_BOOT_ARGS:colibri-imx7 = "video=DPI-1:640x480@60D"
TEZI_BOOT_ARGS:colibri-imx8x = "video=DPI-1:640x480-16@60D"
TEZI_BOOT_ARGS:lino-imx93 = "video=HDMI-A-1:1280x720-16@60D"
TEZI_BOOT_ARGS:toradex-osm-imx93 = "video=HDMI-A-1:1280x720-16@60D"
TEZI_BOOT_ARGS:toradex-smarc-imx8mp = "video=HDMI-A-1:1280x720-16@60D"
TEZI_BOOT_ARGS:toradex-smarc-imx95 = "video=DP-1:1280x720@60D"
TEZI_BOOT_ARGS:verdin-am62 = "video=HDMI-A-1:640x480-16@60D"
TEZI_BOOT_ARGS:verdin-am62p = "video=HDMI-A-1:1280x720-16@60D"
TEZI_BOOT_ARGS:verdin-imx8mm = "video=HDMI-A-1:1280x720-16@60D"
TEZI_BOOT_ARGS:verdin-imx8mp = "video=HDMI-A-1:1280x720-16@60D video=HDMI-A-2:1280x720-16@60D"
TEZI_BOOT_ARGS:verdin-imx95 = "video=HDMI-A-1:1280x720-16@60D"

# As default, keep the ramdisk_addr_r and loadaddr from u-boot for the fitimage
# and overlay files. This way TI machines can use this as default value.
TEZI_FITIMAGE_ADDR = "${ramdisk_addr_r}"
TEZI_OVERLAY_ADDR = "${loadaddr}"
# These are handcrafted values that make sure the tezi.itb file can be loaded
# to RAM without corrputing or overlaping any other files in memory.  Values
# not described here will use the ramdisk_add_r instead, which (in these cases)
# are working fine.
TEZI_FITIMAGE_ADDR:apalis-imx6 = "0x14420000"
TEZI_FITIMAGE_ADDR:apalis-imx8 = "0x8a000000"
TEZI_FITIMAGE_ADDR:colibri-imx6 = "0x14420000"
TEZI_FITIMAGE_ADDR:colibri-imx6ull = "0x81100000"
TEZI_FITIMAGE_ADDR:colibri-imx7 = "0x81100000"
TEZI_FITIMAGE_ADDR:colibri-imx8x = "0x8a000000"
TEZI_FITIMAGE_ADDR:mx8mp-generic-bsp = "0x44200000"
TEZI_FITIMAGE_ADDR:mx93-generic-bsp = "0x82800000"
TEZI_FITIMAGE_ADDR:mx95-generic-bsp = "0x90400000"
TEZI_FITIMAGE_ADDR:verdin-imx8mm = "0x44200000"

TEZI_OVERLAY_ADDR:apalis-imx6 = "0x14410000"
TEZI_OVERLAY_ADDR:apalis-imx8 = "0x82e10000"
TEZI_OVERLAY_ADDR:colibri-imx6 = "0x14410000"
TEZI_OVERLAY_ADDR:colibri-imx6ull = "0x80f00000"
TEZI_OVERLAY_ADDR:colibri-imx7 = "0x80f00000"
TEZI_OVERLAY_ADDR:colibri-imx8x = "0x82e10000"
TEZI_OVERLAY_ADDR:mx8mp-generic-bsp = "0x42e10000"
TEZI_OVERLAY_ADDR:mx93-generic-bsp = "0x9c800000"
TEZI_OVERLAY_ADDR:mx95-generic-bsp = "0x9c800000"
TEZI_OVERLAY_ADDR:verdin-imx8mm = "0x42e10000"

BOOT_CMD_FILE = "boot.cmd.in"
BOOT_CMD_FILE:colibri-imx6ull = "boot.cmd.in-NAND"
BOOT_CMD_FILE:colibri-imx7 = "boot.cmd.in-NAND"

do_deploy () {
    sed 's/@@INITRAMFS_FSTYPES@@/${INITRAMFS_FSTYPES}/' ${WORKDIR}/${BOOT_CMD_FILE} > boot.cmd
    sed -i 's/@@TEZI_BOOT_ARGS@@/${TEZI_BOOT_ARGS}/' boot.cmd
    sed -i 's/@@TEZI_FITIMAGE_ADDR@@/${TEZI_FITIMAGE_ADDR}/' boot.cmd
    sed -i 's/@@TEZI_OVERLAY_ADDR@@/${TEZI_OVERLAY_ADDR}/' boot.cmd
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
