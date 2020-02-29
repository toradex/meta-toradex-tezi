DESCRIPTION = "Toradex Easy Installer UI"

HOMEPAGE = "http://www.toradex.com"

LICENSE = "BSD-3-Clause"

SRC_URI = "git://gitlab.toradex.int/rd/tezi/qt-tezi.git;branch=${SRCBRANCH};protocol=http \
    file://rc.local \
    file://udhcpd.conf \
"

SRCREV = "ed91e48a3f17cf1886fc2847cebbe37a5afffbdd"
SRCBRANCH = "master"
SRCREV_use-head-next = "${AUTOREV}"
SRCBRANCH_use-head-next = "master"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=81f0d32e0eab9775391c3bdeb681aadb"

S = "${WORKDIR}/git"

EXTRA_QMAKEVARS_PRE_append = " DEFINES+=VERSION_NUMBER=\\\\\\\"${TDX_VER_PACKAGE_MIN}\\\\\\\""

inherit qmake5

DEPENDS += " \
    libusbgx \
    rapidjson \
    qtwayland \
    qtzeroconf \
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
    tezi-version \
    u-boot-fw-utils \
    unzip \
    util-linux-blkdiscard \
    util-linux-blkid \
    util-linux-sfdisk \
    weston \
    xz \
    zstd \
"

FILES_${PN} = " \
    ${sysconfdir} \
    ${sysconfdir}/rc.local \
    ${sysconfdir}/default/tezi \
    ${sysconfdir}/udhcpd.conf \
    ${bindir} \
    ${datadir}/tezi/ \
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