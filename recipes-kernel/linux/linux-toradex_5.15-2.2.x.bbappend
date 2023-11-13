FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}_5.15-2.2.x:${THISDIR}/files:"

inherit toradex-kernel-config
require linux-toradex-tezi-common.inc

SRC_URI:append = " \
    file://0001-drm-bridge-cadence-lower-mhdp-hdmi-bridge-ddc-edid-r.patch \
    file://tezi.cfg \
"
