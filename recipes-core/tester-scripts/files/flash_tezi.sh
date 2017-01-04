#!/bin/sh

if [ "$1" == "-h" ]
then
    echo "Usage: flash_tezi [FOLDER]"
fi

mkdir -p /run/src
mount /dev/mmcblk2p1 /run/src

if [ ! -d /run/src/$1/ ]
then
    echo "Directory $1 missing."
    umount /run/src
    exit
fi

# Write SPL/U-Boot to boot partition
#This is done in a U-Boot script
#echo 0 > /sys/class/block/mmcblk0boot0/force_ro
#dd if=/run/src/SPL of=/dev/mmcblk0boot0 seek=2
#dd if=/run/src/u-boot.img of=/dev/mmcblk0boot0 seek=138

# Create Tezi Partition
echo "8192,65536,0c" | sfdisk /dev/mmcblk0
mkfs.fat /dev/mmcblk0p1

mkdir -p /run/dst
mount /dev/mmcblk0p1 /run/dst

# Write Tezi files...
TEZI_FILES="boot.scr tezi.itb"
for FILE in ${TEZI_FILES}
do
    cp /run/src/$1/$FILE /run/dst
done
umount /run/dst
umount /run/src

sync
