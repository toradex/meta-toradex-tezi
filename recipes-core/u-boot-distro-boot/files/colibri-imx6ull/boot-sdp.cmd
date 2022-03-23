setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480-16@60D rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall clk_ignore_unused

# Execute previously loaded FIT image, choose configuration only from variables
# which are not influenced by the environment
bootm 0x82100000#conf-imx6ull-colibri${variant}-eval-v3.dtb
