#!/bin/sh

# Use uuu
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
