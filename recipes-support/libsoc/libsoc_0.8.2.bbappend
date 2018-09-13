FILESEXTRAPATHS_prepend := "${THISDIR}/libsoc:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRCREV = "9a97d260379856940a5de9862ef06803de82cca8"

SRC_URI += " \
        file://0001-libsoc-use-sane-board-naming.patch \
"

BOARD_apalis-imx6 = "apalis-imx6"
BOARD_apalis-t30 = "apalis-t30"
BOARD_apalis-tk1 = "apalis-tk1"
BOARD_apalis-tk1-mainline = "apalis-tk1"
BOARD_colibri-imx6 = "colibri-imx6"
BOARD_colibri-imx6ull = "colibri-imx6ull"
BOARD_colibri-imx7 = "colibri-imx7"
BOARD_colibri-imx7-emmc = "colibri-imx7-emmc"
BOARD_colibri-t20 = "colibri-t20"
BOARD_colibri-t30 = "colibri-t30"
BOARD_colibri-vf = "colibri-vf"

PACKAGECONFIG_apalis-imx6 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_apalis-t30 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_apalis-tk1 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_apalis-tk1_mainline = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-imx6 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-imx6ull = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-imx7 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-imx7-emmc = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-t20 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-t30 = "allboardconfigs enableboardconfig python"
PACKAGECONFIG_colibri-vf = "allboardconfigs enableboardconfig python"

pkg_postinst_${PN}_colibri-imx6ull () {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    IS_WIFI_DTB=`grep -c toradex,colibri_imx6ull-wifi /proc/device-tree/compatible`
    if [ $IS_WIFI_DTB -gt 0 ]; then
        mv -f ${datadir}/libsoc/colibri-imx6ull.conf ${datadir}/libsoc/colibri-imx6ull.conf.bak
        # SODIMM pins missing on Wi-Fi SKU
	cat ${datadir}/libsoc/colibri-imx6ull.conf.bak | grep -E -v "(SODIMM_79|SODIMM_81|SODIMM_89|SODIMM_93|SODIMM_94|SODIMM_97|SODIMM_101|SODIMM_103|SODIMM_127|SODIMM_138)" > ${datadir}/libsoc/colibri-imx6ull.conf
        rm -f ${datadir}/libsoc/colibri-imx6ull.conf.bak
    fi
}
