#!/bin/sh
# configures the usb gadget to provide rnidis, 
case "$1" in
	start)
		echo 0 > /sys/class/android_usb/android0/enable
		echo rndis > /sys/class/android_usb/android0/functions
		echo 1 > /sys/class/android_usb/android0/enable
	;;
	
	stop)
		echo 0 > /sys/class/android_usb/android0/enable
	;;
esac

exit 0
