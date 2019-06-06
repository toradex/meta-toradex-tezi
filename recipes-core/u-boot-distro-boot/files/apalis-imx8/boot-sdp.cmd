setenv bootargs "console=ttyLP1,115200 earlycon=lpuart32,0x5a070000,115200,115200 video=HDMI-A-1:640x480 video=imxdpufb5:off video=imxdpufb6:off video=imxdpufb7:off initcall_blacklist=vpu_driver_init rootfstype=squashfs root=/dev/ram autoinstall ${teziargs}"

# Note: the following memory locations are bound to the ones used to generate
# the boot container image with im8_mkimage.

# Load hdmi firwmware
hdp load 0x82fe0000
# Execute previously loaded FIT image
bootm 0x83000000
