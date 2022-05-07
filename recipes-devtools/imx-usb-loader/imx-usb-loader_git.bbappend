SRCREV = "d34a3e37db09b23de440c3d10b877768832f679c"

LDFLAGS:prepend = "-static "
EXTRA_OEMAKE = "'PKG_CONFIG=pkg-config --static'"
