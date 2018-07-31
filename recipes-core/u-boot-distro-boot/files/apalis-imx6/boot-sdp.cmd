setenv bootargs console=ttymxc0,115200 quiet video=mxcfb0:dev=hdmi,640x480@60,if=RGB24 video=mxcfb1:dev=lcd,640x480@60,if=RGB24 video=mxcfb2:dev=vdac,640x480@60,if=RGB565 video=mxcfb3:off rootfstype=squashfs root=/dev/ram autoinstall

# Execute previously loaded FIT image
bootm 0x12100000
