setenv bootargs console=ttymxc0,115200 quiet rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}

load ${devtype} ${devnum}:${bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

bootm ${ramdisk_addr_r}
