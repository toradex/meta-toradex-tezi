setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480-16@60D rootfstype=@@TEZI_INITRD_IMAGE@@ root=/dev/ram autoinstall clk_ignore_unused

# Execute previously loaded FIT image
bootm 0x82100000
