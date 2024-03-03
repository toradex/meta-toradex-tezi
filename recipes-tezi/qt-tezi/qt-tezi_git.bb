DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://gitlab.toradex.int/rd/tezi/qt-tezi.git;branch=${SRCBRANCH};protocol=http \
    file://rc.local \
    file://udhcpd.conf \
"

SRCREV = "392fd7140494c732b82e172f9549f78240e71bd9"
SRCBRANCH = "tezi5"
SRCREV_use-head-next = "${AUTOREV}"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=41609b911f0c746afdabad42336840b5"

PV = "${TDX_VERSION}+git${SRCPV}"

S = "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE_append = " DEFINES+=VERSION_NUMBER=\\\\\\\"${TDX_VERSION}\\\\\\\""

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

RDEPENDS_${PN} += " \
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

FILES_${PN} += " \
    ${datadir}/tezi \
"

do_install() {
    install -d ${D}${bindir}/
    install -m 0755 ${S}/../build/tezi ${D}${bindir}/
    install -d ${D}${datadir}/tezi/keymaps/
    install -m 0644 ${S}/keymaps/*qmap ${D}${datadir}/tezi/keymaps/

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/rc.local ${D}${sysconfdir}

    install -d ${D}${sysconfdir}
    install -m 0755 ${WORKDIR}/udhcpd.conf ${D}${sysconfdir}

}
