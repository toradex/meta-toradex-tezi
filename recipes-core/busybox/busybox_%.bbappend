FILESEXTRAPATHS:prepend := "${THISDIR}/files:" 
SRC_URI:append = "\
    file://resize.cfg \
    file://tinyinit.cfg \
    file://ntpd.cfg \
    file://ifplugd.cfg \
    file://utils.cfg \
"

