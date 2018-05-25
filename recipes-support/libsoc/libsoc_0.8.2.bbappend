# Last release 0.8.2 doesn't include the latest colibri-vf confs
SRCREV = "577917c01fa5d8b06c224745c4328070f7e28f97"

#BOARD = "colibri-vf61"
PACKAGECONFIG = "allboardconfigs python"

#PACKAGECONFIG[disabledebug] = "--disable-debug,,"
#PACKAGECONFIG[allboardconfigs] = "--with-board-configs,,"
#PACKAGECONFIG[enableboardconfig] = "--enable-board=${BOARD},,"
#PACKAGECONFIG[python] = "--enable-python=${PYTHON_PN},,${PYTHON_PN}"
