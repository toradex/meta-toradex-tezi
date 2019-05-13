LICENSE = "MIT"
DEPENDS = "u-boot-mkimage-native"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://boot.cmd \
    file://boot-sdp.cmd \
"
SRC_URI_apalis-t30-mainline = " \
    file://boot.cmd \
"
SRC_URI_apalis-tk1 = " \
    file://boot.cmd \
"
SRC_URI_apalis-tk1-mainline = " \
    file://boot.cmd \
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
do_mkimage_apalis-t30-mainline () {
     if [ ! -d ${MACHINE} ]; then
         mkdir ${MACHINE}
     fi
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "Distro boot script" -d boot.cmd ${MACHINE}/boot.scr
}

do_mkimage_apalis-tk1 () {
     if [ ! -d ${MACHINE} ]; then
         mkdir ${MACHINE}
     fi
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "Distro boot script" -d boot.cmd ${MACHINE}/boot.scr
}
do_mkimage_apalis-tk1-mainline () {
     if [ ! -d ${MACHINE} ]; then
         mkdir ${MACHINE}
     fi
     uboot-mkimage -A arm -O linux -T script -C none -a 0 -e 0 \
          -n "Distro boot script" -d boot.cmd ${MACHINE}/boot.scr
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
do_deploy_apalis-t30-mainline () {
    install -d ${DEPLOYDIR}
    install ${S}/${MACHINE}/boot.scr \
            ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    rm -f boot.scr
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr
}
do_deploy_apalis-tk1 () {
    install -d ${DEPLOYDIR}
    install ${S}/${MACHINE}/boot.scr \
            ${DEPLOYDIR}/boot.scr-${MACHINE}-${PV}-${PR}

    cd ${DEPLOYDIR}
    rm -f boot.scr
    ln -sf boot.scr-${MACHINE}-${PV}-${PR} boot.scr
}
do_deploy_apalis-tk1-mainline () {
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

# apalis-tk1 will include apalis-tk1-mainline as well
COMPATIBLE_MACHINE = "(apalis-imx8|apalis-imx6|apalis-t30|apalis-tk1|colibri-imx6|colibri-imx7)"
