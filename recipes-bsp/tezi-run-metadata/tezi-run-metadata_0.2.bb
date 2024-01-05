DESCRIPTION = "Toradex Easy Installer Metadata for Toradex Easy Installer"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS:am62xx = "dfu-util-native patchelf-native"
MCDEPENDS = ""
MCDEPENDS:am62xx = "mc::k3r5:virtual/bootloader:do_deploy"
do_deploy[mcdepends] = "${MCDEPENDS}"

SRC_URI = " \
    file://wrapup.sh \
    file://tezi.png \
    file://recovery-linux.sh \
    file://recovery-windows.bat \
    file://recovery/uuu \
    file://recovery/uuu.exe \
    file://recovery/uuu.auto \
    file://recovery-windows.README \
"

SRC_URI:append:am62xx = " \
    https://dfu-util.sourceforge.net/releases/dfu-util-0.11-binaries.tar.xz;name=dfu-util \
    https://sourceforge.net/projects/gnuwin32/files/coreutils/5.3.0/coreutils-5.3.0-bin.zip;name=coreutils-5.3.0-bin;subdir=coreutils-5.3.0 \
    https://sourceforge.net/projects/gnuwin32/files/coreutils/5.3.0/coreutils-5.3.0-dep.zip;name=coreutils-5.3.0-dep;subdir=coreutils-5.3.0 \
    file://recovery/SocTypeGP.bin \
"

SRC_URI[md5sum] = "11cf6d9b4b18b7f35f06ed91bfc0b3a8"
SRC_URI[sha256sum] = "064bff2e4cb4a0c0f8bceeaf6dd2cef1e682869e0b22d49e40bc6a8326ec14c9"
SRC_URI[dfu-util.md5sum] = "fac2c6583808e68a9a27d40425e18d63"
SRC_URI[dfu-util.sha256sum] = "6450de30a7dcd8d8c1273f43f0b153f054fd24d85f7f38296b1ad8edbd2ddb25"
SRC_URI[coreutils-5.3.0-bin.md5sum] = "aa7ce7f1f2befb930fb156bddea41bc4"
SRC_URI[coreutils-5.3.0-bin.sha256sum] = "5646a3c2d466ca3cc9f27df91f2e9ffd870c61e73e1a02cbe01a9d7dfea0dbcb"
SRC_URI[coreutils-5.3.0-dep.md5sum] = "6cf05855b6902dffa2cf4ba8b90e82e6"
SRC_URI[coreutils-5.3.0-dep.sha256sum] = "0e516b603b5d39cfd64737ffdc043b1977a4e10a3d93b55144fcb48f761c67c7"

TEZI_RUN_DEPLOYDIR = "${DEPLOYDIR}/${BPN}"

inherit deploy linux-kernel-base nopackages

DEPENDS = "virtual/kernel"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

do_deploy() {
    install -d ${TEZI_RUN_DEPLOYDIR}/recovery
    install -m 644 ${WORKDIR}/wrapup.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${WORKDIR}/tezi.png ${TEZI_RUN_DEPLOYDIR}
    install -m 755 ${WORKDIR}/recovery-linux.sh ${TEZI_RUN_DEPLOYDIR}
    install -m 644 ${WORKDIR}/recovery-windows.bat ${TEZI_RUN_DEPLOYDIR}

    install -m 644 ${WORKDIR}/recovery/uuu.auto ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/recovery/uuu ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/recovery/uuu.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery-windows.README ${TEZI_RUN_DEPLOYDIR}/
}

do_deploy:append:am62xx () {

    install -m 755 ${RECIPE_SYSROOT_NATIVE}/usr/bin/dfu-util ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/dfu-util-0.11-binaries/win64/dfu-util.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/dfu-util-0.11-binaries/win64/libusb-1.0.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/coreutils-5.3.0/bin/dd.exe ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/coreutils-5.3.0/bin/libiconv2.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 755 ${WORKDIR}/coreutils-5.3.0/bin/libintl3.dll ${TEZI_RUN_DEPLOYDIR}/recovery/
    install -m 644 ${WORKDIR}/recovery/SocTypeGP.bin ${TEZI_RUN_DEPLOYDIR}/recovery/
}

addtask deploy before do_build after do_install

COMPATIBLE_MACHINE = "(apalis|colibri|verdin)"

PACKAGE_ARCH = "${MACHINE_ARCH}"
