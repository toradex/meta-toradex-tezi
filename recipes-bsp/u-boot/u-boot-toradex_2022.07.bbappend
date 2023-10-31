FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require u-boot-toradex-tezi.inc

# xxd tool is used in U-boot Makefile when
# CONFIG_USE_DEFAULT_ENV_FILE U-boot option enabled.
DEPENDS += "xxd-native"
