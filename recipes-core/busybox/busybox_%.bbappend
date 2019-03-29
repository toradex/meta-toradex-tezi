FILESEXTRAPATHS_prepend := "${THISDIR}/files:" 
SRC_URI_append = "\
    file://0001-ifplugd-if-SIOCSIFFLAGS-fails-with-ENODEV-don-t-die.patch \
    file://0002-ifplugd-if-SIOCSIFFLAGS-fails-with-EADDRNOTAVAIL-don.patch \
    file://resize.cfg \
    file://tinyinit.cfg \
    file://ntpd.cfg \
    file://ifplugd.cfg \
    file://remove.cfg \
    file://utils.cfg \
"

