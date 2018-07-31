setenv bootargs console=ttymxc0,115200 quiet rootfstype=squashfs root=/dev/ram autoinstall clk_ignore_unused

# Execute previously loaded FIT image
bootm 0x82100000
