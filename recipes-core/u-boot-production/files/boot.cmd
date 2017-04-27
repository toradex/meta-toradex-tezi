setenv bootargs console=ttymxc0,115200 init=/init video=mxcfb0:dev=hdmi,bpp=32,640x480M@60,if=RGB24 video=mxcfb1:dev=lcd,bpp=32,EDT-VGA,if=RGB24 video=mxcfb2:dev=vdac,bpp=32,640x480@60,if=RGB565 video=mxcfb3:dev=ldb,bpp=32 rootfstype=squashfs root=/dev/ram consoleblank=0 vt.global_cursor_default=0 ${defargs}

load ${devtype} ${devnum}:${bootpart} ${kernel_addr_r} ${prefix}zImage
load ${devtype} ${devnum}:${bootpart} ${fdt_addr_r} ${prefix}${fdt_file}
load ${devtype} ${devnum}:${bootpart} ${ramdisk_addr_r} ${prefix}tester-apalis-imx6.squashfs                                                                                     
bootz ${kernel_addr_r} ${ramdisk_addr_r}:${filesize} ${fdt_addr_r}
