include u-boot-toradex-tezi.inc

SRC_URI_append += " \
	file://0001-Makefile-fix-generation-of-defaultenv.h-from-empty-i.patch 	\
"

SRC_URI_append_apalis-imx8 = " \
    file://0004-apalis-imx8-use-TEZI-distro-boot-script.patch \
"

SRC_URI_append_apalis-imx8x = " \
    file://0005-apalis-imx8x-use-TEZI-distro-boot-script.patch \
"

SRC_URI_append_colibri-imx8x = " \
    file://0003-colibri-imx8x-use-TEZI-distro-boot-script.patch \
"

SRC_URI_append_verdin-imx8mm = " \
    file://0001-verdin-imx8mm-use-TEZI-distro-boot-script.patch \
"

SRC_URI_append_verdin-imx8mp = " \
    file://0002-verdin-imx8mp-use-TEZI-distro-boot-script.patch \
"
