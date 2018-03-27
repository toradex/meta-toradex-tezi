setenv bootargs lp0_vec=2064@0xf46ff000 core_edp_mv=1150 core_edp_ma=4000 lane_owner_info=6 usb_port_owner_info=2 emc_max_dvfs=0 igb_mac=${ethaddr} console=ttyS0,115200 quiet video=tegrafb0:640x480-16@60 fbcon=map:1 rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}

# Re-enable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load FIT image from location as detected by distroboot
load ${devtype} ${devnum}:${bootpart} ${ramdisk_addr_r} ${prefix}tezi.itb

bootm ${ramdisk_addr_r}#config@${fdt_module}
