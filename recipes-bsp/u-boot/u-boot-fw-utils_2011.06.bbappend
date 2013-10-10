# Extends the core u-boot recipe 
# to take the u-boot sources including the colibri stuff from our git repository
PR ="r7"
COMPATIBLE_MACHINE_colibri-t20 = "colibri-t20"

DEFAULT_PREFERENCE_colibri-t20 = "1"

FILESPATHPKG =. "git:"
S="${WORKDIR}/git"
SRC_URI_COLIBRI =  "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=colibri"
SRCREV_COLIBRI = "d7fcf63504425a73f74f3d1d2f5ba68533280ff4"

PV_colibri-t20 = "${PR}+gitr${SRCREV}"

SRC_URI_colibri-t20 = "${SRC_URI_COLIBRI}"
SRCREV_colibri-t20 = "${SRCREV_COLIBRI}"
PV_colibri-t20 = "${PR}+gitr${SRCREV}"
