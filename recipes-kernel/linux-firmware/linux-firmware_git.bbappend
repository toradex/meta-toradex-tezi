# backport of http://cgit.openembedded.org/openembedded-core/commit/?id=2dc67b53d1b7c056bbbff2f90ad16ed214b57609

FILES_${PN}-rtl8192cu_append = " \
  /lib/firmware/rtlwifi/rtl8192cufw*.bin \
"

FILES_${PN}-rtl8192ce_append = " \
  /lib/firmware/rtlwifi/rtl8192cfw*.bin \
"
