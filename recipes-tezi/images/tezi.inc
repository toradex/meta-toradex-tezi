SUMMARY="Toradex Easy Installer initramfs"

# We are using the rootfs as initramfs
IMAGE_FSTYPES="cpio.gz squashfs"

# Never deploy kernel for RAM based file systems
RDEPENDS_kernel-base = ""

IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
    qt-tezi \
    parted \
    util-linux-sfdisk \
    e2fsprogs-mke2fs \
    dosfstools \
    wget \
    xz \
"

LICENSE="MIT"

inherit core-image

