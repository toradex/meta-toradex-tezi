SUMMARY = "Toradex Easy Installer"
DESCRIPTION = "Toradex Easy Installer for ${MACHINE} machine"
LICENSE = "MIT"

PV = "${TDX_VER_PACKAGE_MIN}"

IMAGE_FSTYPES = "tezirunimg"
IMAGE_FEATURES+="read-only-rootfs"

CORE_IMAGE_BASE_INSTALL_append = " \
    qt-tezi \
    e2fsprogs-mke2fs \
    e2fsprogs-tune2fs \
    dosfstools \
    curl \
    lz4 \
    lzop \
    xz \
    tar \
    pv \
    mmc-utils \
    u-boot-fw-utils \
    weston \
    openssl-bin \
    haveged \
    tezi-version \
    avahi-daemon \
"

inherit core-image
