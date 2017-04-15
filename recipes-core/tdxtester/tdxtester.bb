SUMMARY = "Toradex Python Tester Utility"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r1"

SRC_URI = "git://eng-git.toradex.int/toradex-tester.git;branch=master;protocol=git"
SRCREV = "c53f575a30e22ea87040a5c420bb6fab5cce2637"

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
        python3-can \
        iproute2 \
    "

inherit setuptools3

S = "${WORKDIR}/git"
