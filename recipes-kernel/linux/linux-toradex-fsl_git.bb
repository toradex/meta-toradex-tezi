inherit kernel
require recipes-kernel/linux/linux-toradex-fsl.inc

LINUX_VERSION_colibri-vf = "3.0.15"
LINUX_VERSION_apalis-imx6 ?= "3.0.35"

SRCREV_colibri-vf = "d6e1c85db4d0442a38b43d6c28900a1f1a8d760f"
PR_colibri-vf = "V2.2b1"
SRCREV_apalis-imx6 = "fbff978ea77f9d0832cc924e91b2497d7cde572c"
PR_apalis-imx6 = "V2.2b1"

PV = "${LINUX_VERSION}+gitr${SRCREV}"
S = "${WORKDIR}/git"
SRCBRANCH_colibri-vf = "colibri_vf_next"
SRCBRANCH_apalis-imx6 = "toradex_imx6"
SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=${SRCBRANCH}"
# a Patch
# SRC_URI += "file://a.patch "

COMPATIBLE_MACHINE = "(colibri-vf|apalis-imx6)"

# Place changes to the defconfig here
config_script () {
#    #example change to the .config
#    #sets CONFIG_TEGRA_CAMERA unconditionally to 'y'
#    sed -i -e /CONFIG_TEGRA_CAMERA/d ${S}/.config
#    echo "CONFIG_TEGRA_CAMERA=y" >> ${S}/.config
    sed -i -e /CONFIG_B43/d ${S}/.config
    echo "CONFIG_B43=m" >> ${S}/.config
    sed -i -e /CONFIG_SSB/d ${S}/.config
    echo "CONFIG_SSB=m" >> ${S}/.config
    echo "dummy" > /dev/null
}

do_configure_prepend () {
    #use the defconfig provided in the kernel source tree
    #assume its called ${MACHINE}_defconfig, but with '_' instead of '-'
    DEFCONFIG="`echo ${MACHINE} | sed -e 's/\-/\_/g' -e 's/$/_defconfig/'`"

    oe_runmake $DEFCONFIG

    #maybe change some configuration
    config_script 
}

kernel_do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
     oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} LD="${KERNEL_LD}"
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

do_install_append_colibri-vf() {
    #install vybrid specific headers with definitions used for userspace interaction
    install -d ${D}/${includedir}/linux
    install -m 644 ${S}/include/linux/mvf_sema4.h ${D}/${includedir}/linux/
}
