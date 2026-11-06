SUMMARY = "Boot script for launching TEZI with U-Boot distro boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "u-boot-mkimage-native"
INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = "file://boot.cmd.in file://boot.cmd.in-NAND"

S = "${UNPACKDIR}"

inherit deploy nopackages

TEZI_DP_MODE = "1280x720D"

TEZI_DPI_MODE = "640x480e"

TEZI_HDMI_MODE = "1280x720D"
TEZI_HDMI_MODE:apalis-imx6 = "640x480D"
TEZI_HDMI_MODE:apalis-imx8 = "640x480D"
TEZI_HDMI_MODE:colibri-imx6 = "640x480D"
TEZI_HDMI_MODE:verdin-am62 = "640x480D"

TEZI_VGA_MODE = "640x480e"

TEZI_BOOT_ARGS = "video=DP-1:${TEZI_DP_MODE} video=DPI-1:${TEZI_DPI_MODE} \
                  video=DVI-A-1:${TEZI_VGA_MODE} \
                  video=HDMI-A-1:${TEZI_HDMI_MODE} video=HDMI-A-2:${TEZI_HDMI_MODE} \
                  video=LVDS-1:d video=VGA-1:${TEZI_VGA_MODE} \
                  drm.edid_firmware=DP-1:edid/tezi.bin,HDMI-A-1:edid/tezi.bin,HDMI-A-2:edid/tezi.bin"

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
TEZI_OVERLAY_ADDR:mx93-generic-bsp = "0x8a900000"
TEZI_OVERLAY_ADDR:mx95-generic-bsp = "0x9c800000"
TEZI_OVERLAY_ADDR:verdin-imx8mm = "0x42e10000"

BOOT_CMD_FILE = "boot.cmd.in"
BOOT_CMD_FILE:colibri-imx6ull = "boot.cmd.in-NAND"
BOOT_CMD_FILE:colibri-imx7 = "boot.cmd.in-NAND"

FITCONF_FDT_OVERLAYS ??= ""
TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT ??= ""

# Mirror the config-node prefix kernel-fit-image.bbclass adds when
# KERNEL_DTBVENDORED = "1", so the requested FIT config name matches.
DTB_PREFIX ??= "${@d.getVar('KERNEL_DTB_PREFIX').replace("/", "_") if d.getVar('KERNEL_DTB_PREFIX') and d.getVar('KERNEL_DTBVENDORED') == '1' else ''}"

do_compile() {
    sed -e 's|@@INITRAMFS_FSTYPES@@|${INITRAMFS_FSTYPES}|' \
        -e 's|@@TEZI_BOOT_ARGS@@|${TEZI_BOOT_ARGS}|' \
        -e 's|@@TEZI_FITIMAGE_ADDR@@|${TEZI_FITIMAGE_ADDR}|' \
        -e 's|@@TEZI_OVERLAY_ADDR@@|${TEZI_OVERLAY_ADDR}|' \
        -e 's|@@TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT@@|${TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT}|' \
        -e 's|@@FITCONF_FDT_OVERLAYS@@|${FITCONF_FDT_OVERLAYS}|' \
        -e 's|@@KERNEL_DTB_PREFIX@@|${DTB_PREFIX}|' \
        "${S}/${BOOT_CMD_FILE}" > boot.cmd
}

do_deploy() {
    uboot-mkimage -T script -C none -a 0 -e 0 \
        -n "TEZI distro boot script" -d boot.cmd ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} ${DEPLOYDIR}/boot.scr
}

addtask deploy after do_install before do_build

PROVIDES += "u-boot-default-script"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE:k3r5 = "(^$)"
