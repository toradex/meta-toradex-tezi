require tezi-run.bb

inherit populate_sdk_qt5

IMAGE_FSTYPES = "tar.bz2 cpio.gz ${INITRAMFS_FSTYPES}"
IMAGE_FEATURES += "debug-tweaks ssh-server-openssh"

CORE_IMAGE_BASE_INSTALL_append = " \
    ldd \
    strace \
    gdbserver \
    openssh-sftp-server \
    gdb \
"
