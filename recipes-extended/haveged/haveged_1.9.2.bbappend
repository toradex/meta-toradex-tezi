FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append = " \
    file://fix-cpu-cache-size-detection.patch \
"
