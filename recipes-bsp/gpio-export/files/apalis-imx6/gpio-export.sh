#!/bin/sh
#export gpios to userspace

case "$1" in
	start)
		CTRL=/sys/class/gpio/export
	;;
	
	stop)
		CTRL=/sys/class/gpio/unexport
	;;

	*)
		echo "usage: '$0 start' '$0 stop'"
		exit 1
	;;
esac

#Apalis GPIO - i.MX6 Ball name - GPIO#

#GPIO1 NAND_DATA04 36
echo 36 > $CTRL
#cat /sys/class/gpio/gpio36/value
#echo low  > /sys/class/gpio/gpio36/direction
#echo 1 > /sys/class/gpio/gpio36/value

#GPIO2 NAND_DATA05 37
echo 37 > $CTRL

#GPIO3 NAND_DATA06 38
echo 38 > $CTRL

#GPIO4 NAND_DATA07 39
echo 39 > $CTRL

#GPIO5 NAND_READY 170
#used by optional fusion_F0710A kernel module
#echo 170 > $CTRL

#GPIO6 NAND_WP_B 169
#used by optional fusion_F0710A kernel module
#echo 169 > $CTRL

#GPIO7 GPIO02 2
#used by PCIe for reset of switch on the Apalis Evaluation Board
#echo 2 > $CTRL

#GPIO8 GPIO06 6
echo 6 > $CTRL
