# Copyright (C) 2013 Timesys Corporation
SUMMARY = "Multicore communication kernel module"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c8959abcbbe4d6676c58eab9354019e6"

inherit module

SRC_URI = "git://github.com/toradex/mcc-kmod.git;protocol=git;branch=${SRCBRANCH}"

SRCBRANCH = "master"
SRCREV = "983012e899c2be4082fb3d45acedeb52bc2191c2"

PV = "1.06"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "(vf60)"
