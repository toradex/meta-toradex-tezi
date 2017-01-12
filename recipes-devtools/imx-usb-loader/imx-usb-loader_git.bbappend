SRCREV = "5434415d921f1cc4d22332d9558bed6d42db9f60"
SRC_URI = "git://github.com/toradex/imx_usb_loader.git;protocol=git;branch=imx_usb_batch_mode_refactored"

LDFLAGS_prepend = "-static "
EXTRA_OEMAKE = "'PKG_CONFIG=pkg-config --static'"
