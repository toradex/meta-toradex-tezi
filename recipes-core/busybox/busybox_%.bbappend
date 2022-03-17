FILESEXTRAPATHS_prepend := "${THISDIR}/files:" 
SRC_URI_append = "\
    file://resize.cfg \
    file://tinyinit.cfg \
    file://ntpd.cfg \
    file://ifplugd.cfg \
    file://utils.cfg \
"

