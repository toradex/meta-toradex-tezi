# cope with the wrong order of _append and and _pn-mesa
# in conf/distro/include/angstrom-mesa-tweaks.inc

# add the original mesa PACKAGECONFIG settings
PACKAGECONFIG_pn-mesa_append = " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'wayland vulkan', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'opengl egl gles gbm dri', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 opengl', 'x11', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 vulkan', 'dri3', '', d)} \
"

# meta-freescale/recipes-graphics/mesa/mesa_%.bbappend
PACKAGECONFIG_pn-mesa_remove_imxgpu2d = "egl gles"

# meta-toradex-tegra/recipes-graphics/mesa/mesa_%.bbappend
PACKAGECONFIG_pn-mesa_remove_tegra = "egl gles"
PACKAGECONFIG_pn-mesa_append_tegra124m = " dri3 egl gles gallium gbm "
