#!/bin/bash

# ask for the password early
sudo true

DFU_UTIL=$(which dfu-util 2>/dev/null)
if [ -z "$DFU_UTIL" ] || ! $DFU_UTIL -w -V &>/dev/null
then
	echo "Install or update dfu-util from your distro should the provided one not work with your distro"
	DFU_UTIL=recovery/dfu-util
fi

# USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A53 SPL
VID_PID_ROM="0451:6165"
VID_PID_R5="1b67:4000"
VID_PID_A53="1b67:4000"

# tiboot3.bin depends on the SoC type, GP or HS-FS
TIBOOT3_GP_BIN=tiboot3-am62x-gp-verdin.bin
TIBOOT3_HSFS_BIN=tiboot3-am62x-hs-fs-verdin.bin

wait_usb_device()
{
	VID_PID=$1

	while ! lsusb -d $VID_PID
	do
		sleep 1
	done
}

# set TIBOOT3_BIN variable according to the SoC type (GP or HF-FS)
select_tiboot3_bin()
{
	local soc_type
	local tmp_dir
	local soc_id_bin

	wait_usb_device $VID_PID_ROM

	tmp_dir=$(mktemp -d) || exit 1
	soc_id_bin=$tmp_dir/SocId.bin
	sudo $DFU_UTIL -R -a SocId --device $VID_PID_ROM -U $soc_id_bin

	soc_type=$(dd if=$soc_id_bin bs=1 count=4 skip=20 2>/dev/null)

	if [[ "$soc_type" == *"GP"* ]]
	then
		TIBOOT3_BIN=$TIBOOT3_GP_BIN
	else
		TIBOOT3_BIN=$TIBOOT3_HSFS_BIN
	fi

	rm -rf $tmp_dir
}

# select correct tiboot3.bin depending on SoC type
select_tiboot3_bin

# load boot binaries, boot script and tezi fitimage to RAM and boot U-Boot
wait_usb_device $VID_PID_ROM
sudo $DFU_UTIL -w -R -a bootloader --device $VID_PID_ROM -D $TIBOOT3_BIN
wait_usb_device $VID_PID_R5
sudo $DFU_UTIL -w -R -a tispl.bin --device $VID_PID_R5 -D tispl.bin
wait_usb_device $VID_PID_A53
sudo $DFU_UTIL -w -R -a u-boot.img --device $VID_PID_A53 -D u-boot.img-recoverytezi

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
