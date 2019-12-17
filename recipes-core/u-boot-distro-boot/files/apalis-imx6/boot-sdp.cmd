setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480D video=HDMI-A-1:640x480-16@60D video=LVDS-1:d video=VGA-1:d rootfstype=@@TEZI_INITRD_IMAGE@@ root=/dev/ram autoinstall

# Execute previously loaded FIT image
bootm 0x12100000
