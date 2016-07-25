setenv bootargs console=ttymxc0,115200 quiet init=/init video=mxcfb0:dev=hdmi,640x480M@60,if=RGB24 video=mxcfb1:dev=lcd,EDT-VGA,if=RGB24 video=mxcfb2:off video=mxcfb3:off rootfstype=squashfs root=/dev/ram autoinstall
load ${devtype} ${devnum}:${bootpart} ${kernel_addr_r} ${prefix}zImage
load ${devtype} ${devnum}:${bootpart} ${fdt_addr_r} ${prefix}${fdt_file}
load ${devtype} ${devnum}:${bootpart} ${ramdisk_addr_r} ${prefix}tezi-apalis-imx6.squashfs

bootz ${kernel_addr_r} ${ramdisk_addr_r}:${filesize} ${fdt_addr_r}
