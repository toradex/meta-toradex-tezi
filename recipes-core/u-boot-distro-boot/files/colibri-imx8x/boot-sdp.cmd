setenv bootargs "quiet console=ttyLP3,115200 video=VGA-1:640x480 initcall_blacklist=vpu_driver_init rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}"

# Reenable fdt relocation since in place fdt edits corrupt the ramdisk
# in a FIT image...
setenv fdt_high

# Note: the following memory locations are bound to the ones used to generate
# the boot container image with im8_mkimage.

# Execute previously loaded FIT image
bootm 0x83000000
