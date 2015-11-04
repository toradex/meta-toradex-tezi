SUMMARY = "Linux Kernel for Toradex Freescale i.MX6 based modules"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel
require recipes-kernel/linux/linux-dtb.inc

DEPENDS += "lzop-native "

LINUX_VERSION_mx6 = "3.14.28"

LOCALVERSION = "-${PR}"
SRCREV_mx6 = "77e525493f74f9f2e33a41e9e65b54e810ac3dd0"
PR_mx6 = "V2.5b2"

PV = "${LINUX_VERSION}+gitr${SRCREV}"
S = "${WORKDIR}/git"
SRCBRANCH_mx6 = "toradex_imx_3.14.28_1.0.0_ga"
SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH}"

# Load USB functions configurable through configfs (CONFIG_USB_CONFIGFS)
KERNEL_MODULE_AUTOLOAD += "${@bb.utils.contains('COMBINED_FEATURES', 'usbgadget', ' libcomposite', '',d)}"

COMPATIBLE_MACHINE = "(colibri-imx6|apalis-imx6)"

# Place changes to the defconfig here
config_script () {
#    #example change to the .config
#    #sets CONFIG_TEGRA_CAMERA unconditionally to 'y'
#    sed -i -e /CONFIG_TEGRA_CAMERA/d ${S}/.config
#    echo "CONFIG_TEGRA_CAMERA=y" >> ${S}/.config
    echo "dummy" > /dev/null
}

do_configure_prepend () {
    #use the defconfig provided in the kernel source tree
    #assume its called ${MACHINE}_defconfig, but with '_' instead of '-'
    DEFCONFIG="`echo ${MACHINE} | sed -e 's/\-/\_/g' -e 's/$/_defconfig/'`"

    cd ${S}
    export KBUILD_OUTPUT=${B}
    oe_runmake $DEFCONFIG

    #maybe change some configuration
    config_script

    #Add Toradex BSP Version as LOCALVERSION
    sed -i -e /CONFIG_LOCALVERSION/d ${B}/.config
    echo "CONFIG_LOCALVERSION=\"${LOCALVERSION}\"" >> ${B}/.config

    #Add GIT revision to the local version
    head=`git --git-dir=${S}/.git rev-parse --verify --short HEAD 2> /dev/null`
    printf "%s%s" +g $head > ${S}/.scmversion
}

# We need to pass it as param since kernel might support more then one
# machine, with different entry points
KERNEL_EXTRA_ARGS += "LOADADDR=${UBOOT_ENTRYPOINT}"

kernel_do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
    oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS}
    if test "${KERNEL_IMAGETYPE_FOR_MAKE}.gz" = "${KERNEL_IMAGETYPE}"; then
        gzip -9c < "${KERNEL_IMAGETYPE_FOR_MAKE}" > "${KERNEL_OUTPUT}"
    fi
}

do_compile_kernelmodules() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
    if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
        oe_runmake ${PARALLEL_MAKE} modules LD="${KERNEL_LD}"
    else
        bbnote "no modules to compile"
    fi
}
