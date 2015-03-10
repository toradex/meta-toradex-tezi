# Copyright (C) 2015 Toradex AG
# Released under the MIT license (see COPYING.MIT for the terms)
SUMMARY = "Linux kernel driver allowing usermode access for EtherCAT Master Stack AT-EMA"

# The Kernel module under Sources/atemsys/ is licensed differently than the
# rest, this recipe packs the Kernel module only
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://atemsys.c;beginline=144;endline=144;md5=7865b9061132c2794f3fb205cda5bdf4"

inherit module

SRC_URI = "http://software.acontis.com/EC-Master/2.7/EC-Master-V2.7-Linux_armv6-vfp-eabihf-Eval.tar.gz"

SRC_URI[md5sum] = "41e0ff858d2ec8054509ad814453198c"
SRC_URI[sha256sum] = "f934a561c3897d095b1b09bbdc54982dfffd477c52dac39cc898e020cdf8d534"

S = "${WORKDIR}/Sources/atemsys/"

export KERNELDIR = "${STAGING_KERNEL_DIR}"

COMPATIBLE_MACHINE = "(vf60)"
