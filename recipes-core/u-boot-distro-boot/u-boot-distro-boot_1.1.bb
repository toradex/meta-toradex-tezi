LICENSE = "MIT"
DEPENDS = "u-boot-mkimage-native"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
    file://boot.cmd \
    file://boot-sdp.cmd \
"

S = "${WORKDIR}"

inherit deploy

do_mkimage () {
     if [ ! -d ${MACHINE} ]; then
         mkdir ${MACHINE}
     fi
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "Distro boot script" -d boot.cmd ${MACHINE}/boot.scr
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "Recovery boot script" -d boot-sdp.cmd ${MACHINE}/boot-sdp.scr
}

addtask mkimage after do_compile before do_install
do_mkimage[dirs] = "${WORKDIR}"

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/${MACHINE}/boot.scr \
            ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}
    install ${S}/${MACHINE}/boot-sdp.scr \
            ${DEPLOYDIR}/boot-sdp.scr-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    rm -f boot.scr
    rm -f boot-sdp.scr
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr
    ln -sf boot-sdp.scr-${MACHINE}-${PV}-${PR} boot-sdp.scr
}

addtask deploy after do_install before do_build

do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(apalis-imx6|colibri-imx6)"
