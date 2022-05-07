SUMMARY = "drm_info - dump info about DRM devices"
DESCRIPTION = "Small utility to dump info about DRM devices"
HOMEPAGE = "https://github.com/ascent12/drm_info"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=32fd56d355bd6a61017655d8da26b67c"

SRC_URI = "git://github.com/ascent12/drm_info.git;branch=master;protocol=https \
    file://0001-fix-cross-compilation.patch \
"

SRCREV = "a2aee4ab10ecfdd6d059aa2f5f42316fcda6c3eb"

S = "${WORKDIR}/git"

inherit meson pkgconfig

EXTRA_OEMESON:class-target = "-Dpkgconfig-sysroot-path=${PKG_CONFIG_SYSROOT_DIR}"


DEPENDS = "drm json-c"

