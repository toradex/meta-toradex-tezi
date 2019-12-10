SUMMARY = "Toradex Easy Installer"
DESCRIPTION = "Toradex Easy Installer for ${MACHINE} machine"
LICENSE = "MIT"

PV = "${TDX_VER_PACKAGE_MIN}"

IMAGE_FSTYPES = "tezirunimg"
IMAGE_FEATURES+="read-only-rootfs"

CORE_IMAGE_BASE_INSTALL_append = " qt-tezi drm-info"

inherit core-image
