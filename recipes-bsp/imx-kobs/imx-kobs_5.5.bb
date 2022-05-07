SUMMARY = "Tool to create and write Freescale/NXP I.MX NAND boot related boot data structure to nand flash"
DESCRIPTION = "NXP utility to write FCB, DBBT and boot image."
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "git://github.com/NXPmicro/imx-kobs.git;protocal=https \
    file://add_chip_0_size_param.patch \
    file://0001-src-mtd.c-cope-with-different-kernel-provided-bch-ge.patch \
    file://0002-src-mtd.c-fix-compiler-warnings.patch \
    file://dot-kobs \
"
SRCBRANCH = "master"
SRCREV = "a0e9adce2fb7fcd57e794d7f9a5deba0f94f521b"
PV = "5.5+git${SRCPV}"

S = "${WORKDIR}/git"

inherit autotools

do_install:append:colibri-imx6ull() {
    install -m 0644 ${WORKDIR}/dot-kobs ${D}/.kobs
}

do_install:append:colibri-imx7() {
    install -m 0644 ${WORKDIR}/dot-kobs ${D}/.kobs
}

COMPATIBLE_MACHINE = "(mxs|mx5|mx6|mx7|vf|use-mainline-bsp)"

PN = "kobs-ng"

FILES:${PN} += " \
    /.kobs \
"
