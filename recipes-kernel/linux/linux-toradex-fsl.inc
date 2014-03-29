DESCRIPTION = "Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel siteinfo

# Try to build & install perf
#require recipes-kernel/linux/linux-tools.inc

RPSRC = "http://www.rpsys.net/openzaurus/patches/archive"

# Enable OABI compat for people stuck with obsolete userspace
# ARM_KEEP_OABI ?= "1"

# Quirk for udev greater or equal 141
UDEV_GE_141 ?= "0"

# Kernel bootlogo is distro-specific (default is OE logo).
# Logo resolution (qvga, vga, ...) is machine-specific.
LOGO_SIZE ?= "."

# Support for binary device tree generation

FILES_kernel-devicetree = "/boot/devicetree*"

KERNEL_DEVICETREE_boc01 = "${WORKDIR}/boc01.dts"
KERNEL_DEVICETREE_calamari = "arch/${ARCH}/boot/dts/mpc8536ds.dts"
KERNEL_DEVICETREE_canyonlands = "arch/${ARCH}/boot/dts/canyonlands.dts"
KERNEL_DEVICETREE_kilauea = "arch/${ARCH}/boot/dts/kilauea.dts"
KERNEL_DEVICETREE_lite5200 = "arch/${ARCH}/boot/dts/lite5200.dts"
KERNEL_DEVICETREE_lsppchd = "arch/${ARCH}/boot/dts/kuroboxHD.dts"
KERNEL_DEVICETREE_lsppchg = "arch/${ARCH}/boot/dts/kuroboxHG.dts"
KERNEL_DEVICETREE_mpc8313e-rdb = "arch/${ARCH}/boot/dts/mpc8313erdb.dts"
KERNEL_DEVICETREE_mpc8315e-rdb = "arch/${ARCH}/boot/dts/mpc8315erdb.dts"
KERNEL_DEVICETREE_mpc8323e-rdb = "arch/${ARCH}/boot/dts/mpc832x_rdb.dts"
KERNEL_DEVICETREE_mpc8544ds = "arch/${ARCH}/boot/dts/mpc8544ds.dts"
KERNEL_DEVICETREE_mpc8641-hpcn = "arch/${ARCH}/boot/dts/mpc8641_hpcn.dts"
KERNEL_DEVICETREE_p1020rdb = "arch/${ARCH}/boot/dts/p1020rdb.dts"
KERNEL_DEVICETREE_p2020ds = "arch/${ARCH}/boot/dts/p2020ds.dts"
KERNEL_DEVICETREE_sequoia = "arch/${ARCH}/boot/dts/sequoia.dts"
KERNEL_DEVICETREE_tqm8540 = "arch/${ARCH}/boot/dts/tqm8540.dts"
KERNEL_DEVICETREE_xilinx-ml507 = "arch/${ARCH}/boot/dts/virtex440-ml507.dts"

KERNEL_DEVICETREE_FLAGS = "-R 8 -S 0x3000"

CORTEXA8FIXUP ?= "yes"

python __anonymous () {

    import bb
    
    devicetree = bb.data.getVar('KERNEL_DEVICETREE', d, 1) or ''
    if devicetree:
    	depends = bb.data.getVar("DEPENDS", d, 1)
    	bb.data.setVar("DEPENDS", "%s dtc-native" % depends, d)
    	packages = bb.data.getVar("PACKAGES", d, 1)
    	bb.data.setVar("PACKAGES", "%s kernel-devicetree" % packages, d)
}

do_configure_append() {
    if test -e scripts/Makefile.fwinst ; then
        sed -i -e "s:-m0644:-m 0644:g" scripts/Makefile.fwinst
    fi
}

# bitbake.conf only prepends PARALLEL make in tasks called do_compile, which isn't the case for compile_modules
# So explicitly enable it for that in here
EXTRA_OEMAKE = "${PARALLEL_MAKE} "

do_install_append() {
	oe_runmake headers_install INSTALL_HDR_PATH=${D}${exec_prefix}/src/linux-${KERNEL_VERSION} ARCH=$ARCH
}

PACKAGES =+ "kernel-headers"
FILES_kernel-headers = "${exec_prefix}/src/linux*"

do_devicetree_image() {
    if test -n "${KERNEL_DEVICETREE}" ; then
        dtc -I dts -O dtb ${KERNEL_DEVICETREE_FLAGS} -o devicetree ${KERNEL_DEVICETREE}
        install -d ${D}/boot
        install -m 0644 devicetree ${D}/boot/devicetree-${KERNEL_VERSION}
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 0644 devicetree ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE_BASE_NAME}.dtb
        package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE_BASE_NAME}.dtb
        cd ${DEPLOY_DIR_IMAGE}
        rm -f ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
        ln -sf ${KERNEL_IMAGE_BASE_NAME}.dtb ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
        package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE_SYMLINK_NAME}.dtb
    fi
}

addtask devicetree_image after do_install before do_package do_deploy

pkg_postinst_kernel-devicetree () {
	cd /${KERNEL_IMAGEDEST}; update-alternatives --install /${KERNEL_IMAGEDEST}/devicetree devicetree devicetree-${KERNEL_VERSION} ${KERNEL_PRIORITY} || true
}

pkg_postrm_kernel-devicetree () {
	cd /${KERNEL_IMAGEDEST}; update-alternatives --remove devicetree devicetree-${KERNEL_VERSION} || true
}

# Automatically depend on lzop-native if CONFIG_KERNEL_LZO is enabled
python () {
    try:
        defconfig = bb.fetch2.localpath('file://defconfig', d)
    except bb.fetch2.FetchError:
        return

    try:
        configfile = open(defconfig)
    except IOError:
        return

    if 'CONFIG_KERNEL_LZO=y\n' in configfile.readlines():
        depends = d.getVar('DEPENDS', False)
        d.setVar('DEPENDS', depends + ' lzop-native')
}
