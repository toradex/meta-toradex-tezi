setenv bootargs console=ttymxc0,115200 quiet video=mxcfb0:dev=hdmi,640x480M@60,if=RGB24 video=mxcfb1:dev=lcd,640x480M@60,if=RGB666 rootfstype=squashfs root=/dev/ram autoinstall

# Execute previously loaded FIT image
bootm ${ramdisk_addr_r}