SUMMARY = "Toradex Easy Installer"
DESCRIPTION = "Toradex Easy Installer for ${MACHINE} machine"
LICENSE = "MIT"

PV = "${TDX_VERSION}"

IMAGE_FSTYPES = "${INITRAMFS_FSTYPES}"
IMAGE_FSTYPES_remove ="teziimg"
IMAGE_FEATURES += "read-only-rootfs"

CORE_IMAGE_BASE_INSTALL_append = " qt-tezi qt-tezictl drm-info udev-toradex-rules"

add_rootfs_version () {
    printf "${DISTRO_NAME} ${DISTRO_VERSION} (${DISTRO_CODENAME}) \n" > ${IMAGE_ROOTFS}/etc/issue
}
# add the rootfs version to the welcome banner
ROOTFS_POSTPROCESS_COMMAND += " add_rootfs_version;"

inherit core-image
