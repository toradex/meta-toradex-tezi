#!/bin/sh

# Use imx_usb batch download capabilities
echo "Downloading Toradex Easy Installer..."
if [ "$1" == "-d" ]; then
	sudo ./recovery/imx_usb -b
else
	sudo ./recovery/imx_usb -b > /dev/null
fi
if [[ $? != 0 ]]; then 
	echo ""
	printf "\033[31mDownloading Toradex Easy Installer failed...\033[0m\n";
	exit 1
fi

printf "\033[32mSuccessfully downloaded Toradex Easy Installer.\033[0m\n\n"
