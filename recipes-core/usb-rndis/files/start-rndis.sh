#!/bin/sh

# configures the usb gadget to provide rnidis, 
# sets the IP addr of the newly created interface
echo 0 > /sys/class/android_usb/android0/enable
echo rndis > /sys/class/android_usb/android0/functions
echo 1 > /sys/class/android_usb/android0/enable
ifconfig rndis0 192.168.11.2
