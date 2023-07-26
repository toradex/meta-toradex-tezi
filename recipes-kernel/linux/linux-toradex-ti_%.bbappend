FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

require linux-toradex-tezi-common.inc

SRC_URI:append = " file://tezi.scc"
