setenv bootargs "quiet console=ttyLP1,115200 video=HDMI-A-1:640x480 initcall_blacklist=vpu_driver_init rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}"

# Note: the following memory locations are bound to the ones used to generate
# the boot container image with mkimage_imx8 (see image_type_tezi_run.bbclass).

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Load hdmi firwmware
hdp load 0x82fe0000

# Execute previously loaded FIT image
bootm 0x83000000
