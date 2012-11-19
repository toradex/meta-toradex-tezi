# Notes:
# Split out a ralink package of the 22Meg firmware blob
PRINC = "1"

PACKAGES =+ "${PN}-ralink"

LICENSE_${PN}-ralink = "Firmware:LICENCE.ralink-firmware.txt"
FILES_${PN}-ralink = " \
  /lib/firmware/rt[0-9]*.bin \
  /lib/firmware/LICENCE.ralink-firmware.txt \
"
