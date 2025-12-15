FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:${THISDIR}/files:"

inherit toradex-kernel-config

SRC_URI:append = " \
    file://0001-drm-bridge-cadence-lower-mhdp-hdmi-bridge-ddc-edid-r.patch \
    file://tezi.cfg \
"
