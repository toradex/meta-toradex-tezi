SUMMARY="Toradex Easy Installer"
DESCRIPTION_apalis-imx6="Toradex Easy Installer for Apalis iMX6."
DESCRIPTION_colibri-imx6="Toradex Easy Installer for Colibri iMX6."

IMAGE_NAME_apalis-imx6="Apalis-iMX6_ToradexEasyInstaller_V${PV}_${DATE}"
IMAGE_NAME_colibri-imx6="Colibri-iMX6_ToradexEasyInstaller_V${PV}_${DATE}"

# Never deploy kernel for RAM based file systems
RDEPENDS_kernel-base = ""

IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
    qt-tezi \
    e2fsprogs-mke2fs \
    dosfstools \
    wget \
    xz \
    pv \
    mmc-utils \
    u-boot-toradex-fsl-fw-utils \
"

LICENSE="MIT"

DEPENDS="virtual/bootloader"

do_rootfs[depends] += "u-boot-distro-boot:do_deploy"

inherit core-image

