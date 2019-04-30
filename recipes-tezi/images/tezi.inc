include conf/tdx_version.conf

SUMMARY="Toradex Easy Installer"
DESCRIPTION_apalis-imx6="Toradex Easy Installer for Apalis iMX6."
DESCRIPTION_apalis-t30-mainline="Toradex Easy Installer for Apalis T30."
DESCRIPTION_apalis-tk1="Toradex Easy Installer for Apalis TK1."
DESCRIPTION_apalis-tk1-mainline="Toradex Easy Installer for Apalis TK1."
DESCRIPTION_colibri-imx6="Toradex Easy Installer for Colibri iMX6."
DESCRIPTION_colibri-imx6ull="Toradex Easy Installer for Colibri iMX6ULL."
DESCRIPTION_colibri-imx7="Toradex Easy Installer for Colibri iMX7."
DESCRIPTION_colibri-imx7-emmc="Toradex Easy Installer for Colibri iMX7 eMMC."

IMAGE_NAME_apalis-imx6="Apalis-iMX6_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_apalis-t30-mainline="Apalis-T30_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_apalis-tk1="Apalis-TK1_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_apalis-tk1-mainline="Apalis-TK1_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_colibri-imx6="Colibri-iMX6_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_colibri-imx6ull="Colibri-iMX6ULL_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_colibri-imx7="Colibri-iMX7_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_colibri-imx7-emmc="Colibri-iMX7-eMMC_ToradexEasyInstaller_${TDX_VER_PACKAGE}"

RAW_NAND_UTILS = " \
    mtd-utils-ubifs \
    mtd-utils \
    kobs-ng \
"

IMAGE_INSTALL += " \
    ${CORE_IMAGE_BASE_INSTALL} \
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
    u-boot-toradex-fsl-fw-utils \
    weston \
    openssl-bin \
"

IMAGE_INSTALL_append_colibri-imx6ull = " \
    ${RAW_NAND_UTILS} \
"

IMAGE_INSTALL_append_colibri-imx7 = " \
    ${RAW_NAND_UTILS} \
"

MACHINE_EXTRA_RDEPENDS_remove_apalis-tk1 = " \
    tdx-k20-fw \
"
MACHINE_EXTRA_RDEPENDS_remove_apalis-tk1-mainline = " \
    tdx-k20-fw \
"

LICENSE="MIT"

inherit core-image

add_rootfs_tezi_version () {
    echo ${IMAGE_NAME} > ${IMAGE_ROOTFS}${sysconfdir}/tezi-version
}

# add the rootfs version
ROOTFS_POSTPROCESS_COMMAND += " add_rootfs_tezi_version;"

