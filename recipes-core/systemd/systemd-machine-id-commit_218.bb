SUMMARY = "systemd-machine-id-commit utility"
DESCRIPTION = "If the rootfs is readonly when the init process starts the file\
 /etc/machine-id is created as a tempfs overlay resulting in different machine-id\
 on every boot. The systemd-machine-id-commit utility can be used later on to\
 transfer the tempory file to the real fs. This recipe builds this utility out\
 of the v218 source tree where it is first available. The binary can be used\
 with oe builds using an older systemd"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/systemd"

LICENSE = "GPLv2 & LGPLv2.1 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe \
                    file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE.MIT;md5=544799d0b492f119fa04641d1b8868ed"

PE = "1"

DEPENDS = "kmod docbook-sgml-dtd-4.1-native intltool-native gperf-native acl readline dbus libcap libcgroup glib-2.0 qemu-native util-linux"
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'libpam', '', d)}"

SECTION = "base/shell"

inherit gtk-doc pkgconfig autotools perlnative update-alternatives qemu systemd ptest gettext

SRCREV = "820aced6f6067a6b7c57b7d36e44f64378870cbf"

PV = "218+git${SRCPV}"

SRC_URI  = "git://anongit.freedesktop.org/systemd/systemd;branch=master;protocol=git"
SRC_URI += "file://0001-systemd-machine-id-commit-allow-standalone-build.patch"

S = "${WORKDIR}/git"

LDFLAGS_append_libc-uclibc = " -lrt"

GTKDOC_DOCDIR = "${S}/docs/"

PACKAGECONFIG ??= "xz resolved networkd"
PACKAGECONFIG[journal-upload] = "--enable-libcurl,--disable-libcurl,curl"
# Sign the journal for anti-tampering
PACKAGECONFIG[gcrypt] = "--enable-gcrypt,--disable-gcrypt,libgcrypt"
# regardless of PACKAGECONFIG, libgcrypt is always required to expand
# the AM_PATH_LIBGCRYPT autoconf macro
DEPENDS += "libgcrypt"
# Compress the journal
PACKAGECONFIG[xz] = "--enable-xz,--disable-xz,xz"
PACKAGECONFIG[cryptsetup] = "--enable-libcryptsetup,--disable-libcryptsetup,cryptsetup"

CACHED_CONFIGUREVARS = "ac_cv_path_KILL=${base_bindir}/kill"

# Helper variables to clarify locations.  This mirrors the logic in systemd's
# build system.
rootprefix ?= "${base_prefix}"
rootlibdir ?= "${base_libdir}"
rootlibexecdir = "${rootprefix}/lib"

# The gtk+ tools should get built as a separate recipe e.g. systemd-tools
EXTRA_OECONF = " --with-rootprefix=${rootprefix} \
                 --with-rootlibdir=${rootlibdir} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'pam', '--enable-pam', '--disable-pam', d)} \
                 --disable-manpages \
                 --disable-coredump \
                 --disable-introspection \
                 --disable-kdbus \
                 --enable-split-usr \
                 --without-python \
                 --with-sysvrcnd-path=${sysconfdir} \
                 --with-firmware-path=/lib/firmware \
                 ac_cv_path_KILL=${base_bindir}/kill \
               "
# uclibc does not have NSS
EXTRA_OECONF_append_libc-uclibc = " --disable-myhostname "

do_configure_prepend() {
	export CPP="${HOST_PREFIX}cpp ${TOOLCHAIN_OPTIONS} ${HOST_CC_ARCH}"
	export NM="${HOST_PREFIX}gcc-nm"
	export AR="${HOST_PREFIX}gcc-ar"
	export RANLIB="${HOST_PREFIX}gcc-ranlib"
	export KMOD="${base_bindir}/kmod"
	if [ -d ${S}/units.pre_sed ] ; then
		cp -r ${S}/units.pre_sed ${S}/units
	else
		cp -r ${S}/units ${S}/units.pre_sed
	fi
	sed -i -e 's:=/root:=${ROOT_HOME}:g' ${S}/units/*.service*
	sed -i '/ln --relative --help/d' ${S}/configure.ac
	sed -i -e 's:\$(LN_S) --relative -f:lnr:g' ${S}/Makefile.am
	sed -i -e 's:\$(LN_S) --relative:lnr:g' ${S}/Makefile.am
}

do_compile() {
    oe_runmake src/shared/errno-from-name.h \
        src/shared/errno-to-name.h src/shared/af-from-name.h \
        src/shared/af-to-name.h src/shared/arphrd-from-name.h \
        src/shared/arphrd-to-name.h src/shared/cap-from-name.h \  
        src/shared/cap-to-name.h src/resolve/dns_type-from-name.h \
        src/resolve/dns_type-to-name.h src/test/test-hashmap-ordered.c
    oe_runmake systemd-machine-id-commit
}

do_install() {
	install -d ${D}/${base_bindir}
	install -m 0755 ${B}/systemd-machine-id-commit ${D}/${base_bindir}/systemd-machine-id-commit
}

pkg_postinst_${PN} () {
    # can't do this offline
    if [ "x$D" != "x" ]; then
        exit 1
    fi
    ${base_bindir}/systemd-machine-id-commit
}

# As this recipe builds udev, respect systemd being in DISTRO_FEATURES so
# that we don't build both udev and systemd in world builds.
python () {
    if not bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' not in DISTRO_FEATURES")
}
