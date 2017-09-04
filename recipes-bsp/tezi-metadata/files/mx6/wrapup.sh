#!/bin/sh
#
# (c) Toradex AG 2016-2017
#
# Apalis/Colibri iMX6 in-field hardware update script
#
# One-time configurations (non-reversible!):
# - Fuse SoC to use eMMC Fast Boot mode
# - Enable eMMC H/W reset capabilities
# Required configurations
# - Configure Boot Bus mode (due to eMMC Fast Boot mode above)
#
# Other configurations
# - Boot from eMMC boot partition (must run as wrap-up script)
#

PRODUCT_ID=$1
BOARD_REV=$2
SERIAL=$3
IMAGE_FOLDER=$4

error_exit () {
	echo "$1" 1>&2
	exit 1
}

# Do a basic validation that we do this on one of our modules
case $PRODUCT_ID in
0027|0028|0029|0035) ;;
0014|0015|0016|0017) ;;
*) error_exit "This script is meant to be run on a Apalis/Colibri iMX6. Aborting...";
esac

# Fuse SoC's BOOT_CFG to enable eMMC Fast Boot mode, if necessary
# WARNING: Fusing is a one-time operation, do not change values
# here unless you are absolutely sure what your are doing.
BOOT_CFG=0x5072
if [ ! -f /sys/fsl_otp/HW_OCOTP_CFG4 ]; then
	echo "Fusing not supported."
elif grep -q ${BOOT_CFG} /sys/fsl_otp/HW_OCOTP_CFG4; then
	echo "No new value for BOOT_CFG required."
else
	echo ${BOOT_CFG} > /sys/fsl_otp/HW_OCOTP_CFG4
	if [ "$?" != "0" ]; then
		error_exit "Writing fuse BOOT_CFG failed! Aborting..."
	fi
	echo "Fuse BOOT_CFG updated to ${BOOT_CFG}."
fi

# eMMC configurations
MMCDEV=/dev/mmcblk0

# Enable eMMC H/W Reset feature. This need to be executed before the other
# eMMC settings, it seems that this command resets all settings.
# Since this is a one-time operation, it will fail the second time. Ignore
# errors and redirect stderr to stdout.
mmc hwreset enable ${MMCDEV} 2>&1
if [ "$?" == "0" ]; then
	echo "H/W Reset permanently enabled on ${MMCDEV}"
fi

# Set boot bus mode
if ! mmc bootbus set single_hs x1 x8 ${MMCDEV}; then
	error_exit "Setting boot bus mode failed"
fi

# Enable eMMC boot partition 1 (mmcblkXboot0) and boot ack
# Make sure everything hit the eMMC when execute this command. Otherwise
# the eMMC will reset the configuration.
sync
if ! mmc bootpart enable 1 1 ${MMCDEV}; then
	error_exit "Setting bootpart failed"
fi

mmc extcsd read ${MMCDEV} | grep -e BOOT_BUS_CONDITIONS -e PARTITION_CONFIG -e RST_N_FUNCTION

echo "Apalis/Colibri iMX6 in-field hardware update script ended successfully."

exit 0
