include conf/tdx_version.conf

SUMMARY="Toradex Easy Installer"
DESCRIPTION_apalis-imx6="Toradex Easy Installer for Apalis iMX6."
DESCRIPTION_colibri-imx6="Toradex Easy Installer for Colibri iMX6."
DESCRIPTION_colibri-imx7="Toradex Easy Installer for Colibri iMX7."
DESCRIPTION_colibri-imx7-emmc="Toradex Easy Installer for Colibri iMX7 eMMC."

IMAGE_NAME_apalis-imx6="Apalis-iMX6_ToradexEasyInstaller_${TDX_VER_EXT}"
IMAGE_NAME_colibri-imx6="Colibri-iMX6_ToradexEasyInstaller_${TDX_VER_EXT}"
IMAGE_NAME_colibri-imx7="Colibri-iMX7_ToradexEasyInstaller_${TDX_VER_EXT}"
IMAGE_NAME_colibri-imx7-emmc="Colibri-iMX7-eMMC_ToradexEasyInstaller_${TDX_VER_EXT}"

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

IMAGE_INSTALL_append_colibri-imx7 = " \
    mtd-utils-ubifs \
    mtd-utils \
"

LICENSE="MIT"

DEPENDS="virtual/bootloader"

inherit core-image

