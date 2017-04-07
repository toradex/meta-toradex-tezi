IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
    alsa-utils \
    dosfstools \
    mmc-utils \
    spitools \
    pciutils \
    i2c-tools \
    tester-scripts \
    tdxtester \
    util-linux \
    util-linux-sfdisk \
    ldd \
    strace \
"

LICENSE="MIT"

BBMASK="qt4-embedded_%.bbappend"

inherit core-image

# Never deploy kernel for RAM based file systems
RDEPENDS_kernel-base = ""

