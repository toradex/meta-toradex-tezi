# verdin-imx8mp hardware setup

env set bootcmd_hdp ';'
env set fdtfile ${fdt_prefix}imx8mp-verdin-wifi-dev.dtb
env set ramdisk_addr_r 0x46400000
env set vidargs 'video=HDMI-A-1:1280x720-16@60D video=HDMI-A-2:1280x720-16@60D'

env set bootcmd_args 'env set bootargs quiet ${vidargs} initcall_blacklist=vpu_driver_init rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall clk_ignore_unused pci=nomsi ${teziargs}'
env set bootcmd_run 'bootm ${ramdisk_addr_r}#config@freescale_${fdtfile}'
env set bootcmd 'run bootcmd_args && run bootcmd_hdp && run bootcmd_tezi && run bootcmd_run'
env set tezi_image ${prefix}tezi.itb

# Re-enable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
env set fdt_high

if test -n ${fastboot_buffer}
then
    # Recovery mode
    # Note: the memory locations are bound to the ones as used in uuu.auto

    env set bootcmd_tezi ';'
else
    env set bootcmd_tezi 'load ${devtype} ${devnum}:${distro_bootpart} ${ramdisk_addr_r} ${tezi_image}'
fi

run bootcmd
