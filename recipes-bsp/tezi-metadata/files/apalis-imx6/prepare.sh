#!/bin/sh
#
# (c) Toradex AG 2016
#
# Apalis iMX6 in-field hardware update script
#
# One-time configurations (non-reversible!):
# - Fuse SoC to use eMMC Fast Boot mode
# - Enable eMMC H/W reset capabilities
# Required configurations
# - Configure Boot Bus mode (due to eMMC Fast Boot mode above)
#
# Other configurations
# - Boot from eMMC boot partition
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
*) error_exit "This script is meant to be run on a Apalis iMX6. Aborting...";
esac

# Fuse SoC's BOOT_CFG to enable eMMC Fast Boot mode, if necsary
# WARNING: Fusing is a one-time operation, do not change values
# here unless you are absolutely sure what your are doing.
if [ ! -f /sys/fsl_otp/HW_OCOTP_CFG4 ]; then
	echo "Fusing not supported."
elif grep -q 0x5072 /sys/fsl_otp/HW_OCOTP_CFG4; then
	echo "No new value for BOOT_CFG required."
else
	echo 0x5072 > /sys/fsl_otp/HW_OCOTP_CFG4
	if [ "$?" != "0" ]; then
		error_exit "Writing fuse BOOT_CFG failed! Aborting..."
	fi
fi

# eMMC configurations
MMCDEV=/dev/mmcblk0

# Set boot bus mode
if ! mmc bootbus set single_hs x1 x8 ${MMCDEV}; then
	error_exit "Setting boot bus mode failed"
fi

# Enable eMMC boot partition 1 (mmcblkXboot0) and boot ack
if ! mmc bootpart enable 1 1 ${MMCDEV}; then
	error_exit "Setting bootpart failed"
fi

# Enable eMMC H/W Reset feature
# Since this is a one-time operation, it will fail the second time. Ignore
# errors and redirect stderr to stdout.
mmc hwreset enable ${MMCDEV} 2>&1

echo "Apalis iMX6 in-field hardware update script ended successfully."

exit 0
