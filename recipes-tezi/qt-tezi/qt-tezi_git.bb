DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://github.com/toradex/qt-tezi.git;branch=${SRCBRANCH};protocol=https \
    file://rc.local \
    file://udhcpd.conf \
"

SRCREV = "4f4306ca7db8aa3aab077653b4b4cdaeab68c05a"
SRCBRANCH = "master"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=41609b911f0c746afdabad42336840b5"

PV = "${TDX_VERSION}+git"

EXTRA_QMAKEVARS_PRE:append = " DEFINES+=VERSION_NUMBER=\\\\\\\"${TDX_RELEASE}\\\\\\\""

QMAKE_PROFILES = "${S}/tezi.pro"

inherit qmake5 pkgconfig

DEPENDS += " \
    libusbgx \
    rapidjson \
    valijson \
    qtbase \
    qtwayland \
    qtzeroconf \
    qttools-native \
    qhttp \
"

RDEPENDS:${PN} += " \
    avahi-daemon \
    avahi-utils \
    curl \
    dosfstools \
    e2fsprogs-mke2fs \
    e2fsprogs-tune2fs \
    flash-wince \
    lz4 \
    lzop \
    mmc-utils \
    pv \
    qtwayland-plugins \
    tar \
    u-boot-fw-utils \
    unzip \
    util-linux-blkdiscard \
    util-linux-blkid \
    util-linux-sfdisk \
    weston \
    xz \
    zstd \
"

FILES:${PN} += " \
    ${datadir}/tezi \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${B}/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/

    install -d ${D}${sysconfdir}
    install -m 0755 ${UNPACKDIR}/rc.local ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/udhcpd.conf ${D}${sysconfdir}
}
