DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://gitlab.int.toradex.com/rd/tezi/qt-tezi.git;branch=${SRCBRANCH};protocol=https \
    file://rc.local \
    file://udhcpd.conf \
"

SRCREV = "561041c64a9cff6461b8b6ea1fa40698f8a9edfb"
SRCBRANCH = "master"
SRCREV:use-head-next = "${AUTOREV}"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=41609b911f0c746afdabad42336840b5"

PV = "${TDX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE:append = " DEFINES+=VERSION_NUMBER=\\\\\\\"${TDX_VERSION}\\\\\\\""

QMAKE_PROFILES = "${S}/tezi.pro"

inherit qmake5 pkgconfig

DEPENDS += " \
    libusbgx \
    rapidjson \
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
    install -m 0755 ${S}/../build/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/

    install -d ${D}${sysconfdir}
    install -m 0755 ${UNPACKDIR}/rc.local ${D}${sysconfdir}

    install -d ${D}${sysconfdir}
    install -m 0755 ${UNPACKDIR}/udhcpd.conf ${D}${sysconfdir}

}
