setenv bootargs console=ttymxc0,115200 quiet rootfstype=squashfs root=/dev/ram autoinstall

# Execute previously loaded FIT image, choose configuration depending on runtime SoC detection
bootm ${ramdisk_addr_r}#config@${soc}${variant}
