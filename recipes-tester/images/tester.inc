IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
    dosfstools \
    mmc-utils \
    spitools \
    pciutils \
    i2c-tools \
    tester-scripts \
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

