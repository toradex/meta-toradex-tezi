PACKAGE_ARCH_tegra2 = "${MACHINE_ARCH}"

#T20 does not have neon, but the armv7-linux-gcc assumes that the target has a neon unit
CONFIGUREOPTS_append_tegra2 = " --disable-runtime-cpu-detect --disable-neon --disable-neon-asm "
