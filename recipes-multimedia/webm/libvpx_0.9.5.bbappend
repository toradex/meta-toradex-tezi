PRINC = "1"

CFLAGS_append = " -mfpu=neon -funsafe-math-optimizations -ftree-vectorize "
CONFIGUREOPTS += " --enable-runtime-cpu-detect "