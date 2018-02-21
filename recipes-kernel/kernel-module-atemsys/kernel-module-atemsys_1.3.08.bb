# Copyright (C) 2015-2018 Toradex AG
# Released under the MIT license (see COPYING.MIT for the terms)
SUMMARY = "Linux kernel driver allowing usermode access for EtherCAT Master Stack AT-EMA"

# The Kernel module under Sources/atemsys/ is licensed differently than the
# rest, this recipe packs the Kernel module only
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://atemsys.c;beginline=182;endline=182;md5=7865b9061132c2794f3fb205cda5bdf4"

inherit module

SRC_URI = "http://software.acontis.com/EC-Master/2.9/atemsys-V${PV}.zip"

SRC_URI[md5sum] = "c98d171e7455db93b87edd104b6a47a2"
SRC_URI[sha256sum] = "2d4bd00f483e1826e26cd1263c3d6a5ab348098c11c2dc48477cb4f097159fce"

S = "${WORKDIR}/atemsys-V${PV}/"

export KERNELDIR = "${STAGING_KERNEL_DIR}"

