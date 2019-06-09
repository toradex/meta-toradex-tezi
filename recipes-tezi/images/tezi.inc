SUMMARY="Toradex Easy Installer"
LICENSE="MIT"

PV = "${TDX_VER_PACKAGE_MIN}"

DESCRIPTION_apalis-imx6="Toradex Easy Installer for Apalis iMX6."
DESCRIPTION_apalis-imx8="Toradex Easy Installer for Apalis iMX8."
DESCRIPTION_apalis-t30-mainline="Toradex Easy Installer for Apalis T30."
DESCRIPTION_apalis-tk1="Toradex Easy Installer for Apalis TK1."
DESCRIPTION_apalis-tk1-mainline="Toradex Easy Installer for Apalis TK1."
DESCRIPTION_colibri-imx6="Toradex Easy Installer for Colibri iMX6."
DESCRIPTION_colibri-imx6ull="Toradex Easy Installer for Colibri iMX6ULL."
DESCRIPTION_colibri-imx7="Toradex Easy Installer for Colibri iMX7."
DESCRIPTION_colibri-imx7-emmc="Toradex Easy Installer for Colibri iMX7 eMMC."

IMAGE_NAME_apalis-imx6="Apalis-iMX6_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
IMAGE_NAME_apalis-imx8="Apalis-iMX8_ToradexEasyInstaller_${TDX_VER_PACKAGE}"
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
TORADEX_FW_UTILS = "u-boot-toradex-fsl-fw-utils"
TORADEX_FW_UTILS_apalis-imx8 = "u-boot-toradex-fw-utils"

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
    ${TORADEX_FW_UTILS} \
    weston \
    openssl-bin \
    haveged \
"

IMAGE_INSTALL_append_colibri-imx6ull = " \
    ${RAW_NAND_UTILS} \
"

IMAGE_INSTALL_append_colibri-imx7 = " \
    ${RAW_NAND_UTILS} \
"

inherit core-image

add_rootfs_tezi_version () {
    echo ${IMAGE_NAME} > ${IMAGE_ROOTFS}${sysconfdir}/tezi-version
}

# add the rootfs version
ROOTFS_POSTPROCESS_COMMAND += " add_rootfs_tezi_version;"
