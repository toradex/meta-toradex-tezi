DESCRIPTION = "Toradex Easy Installer Metadata for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS:k3 = "dfu-util-native"
MCDEPENDS = ""
MCDEPENDS:k3 = "mc::k3r5:virtual/bootloader:do_deploy"
do_deploy[mcdepends] = "${MCDEPENDS}"

SRC_URI = " \
    file://wrapup.sh \
    file://tezi.png \
    file://recovery-linux.sh \
    file://recovery-windows.bat \
    file://recovery/uuu.auto \
    file://recovery-windows.README \
    file://recovery-linux.README \
"

SRC_URI:append:k3 = " \
    https://dfu-util.sourceforge.net/releases/dfu-util-0.11-binaries.tar.xz;name=dfu-util \
    https://sourceforge.net/projects/gnuwin32/files/coreutils/5.3.0/coreutils-5.3.0-bin.zip;name=coreutils-5.3.0-bin;subdir=coreutils-5.3.0 \
    https://sourceforge.net/projects/gnuwin32/files/coreutils/5.3.0/coreutils-5.3.0-dep.zip;name=coreutils-5.3.0-dep;subdir=coreutils-5.3.0 \
    file://recovery/SocTypeGP.bin \
"

SRC_URI[dfu-util.sha256sum] = "6450de30a7dcd8d8c1273f43f0b153f054fd24d85f7f38296b1ad8edbd2ddb25"
SRC_URI[coreutils-5.3.0-bin.sha256sum] = "5646a3c2d466ca3cc9f27df91f2e9ffd870c61e73e1a02cbe01a9d7dfea0dbcb"
SRC_URI[coreutils-5.3.0-dep.sha256sum] = "0e516b603b5d39cfd64737ffdc043b1977a4e10a3d93b55144fcb48f761c67c7"

S = "${UNPACKDIR}"

TEZI_RUN_DEPLOYDIR = "${DEPLOYDIR}/${BPN}"

inherit deploy nopackages

do_deploy() {
    install -d ${TEZI_RUN_DEPLOYDIR}/recovery
    install -m 644 ${S}/wrapup.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${S}/tezi.png ${TEZI_RUN_DEPLOYDIR}
    install -m 755 ${S}/recovery-linux.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${S}/recovery-windows.bat ${TEZI_RUN_DEPLOYDIR}

    install -m 644 ${S}/recovery/uuu.auto ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${S}/recovery-windows.README ${TEZI_RUN_DEPLOYDIR}/
    install -m 644 ${S}/recovery-linux.README ${TEZI_RUN_DEPLOYDIR}/
}

do_deploy:append:k3 () {

    install -m 755 ${RECIPE_SYSROOT_NATIVE}/usr/bin/dfu-util ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${S}/dfu-util-0.11-binaries/win64/dfu-util.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${S}/dfu-util-0.11-binaries/win64/libusb-1.0.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${S}/coreutils-5.3.0/bin/dd.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${S}/coreutils-5.3.0/bin/libiconv2.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${S}/coreutils-5.3.0/bin/libintl3.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${S}/recovery/SocTypeGP.bin ${TEZI_RUN_DEPLOYDIR}/recovery/
}

addtask deploy before do_build after do_install

COMPATIBLE_MACHINE = "(apalis|aquila|colibri|verdin|toradex|lino)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
