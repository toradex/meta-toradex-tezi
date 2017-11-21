#----------------------------------------------------------
LICENSE_${PN}-rtl8188eu = "Firmware-rtlwifi_firmware"

PACKAGES_prepend = "\
                     ${PN}-rtl8188eu \
                   "
FILES_${PN}-rtl8188eu = " \
  /lib/firmware/rtlwifi/rtl8188eufw.bin \
"
RDEPENDS_${PN}-rtl8188eu += "${PN}-rtl-license"

#----------------------------------------------------------
LICENSE_${PN}-sd8887 = "Firmware-Marvell"

PACKAGES_prepend = "\
                     ${PN}-sd8887 \
                   "
FILES_${PN}-sd8887 = " \
  /lib/firmware/mrvl/sd8887_uapsta.bin \
"
RDEPENDS_${PN}-sd8887 += "${PN}-marvell-license"
