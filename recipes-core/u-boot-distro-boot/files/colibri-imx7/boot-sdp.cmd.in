setenv bootargs console=ttymxc0,115200 quiet video=DPI-1:640x480-16@60D rootfstype=@@INITRAMFS_FSTYPES@@ root=/dev/ram autoinstall

# Execute previously loaded FIT image, choose configuration only from variables
# which are not influenced by the environment
bootm 0x82100000#conf-${soc}-colibri${variant}-eval-v3.dtb
