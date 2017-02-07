require tezi.inc

SUMMARY="Toradex Easy Installer development image"

IMAGE_FSTYPES="tar.bz2 cpio.gz squashfs"
IMAGE_FEATURES+="debug-tweaks ssh-server-openssh"

IMAGE_INSTALL += " \
    ldd \
    strace \
    gdbserver \
    openssh-sftp-server \
    gdb \
"

LICENSE="MIT"

