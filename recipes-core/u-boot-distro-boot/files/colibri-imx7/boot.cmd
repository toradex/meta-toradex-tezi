setenv bootargs console=ttymxc0,115200 quiet rootfstype=@@TEZI_INITRD_IMAGE@@ root=/dev/ram autoinstall ${teziargs}

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load FIT image from location as detected by distroboot
load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

bootm ${ramdisk_addr_r}#config@${soc}${variant}
