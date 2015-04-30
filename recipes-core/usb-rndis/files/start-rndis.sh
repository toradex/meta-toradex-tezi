#!/bin/sh
# configures the usb gadget to provide rnidis, 
case "$1" in
	start)
		/sbin/modprobe g_ether dev_addr=00:14:2d:ff:ff:ff host_addr=00:14:2d:ff:ff:fe
	;;
	
	stop)
		/sbin/modprobe -r g_ether
	;;
esac

exit 0
