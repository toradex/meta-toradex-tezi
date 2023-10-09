FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}_5.15-2.2.x:${THISDIR}/files:"

inherit toradex-kernel-config
require linux-toradex-tezi-common.inc

SRC_URI:append = " file://tezi.cfg"
