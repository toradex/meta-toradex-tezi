#EXTRA_OEMAKE = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR' 'BUILDDIR=${S}'"
#we want mkfs.ubifs binary to run on a 32-bit architecture, on x86_64 this requires the 32-bit compatibility libs
EXTRA_OEMAKE_class-native = "'CC=${CC}' 'RANLIB=${RANLIB}' 'AR=${AR}' 'CFLAGS=${CFLAGS} -I${S}/include -DWITHOUT_XATTR -m32' 'BUILDDIR=${S}'"
