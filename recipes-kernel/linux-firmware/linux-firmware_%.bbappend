LICENSE_${PN}-rtl8188eu = "Firmware-rtlwifi_firmware"

PACKAGES_prepend = "\
                     ${PN}-rtl8188eu \
                   "
FILES_${PN}-rtl8188eu = " \
  /lib/firmware/rtlwifi/rtl8188eufw.bin \
"
RDEPENDS_${PN}-rtl8188eu += "${PN}-rtl-license"
