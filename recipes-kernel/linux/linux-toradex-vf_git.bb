inherit kernel
require recipes-kernel/linux/linux.inc

LINUX_VERSION ?= "3.0.15"

SRCREV_colibri-vf50 = "87af4ef6e63b55de3c34cd20e9ebb54b0e0ded05"
PR_colibri-vf50 = "V2.1b1"

PV = "${LINUX_VERSION}+gitr${SRCREV}"
S = "${WORKDIR}/git"
SRC_URI = "git://git.toradex.com/linux-toradex.git;protocol=git;branch=colibri_vf"
# a Patch
# SRC_URI += "file://a.patch "


COMPATIBLE_MACHINE_colibri-vf50 = "colibri-vf50"

# Place changes to the defconfig here
config_script () {
#    #example change to the .config
#    #sets CONFIG_TEGRA_CAMERA unconditionally to 'y'
#    sed -i -e /CONFIG_TEGRA_CAMERA/d ${S}/.config
#    echo "CONFIG_TEGRA_CAMERA=y" >> ${S}/.config
    sed -i -e /CONFIG_VFPv3/d ${S}/.config
    echo "CONFIG_VFPv3=y" >> ${S}/.config
    sed -i -e /CONFIG_NEON/d ${S}/.config
    echo "CONFIG_NEON=y" >> ${S}/.config
    echo "dummy" > /dev/null
}

do_configure_prepend () {
    #use the defconfig provided in the kernel source tree
    #assume its called ${MACHINE}_defconfig, but with '_' instead of '-'
    DEFCONFIG=`echo ${MACHINE} | sed -e 's/\-/\_/g' -e 's/$/_defconfig/'`

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
