inherit kernel
require recipes-kernel/linux/linux.inc

LINUX_VERSION ?= "3.1.10"

SRCREV = "37440f3ed07a6f588b05b8f98d0b3025c1949371"

PV = "${LINUX_VERSION}+gitr${SRCREV}"
PR = "V1.1b1"
S = "${WORKDIR}/git"
#SRC_URI = "\
#  git://gitorious.org/colibri-t20-embedded-linux-bsp/colibri_t20-linux-kernel.git;protocol=git;branch=master \
#  file://bcm4329_warning.patch "
SRC_URI = "\
  git://git.toradex.com/linux-colibri.git;protocol=git;branch=colibri \
  file://bcm4329_warning.patch "

#SVN_REV = 190
#PV = "2.6.36.2"
#PR = "${SVN_REV}"
#S = "${WORKDIR}/kernel"
#SRC_URI = "svn://tegradev:tegra123!@mammut.toradex.int:8090/colibri_tegra_linux/trunk;module=kernel;rev=${SVN_REV};proto=http \
#	file://bcm4329_warning.patch "

COMPATIBLE_MACHINE = "colibri-t20"

CMDLINE="mem=148M@0M fbmem=12M@148M nvmem=96M@160M vmalloc=248M video=tegrafb root=/dev/nfs ip=:::::eth0:on rw netdevwait mtdparts=tegra_nand:1018368K@28160K(userspace) console=ttyS0,115200n8 usb_high_speed=0"

do_configure_prepend_colibri-t20() {
	#use the defconfig provided in the kernel source tree
	install -m 0644 ${S}/arch/arm/configs/colibri_t20_defconfig ${WORKDIR}/defconfig

	#compile with -O2, 4.5. compiler seems to fail with -Os
#	sed -i -e /CONFIG_CC_OPTIMIZE_FOR_SIZE/d ${WORKDIR}/defconfig
#	echo "CONFIG_CC_OPTIMIZE_FOR_SIZE=n" >> ${WORKDIR}/defconfig

	#compile with -mno-unaligned-access, with 4.7 compiler the kernel fails 
	echo "KBUILD_CFLAGS   += -mno-unaligned-access" >> ${S}/Makefile
}

do_compile_kernelmodules_colibri-t20() {
	:
}

#require recipes-kernel/linux/linux-tools.inc

