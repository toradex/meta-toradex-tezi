FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://0001-Add-Mouse-Wheel-Support.patch \
    file://0002-Remove-Warnings-on-unknown-events.patch \
    file://0003-make-sure-relative-devices-limit-to-screen-size.patch \
    file://0001-remove-DPI-handling.patch \
    file://0002-never-use-hwaccel-when-using-VNC-backend.patch \
"
