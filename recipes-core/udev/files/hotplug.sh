#!/bin/sh
#
# Called from udev
#
# Set the read_ahead cache size of SD card on hotplug

SYSNAME=$(uname -n)
DISKNUM=$(echo $DEVNAME | sed -e 's/\(^.*\)\(.$\)/\2/')
DISKNAME=$(echo $DEVNAME | sed 's/.*\///g;s/p.*//g')
CORES=$(cat /proc/cpuinfo | grep processor | wc -l)

set_read_ahead_size () {
	if [ $DISKNUM == "0" ]; then #eMMC
		echo $1 > /sys/block/$DISKNAME/queue/read_ahead_kb
	else #SD card
		echo $2 > /sys/block/$DISKNAME/queue/read_ahead_kb
	fi
}

if [ "$ACTION" = "add" ] && [ -n "$DEVNAME" ] && [ "$DEVTYPE" = "disk" ]; then
	#Set read_ahead cache of mmcblk? SD card
	if test -e /sys/block/$DISKNAME/queue/read_ahead_kb
	then
		if [ $SYSNAME == "colibri-imx6" ]; then
			if [ $CORES -eq 1 ]; then #imx6s
				set_read_ahead_size 512 512
			else #imx6dl
				set_read_ahead_size 1024 512
			fi
		elif [ $SYSNAME == "colibri-t30" ]; then
			set_read_ahead_size 2048 1024
		elif [ $SYSNAME == "apalis-imx6" ]; then
			if [ $CORES -eq 2 ]; then #imx6d
				set_read_ahead_size 1024 512
			else #imx6q
				set_read_ahead_size 1024 512
			fi
		elif [ $SYSNAME == "apalis-t30" ]; then
			set_read_ahead_size 4096 1024
		elif [ $SYSNAME == "apalis-tk1" ]; then
			set_read_ahead_size 4096 1024
		else
			logger "sd_hotplug.sh" "System name not compatible"
		fi
	else
		logger "sd_hotplug" "No read_ahead_kb configuration file found"
    fi
fi
