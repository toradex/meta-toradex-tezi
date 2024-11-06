#!/bin/bash

# ask for the password early
sudo true

DFU_UTIL=$(which dfu-util 2>/dev/null)
if [ -z "$DFU_UTIL" ] || ! $DFU_UTIL -w -V &>/dev/null
then
	echo "Install or update dfu-util from your distro should the provided one not work with your distro"
	DFU_UTIL=recovery/dfu-util
fi

# USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A72 SPL
VID_PID_ROM="0451:6167"
VID_PID_R5="1b67:4000"
VID_PID_A72="1b67:4000"

TIBOOT3_BIN=tiboot3-am69-hs-fs-aquila.bin

wait_usb_device()
{
	VID_PID=$1

	while ! lsusb -d $VID_PID
	do
		sleep 1
	done
}

# load boot binaries, boot script and tezi fitimage to RAM and boot U-Boot
wait_usb_device $VID_PID_ROM
sudo $DFU_UTIL -w -R -a bootloader --device $VID_PID_ROM -D $TIBOOT3_BIN
wait_usb_device $VID_PID_R5
sudo $DFU_UTIL -w -R -a tispl.bin --device $VID_PID_R5 -D tispl.bin
wait_usb_device $VID_PID_A72
sudo $DFU_UTIL -w -R -a u-boot.img --device $VID_PID_A72 -D u-boot.img-recoverytezi

# call uuu to download FIT
echo "Downloading Toradex Easy Installer..."
if [ "$1" = "-q" ]; then
        sudo ./recovery/uuu recovery > /dev/null
else
        sudo ./recovery/uuu recovery
fi

if [ $? != 0 ]; then
        echo ""
        printf "\033[31mDownloading Toradex Easy Installer failed...\033[0m\n";
        exit 1
fi

printf "\033[32mSuccessfully downloaded Toradex Easy Installer.\033[0m\n\n"

exit 0
