require tezi-run.bb

inherit populate_sdk_qt5

IMAGE_FSTYPES = "tar.bz2 cpio.gz ${INITRAMFS_FSTYPES}"
IMAGE_FEATURES += "empty-root-password allow-empty-password allow-root-login ssh-server-openssh"

CORE_IMAGE_BASE_INSTALL:append = " \
    ldd \
    strace \
    gdbserver \
    openssh-sftp-server \
    gdb \
"
