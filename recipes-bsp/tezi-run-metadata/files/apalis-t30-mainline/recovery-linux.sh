#!/bin/sh

# Append tezi.itb to recovery U-Boot and transfer it using tegrarcm
echo "Downloading Toradex Easy Installer..."
if [ "$1" = "-d" ]; then
	cp u-boot-dtb-tegra.bin tezi-recovery.bin
	dd if=tezi.itb of=tezi-recovery.bin bs=1M seek=1
	cd recovery
	sudo ./tegrarcm --bct=Apalis_T30_2GB_800Mhz.bct	 --bootloader=../tezi-recovery.bin --loadaddr=0x80108000 --usb-timeout=5000
else
	cp u-boot-dtb-tegra.bin tezi-recovery.bin > /dev/null
	dd if=tezi.itb of=tezi-recovery.bin bs=1M seek=1 status=none
	cd recovery
	sudo ./tegrarcm --bct=Apalis_T30_2GB_800Mhz.bct	 --bootloader=../tezi-recovery.bin --loadaddr=0x80108000 --usb-timeout=5000 > /dev/null
fi
if [ $? != 0 ]; then
	echo ""
	printf "\033[31mDownloading Toradex Easy Installer failed...\033[0m\n";
	exit 1
fi

printf "\033[32mSuccessfully downloaded Toradex Easy Installer.\033[0m\n\n"
