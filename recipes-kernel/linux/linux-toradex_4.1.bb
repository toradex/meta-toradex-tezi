require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

SUMMARY = "Linux kernel for Toradex Colibri VFxx Computer on Modules"

SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://defconfig"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

LOCALVERSION = "-v2.5b2"
SRCBRANCH = "toradex_vf_4.1-next"
SRCREV = "db6b1f8270757539d90422ee5294bc25ff13a040"
DEPENDS += "lzop-native bc-native"
COMPATIBLE_MACHINE = "(vf)"
