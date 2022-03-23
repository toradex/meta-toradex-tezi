# Start from a known environment
# This is possible here, since apalis-imx6 has no variants that need dynamically set variables to boot.
env default -a

env set ramdisk_addr_r 0x12100000
setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480M@60D video=HDMI-A-1:640x480M@60D rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall

env set fdt_high
env set fdt_resize true
env set fitconf_fdt_overlays

# Recovery-mode: load overlays.txt from the address provided by uuu utility
env set set_load_overlays_file 'env set load_overlays_file "env import -t 0x14100000 0x200"'
env set set_apply_overlays 'env set apply_overlays "for overlay_file in \"\\${fdt_overlays}\"; do env set fitconf_fdt_overlays \"\\"\\${fitconf_fdt_overlays}#conf-\\${overlay_file}\\"\"; env set overlay_file; done; true"'

env set bootcmd_run 'echo "Bootargs: \${bootargs}" && bootm ${ramdisk_addr_r}#conf-${fdtfile}\${fitconf_fdt_overlays}'

run set_load_overlays_file
run set_apply_overlays

run load_overlays_file
run apply_overlays

# Execute previously loaded FIT image
run bootcmd_run
