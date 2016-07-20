LICENSE = "MIT"
DEPENDS = "u-boot-mkimage-native"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=4d92cd373abda3937c2bc47fbc49d690 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
    file://boot.cmd \
"

S = "${WORKDIR}"

inherit deploy

do_mkimage () {
     if [ ! -d ${MACHINE} ]; then
         mkdir ${MACHINE}
     fi
     ls
     pwd
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "boot script" -d boot.cmd ${MACHINE}/boot.scr
}

addtask mkimage after do_compile before do_install

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/${MACHINE}/boot.scr \
            ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    rm -f boot.scr
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr
}

addtask deploy after do_install before do_build

do_compile[noexec] = "1"
do_install[noexec] = "1"
do_populate_sysroot[noexec] = "1"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(apalis-imx6)"         
