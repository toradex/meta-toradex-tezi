PACKAGE_ARCH = "${MACHINE_ARCH}"

#T20 does not have neon, but the armv7-linux-gcc assumes that the target has a neon unit
VPXTARGET_armv7a_tegra2 = "armv6-linux-gcc"
VPXTARGET_armv7a_qemuarm = "armv6-linux-gcc"

CFLAGS_append_tegra3 = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CFLAGS_append_mx6 = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CFLAGS_append_mx7 = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CONFIGUREOPTS += " --enable-runtime-cpu-detect "
