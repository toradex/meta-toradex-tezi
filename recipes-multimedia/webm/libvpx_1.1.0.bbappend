FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PRINC = "2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "file://configure_remove_implicit_cflags.patch \
            "
            
#T20 does not have neon, but the armv7-linux-gcc assumes that the target has a neon unit
VPXTARGET_armv7a_colibri-t20 = "armv6-linux-gcc"
#VPXTARGET_armv7a_colibri-t30 = "armv6-linux-gcc"
#VPXTARGET_armv7a_apalis-t30 = "armv6-linux-gcc"
VPXTARGET_armv7a_qemuarm = "armv6-linux-gcc"

CFLAGS_append_colibri-t30 = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CFLAGS_append_apalis-t30 = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CONFIGUREOPTS += " --enable-runtime-cpu-detect "