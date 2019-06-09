SUMMARY = "U-boot bootloader fw_printenv/setenv utils"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"
SECTION = "bootloader"
PROVIDES += "u-boot-fw-utils"
RPROVIDES_${PN} += "u-boot-fw-utils"
DEPENDS = "mtd-utils"

COMPATIBLE_MACHINE = "(apalis-imx6|apalis-t30|apalis-tk1|colibri-imx6|colibri-imx7|colibri-vf)"
DEFAULT_PREFERENCE_apalis-imx6 = "1"
DEFAULT_PREFERENCE_apalis-tk1 = "1"
DEFAULT_PREFERENCE_colibri-imx6 = "1"
DEFAULT_PREFERENCE_colibri-imx7 = "1"
DEFAULT_PREFERENCE_colibri-vf = "1"

FILESPATHPKG =. "git:"
S="${WORKDIR}/git"
SRCREV = "07edca0bb81857a339f26f3465d5c5602705a94d"
SRCBRANCH = "2016.11-toradex"
SRC_URI = "git://git.toradex.com/u-boot-toradex.git;protocol=git;branch=${SRCBRANCH} \
           file://0001-tools-env-build-without-default-environment.patch \
	   file://default-gcc.patch \
           file://fw_env_mtd4.config \
           file://fw_env_mmcblk0boot0.config \
"
PV = "v2016.11-v0.5+git${SRCPV}"

S = "${WORKDIR}/git"

INSANE_SKIP_${PN} = "already-stripped"
EXTRA_OEMAKE_class-target = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${CC} ${CFLAGS} ${LDFLAGS}" HOSTCC="${BUILD_CC} ${BUILD_CFLAGS} ${BUILD_LDFLAGS}" V=1'
EXTRA_OEMAKE_class-cross = 'ARCH=${TARGET_ARCH} CC="${CC} ${CFLAGS} ${LDFLAGS}" V=1'

inherit uboot-config

do_compile () {
    oe_runmake ${UBOOT_MACHINE_FW_UTILS}
    oe_runmake env
}

do_install () {
    install -d ${D}${base_sbindir} ${D}${sysconfdir}
    install -m 755 ${S}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    ln -s fw_printenv ${D}${base_sbindir}/fw_setenv
    install -m 644 ${WORKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/
    install -m 644 ${WORKDIR}/fw_env_mmcblk0boot0.config ${D}${sysconfdir}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
