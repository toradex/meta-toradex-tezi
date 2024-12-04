SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70 \
                    file://libweston/compositor.c;endline=27;md5=6c53bbbd99273f4f7c4affa855c33c0a"

SRC_URI = "https://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
           file://weston.png \
           file://weston.desktop \
           file://xwayland.weston-start \
           file://0001-weston-launch-Provide-a-default-version-that-doesn-t.patch \
           file://0001-tests-include-fcntl.h-for-open-O_RDWR-O_CLOEXEC-and-.patch \
           file://0001-backend-vnc-add-VNC-support-using-Neat-VNC-library.patch \
           file://0001-screen-share-auto-enable-screen-share-on-startup.patch \
           file://0001-libweston-properly-track-the-overlapping-output-dama.patch \
           file://0002-libweston-per-output-view-damage-tracking.patch \
           file://0003-libweston-remove-weston_plane.damage.patch \
           file://0004-drm-backend-removing-the-restriction-of-exclusively-.patch \
           file://0005-libweston-enable-API-to-make-output-a-slave-to-anoth.patch \
           file://0006-compositor-new-algorithm-to-enable-output.patch \
           file://0001-backend-vnc-drop-plane-damage-substraction.patch \
           file://0001-vnc-backend-ignore-SIGPIPE-signal.patch \
           file://0001-vnc-backend-workaround-for-seat-releasing.patch \
          "

SRC_URI[sha256sum] = "5cf5d6ce192e0eb15c1fc861a436bf21b5bb3b91dbdabbdebe83e1f83aa098fe"

UPSTREAM_CHECK_URI = "https://wayland.freedesktop.org/releases.html"

inherit meson pkgconfig useradd

export CFLAGS:class-target = "${TARGET_CFLAGS}"
export CPPFLAGS:class-target = "${TARGET_CPPFLAGS}"
export CXXFLAGS:class-target = "${TARGET_CXXFLAGS}"
export LDFLAGS:class-target = "${TARGET_LDFLAGS}"

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg drm neatvnc"
DEPENDS += "wayland wayland-protocols libinput pango wayland-native"

WESTON_MAJOR_VERSION = "${@'.'.join(d.getVar('PV').split('.')[0:1])}"

EXTRA_OEMESON += "-Dbackend-vnc=true -Dbackend-default=drm -Drenderer-gl=false \
		-Dpipewire=false -Dbackend-rdp=false \
		"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'xwayland', '', d)} \
                   ${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "-Dbackend-drm=true,-Dbackend-drm=false,drm udev mtdev"
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
PACKAGECONFIG[systemd] = "-Dsystemd=true -Dlauncher-logind=true,-Dsystemd=false -Dlauncher-logind=false,systemd dbus"
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
# Weston with PAM support
PACKAGECONFIG[pam] = "-Dpam=true,-Dpam=false,libpam"

do_install:append() {
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

	if [ "${@bb.utils.contains('PACKAGECONFIG', 'launch', 'yes', 'no', d)}" = "yes" ]; then
		chmod u+s ${D}${bindir}/weston-launch
	fi
}

PACKAGES += "${@bb.utils.contains('PACKAGECONFIG', 'xwayland', '${PN}-xwayland', '', d)} \
             libweston-${WESTON_MAJOR_VERSION} ${PN}-examples"

FILES:${PN}-dev += "${libdir}/${BPN}/libexec_weston.so"
FILES:${PN} = "${bindir}/weston ${bindir}/weston-launch ${bindir}/wcap-decode ${libexecdir} ${libdir}/${BPN}/*.so* ${datadir}"

FILES:libweston-${WESTON_MAJOR_VERSION} = "${libdir}/lib*${SOLIBS} ${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.so"
SUMMARY:libweston-${WESTON_MAJOR_VERSION} = "Helper library for implementing 'wayland window managers'."

FILES:${PN}-examples = "${bindir}/*"

FILES:${PN}-xwayland = "${libdir}/libweston-${WESTON_MAJOR_VERSION}/xwayland.so"
RDEPENDS:${PN}-xwayland += "xserver-xorg-xwayland"

RDEPENDS:${PN} += "xkeyboard-config"
RRECOMMENDS:${PN} = "weston-init ttf-bitstream-vera"
RRECOMMENDS:${PN}-dev += "wayland-protocols"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system weston-launch"
