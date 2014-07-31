#!/bin/sh
# configures the usb gadget to provide rnidis, 
case "$1" in
	start)
		/sbin/modprobe g_ether dev_addr=00:14:2d:ff:ff:ff host_addr=00:14:2d:ff:ff:fe ; \
		  sleep 1 && /sbin/ip address add 192.168.11.2/24 dev usb0; /sbin/ip link set usb0 up \
		  && /usr/sbin/udhcpd -S /etc/udhcpd-usb-rndis.conf
	;;
	
	stop)
		/sbin/modprobe -r g_ether
	;;
esac

exit 0
