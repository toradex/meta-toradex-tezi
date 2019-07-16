SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70 \
                    file://libweston/compositor.c;endline=27;md5=6c53bbbd99273f4f7c4affa855c33c0a"

SRC_URI = "https://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
           file://weston.png \
           file://weston.desktop \
           file://0001-make-error-portable.patch \
           file://xwayland.weston-start \
           file://0001-weston-launch-Provide-a-default-version-that-doesn-t.patch \
           file://find-wayland-scanner.patch \
           file://auto-enable-screenshare.patch \
           file://0001-backend-rdp-allow-to-force-compression-off.patch \
           file://0001-backend-rdp-fix-memory-leak.patch \
           file://0001-pixman-avoid-unnecessary-y-flip-for-screen-capture.patch \
           file://0001-screen-share-destroy-seat-on-remove.patch \
           file://0001-desktop-shell-make-sure-child-window-stays-active.patch \
           file://0001-backend-rdp-release-seat-on-peer-disconnect.patch \
           file://wallpaper.png \
"
SRC_URI[md5sum] = "7c634e262f8a464a076c97fd50ad36b3"
SRC_URI[sha256sum] = "546323a90607b3bd7f48809ea9d76e64cd09718102f2deca6d95aa59a882e612"

UPSTREAM_CHECK_URI = "https://wayland.freedesktop.org/releases.html"

inherit meson pkgconfig useradd distro_features_check

export CFLAGS_class-target = "${TARGET_CFLAGS}"
export CPPFLAGS_class-target = "${TARGET_CPPFLAGS}"
export CXXFLAGS_class-target = "${TARGET_CXXFLAGS}"
export LDFLAGS_class-target = "${TARGET_LDFLAGS}"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg drm freerdp"
DEPENDS += "wayland wayland-protocols libinput pango wayland-native"

WESTON_MAJOR_VERSION = "${@'.'.join(d.getVar('PV').split('.')[0:1])}"
WESTON_MAJOR_VERSION = "6"

EXTRA_OEMESON += "-Dbackend-rdp=true -Dsimple-dmabuf-drm= -Dbackend-default=fbdev"
EXTRA_OECONF_append_qemux86 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
EXTRA_OECONF_append_qemux86-64 = "\
		WESTON_NATIVE_BACKEND=fbdev-backend.so \
		"
PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'fbdev', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'xwayland', '', d)} \
                   ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "-Dbackend-drm=true,-Dbackend-drm=false,drm udev mtdev virtual/mesa"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "-Dbackend-wayland=true,-Dbackend-wayland=false,virtual/mesa"
# Weston on X11
PACKAGECONFIG[x11] = "-Dbackend-x11=true,-Dbackend-x11=false,virtual/libx11 libxcb libxcb libxcursor cairo"
# Headless Weston
PACKAGECONFIG[headless] = "-Dbackend-headless=true,-Dbackend-headless=false"
# Weston on framebuffer
PACKAGECONFIG[fbdev] = "-Dbackend-fbdev=true,-Dbackend-fbdev=false,udev mtdev"
# weston-launch
PACKAGECONFIG[launch] = "-Dweston-launch=true,-Dweston-launch=false,drm pam"
# VA-API desktop recorder
PACKAGECONFIG[vaapi] = "-Dbackend-drm-screencast-vaapi=true,-Dbackend-drm-screencast-vaapi=false,libva"
# Weston with EGL support
PACKAGECONFIG[egl] = "-Drenderer-gl=true,-Drenderer-gl=false,virtual/egl"
# Weston with lcms support
PACKAGECONFIG[lcms] = "-Dcolor-management-lcms=true,-Dcolor-management-lcms=false,lcms"
# Weston with webp support
PACKAGECONFIG[webp] = "-Dimage-webp=true,-Dimage-webp=false,libwebp"
# Weston with systemd-login support
PACKAGECONFIG[systemd] = "-Dsystemd=true -Dlauncher-logind=true,-Dsystemd=false -D=launcher-logind=false,systemd dbus"
# Weston with Xwayland support (requires X11 and Wayland)
PACKAGECONFIG[xwayland] = "-Dxwayland=true,-Dxwayland=false"
# colord CMS support
PACKAGECONFIG[colord] = "-Dcolor-management-colord=true,-Dcolor-management-colord=false"
# Clients support
PACKAGECONFIG[clients] = "-Dsimple-clients=all,-Dsimple-clients="
# Demo clients
PACKAGECONFIG[demo] = "-Ddemo-clients=true,-Ddemo-clients=false"
# Virtual remote output with GStreamer on DRM backend
PACKAGECONFIG[remoting] = "-Dremoting=true,-Dremoting=false,gstreamer-1.0"

do_install_append() {
	# Weston doesn't need the .la files to load modules, so wipe them
	rm -f ${D}/${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.la

	# If X11, ship a desktop file to launch it
	if [ "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}" ]; then
		install -d ${D}${datadir}/applications
		install ${WORKDIR}/weston.desktop ${D}${datadir}/applications

		install -d ${D}${datadir}/icons/hicolor/48x48/apps
		install ${WORKDIR}/weston.png ${D}${datadir}/icons/hicolor/48x48/apps
	fi

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', 'yes', 'no', d)}" = "yes" ]; then
		install -Dm 644 ${WORKDIR}/xwayland.weston-start ${D}${datadir}/weston-start/xwayland
	fi

	install ${WORKDIR}/wallpaper.png ${D}${datadir}/weston
}

PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', '${PN}-xwayland', '', d)} \
             libweston-${WESTON_MAJOR_VERSION} ${PN}-examples"

FILES_${PN} = "${bindir}/weston ${bindir}/weston-terminal ${bindir}/weston-info ${bindir}/weston-launch ${bindir}/wcap-decode ${libexecdir} ${libdir}/${BPN}/*.so ${datadir}"

FILES_libweston-${WESTON_MAJOR_VERSION} = "${libdir}/lib*${SOLIBS} ${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.so"
SUMMARY_libweston-${WESTON_MAJOR_VERSION} = "Helper library for implementing 'wayland window managers'."

FILES_${PN}-examples = "${bindir}/*"

FILES_${PN}-xwayland = "${libdir}/libweston-${WESTON_MAJOR_VERSION}/xwayland.so"
RDEPENDS_${PN}-xwayland += "xserver-xorg-xwayland"

RDEPENDS_${PN} += "xkeyboard-config weston-config"
RRECOMMENDS_${PN} = "liberation-fonts"
RRECOMMENDS_${PN}-dev += "wayland-protocols"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system weston-launch"
