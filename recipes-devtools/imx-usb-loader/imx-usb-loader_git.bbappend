SRCREV = "ba96a36a8e61e018112e570456a161d5864b33ef"

LDFLAGS_prepend = "-static "
EXTRA_OEMAKE = "'PKG_CONFIG=pkg-config --static'"
