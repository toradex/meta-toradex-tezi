PRINC = "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

EXTRA_OEGYP_prepend = "-DUSE_EABI_HARDFLOAT=1"

SRC_URI +=  " \
    file://chromium_arm_build.patch \
    file://h264_enablement.patch \
"

FILES_${PN} += " \
    ${libdir}/libOmxCore.so 
"

#we have a symlinks ending in .so, skip QA ldflags for this package
INSANE_SKIP_${PN} = "dev-so ldflags"

do_install_append () {
    #make a symlink with the standard soname of the OpenMax component
    install -d ${D}/${libdir}
    ln -s libnvomx.so ${D}/${libdir}/libOmxCore.so
}
