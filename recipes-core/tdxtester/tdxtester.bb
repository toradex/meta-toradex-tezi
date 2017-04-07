SUMMARY = "Toradex Python Tester Utility"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

SRC_URI = "git://eng-git.toradex.int/toradex-tester.git;branch=master;protocol=git"
SRCREV = "2021e6445e967724fc4662ef8ed980e427d3cf0b"

RDEPENDS_${PN} = " \
        python3 \
        python3-ctypes \
        python3-importlib \
        python3-pyalsaaudio \
        python3-pyserial \
        python3-textutils \
        python3-mmap \
        python3-numpy \
        python3-json \
        python3-smbus \
    "

inherit setuptools3

S = "${WORKDIR}/git"
