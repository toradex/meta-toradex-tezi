SUMMARY="Toradex Easy Installer initramfs"

# We are using the rootfs as initramfs
IMAGE_FSTYPES="cpio.gz squashfs"

# Never deploy kernel for RAM based file systems
RDEPENDS_kernel-base = ""

IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
    qt-tezi \
    util-linux-sfdisk \
    e2fsprogs-mke2fs \
    dosfstools \
    wget \
    xz \
    pv \
"

LICENSE="MIT"

do_rootfs[depends] += "u-boot-distro-boot:do_deploy"

inherit core-image

