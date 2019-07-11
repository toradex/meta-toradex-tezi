setenv bootargs "quiet console=ttyLP3,115200 video=HDMI-A-1:640x480 rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}"

# Set address outside the range where FIT Image is extracted
setenv ramdisk_addr_r 0x86400000

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load FIT Image and boot
fatload mmc ${mmcdev}:${mmcpart} ${ramdisk_addr_r} tezi.itb
bootm ${ramdisk_addr_r}

# TODO: modify when distroboot is set by default
# Load FIT image from location as detected by distroboot
# load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb
