require tezi-run.bb

SUMMARY = "Toradex Easy Installer development image"

IMAGE_FSTYPES = "tar.bz2 cpio.gz ${TEZI_INITRD_IMAGE}"
IMAGE_FEATURES += "debug-tweaks ssh-server-openssh"

CORE_IMAGE_BASE_INSTALL_append = " \
    ldd \
    strace \
    gdbserver \
    openssh-sftp-server \
    gdb \
    drm-info \
"
