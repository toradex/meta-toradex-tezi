require tezi.inc

# Image used for development
IMAGE_FEATURES+="debug-tweaks ssh-server-openssh"

IMAGE_INSTALL += " \
    ldd \
    strace \
    gdbserver \
    openssh-sftp-server \
"

LICENSE="MIT"

