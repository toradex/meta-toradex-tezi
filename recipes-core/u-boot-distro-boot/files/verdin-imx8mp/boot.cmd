# verdin-imx8mp hardware setup

env set bootcmd_hdp ';'
env set fdtfile ${fdt_prefix}imx8mp-verdin-wifi-dev.dtb
env set ramdisk_addr_r 0x47400000
env set vidargs 'drm.edid_firmware=edid/1280x720.bin video=HDMI-A-1:1280x720-16@60D video=HDMI-A-2:1280x720-16@60D'

env set bootcmd_args 'env set bootargs quiet ${vidargs} initcall_blacklist=vpu_driver_init rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall clk_ignore_unused pci=nomsi ${teziargs}'

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

# Load user specified overlays from eMMC partition (file overlays.txt)

test -n ${m4boot} || env set m4boot ';'
test -n ${boot_part} || env set boot_part ${distro_bootpart}
test -n ${root_part} || env set root_part 2
test -n ${boot_devnum} || env set boot_devnum ${devnum}
test -n ${root_devnum} || env set root_devnum ${devnum}
test -n ${kernel_image} || env set kernel_image Image.gz
test -n ${boot_devtype} || env set boot_devtype ${devtype}
test -n ${overlays_file} || env set overlays_file "overlays.txt"
test -n ${overlays_prefix} || env set overlays_prefix "overlays/"

test ${boot_devtype} = "mmc" && env set load_cmd 'load ${boot_devtype} ${boot_devnum}:${boot_part}'
test ${boot_devtype} = "usb" && env set load_cmd 'load ${boot_devtype} ${boot_devnum}:${boot_part}'
test ${boot_devtype} = "tftp" && env set load_cmd 'tftp'
test ${boot_devtype} = "dhcp" && env set load_cmd 'dhcp'

env set fdt_high
env set fdt_resize true
env set fitconf_fdt_overlays

env set set_default_overlays 'env set fdt_overlays "@@TEZI_EXTERNAL_KERNEL_DEVICETREE_BOOT@@"'

if test -n ${devtype}
then
       # We have devtype, devnum and boot_part defined for boot from USB and eMMC
       env set set_load_overlays_file 'env set load_overlays_file "${load_cmd} \\${loadaddr} \\${overlays_file} && env import -t \\${loadaddr} \\${filesize}; test -n \\${fdt_overlays} || run set_default_overlays"'
else
       # Recovery-mode: load overlays.txt from the address provided by uuu utility
       env set set_load_overlays_file 'env set load_overlays_file "env import -t 0x42e10000 0x200"'
fi

env set set_apply_overlays 'env set apply_overlays "for overlay_file in \"\\${fdt_overlays}\"; do env set fitconf_fdt_overlays \"\\"\\${fitconf_fdt_overlays}#conf-\\${overlay_file}\\"\"; env set overlay_file; done; true"'

env set bootcmd_run 'echo "Bootargs: \${bootargs}" && bootm ${ramdisk_addr_r}#conf-freescale_\${fdtfile}\${fitconf_fdt_overlays}'

run set_load_overlays_file
run set_apply_overlays

run load_overlays_file
run apply_overlays

run bootcmd
