setenv bootargs console=ttymxc0,115200 quiet init=/init video=mxcfb0:dev=hdmi,640x480M@60,if=RGB24 video=mxcfb1:dev=lcd,EDT-VGA,if=RGB24 video=mxcfb2:dev=vdac,640x480@60,if=RGB565 video=mxcfb3:off rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}

load ${devtype} ${devnum}:${bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

bootm ${ramdisk_addr_r}