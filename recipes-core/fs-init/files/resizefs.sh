#!/bin/sh
# resize the rootfs ext filesystem size to its full partition size
# usually used on first boot in a postinstall script
# or set in an autostart file from a postinstall script

DISK="mmcblk0"
PART="mmcblk0p1"

logger "resizing $PART to fill its full partition size"
# get the disk total size
DISK_SIZE=`cat /sys/block/$DISK/size`

# get partition start and size
PART_OFF=`cat /sys/block/$DISK/$PART/start`
PART_SIZE=`cat /sys/block/$DISK/$PART/size`

# calculate size after the partition to the end of disk
SPARE=`expr $DISK_SIZE - $PART_OFF - $PART_SIZE`

# new filesystem size, it must not overlap the secondary gpt header
# assume 1024kB as GPT size (is 34 sectors)
if [ $SPARE -lt 2048 ]
then
	FSSIZE=`expr $PART_SIZE - 2048`
else
	FSSIZE=$PART_SIZE
fi

# resize now

#reduce I/O load by doing this in 32M increments
#idea stolen from here:
#https://codereview.chromium.org/551127

NEXTSIZE=`expr 320 \* 1024 \* 2`
while [ $NEXTSIZE -lt $FSSIZE ]; do
	FSSIZEMEG=`expr $NEXTSIZE / 2 / 1024`"M"
	resize2fs /dev/$PART $FSSIZEMEG
	sleep 1
	NEXTSIZE=`expr $NEXTSIZE + 32 \* 1024 \* 2`
done
FSSIZEMEG=`expr $FSSIZE / 2 / 1024`"M"
resize2fs /dev/$PART $FSSIZEMEG

#job done, remove it from systemd services
systemctl disable resizefs.service

logger "resizing $PART finished, new size is $FSSIZEMEG"

