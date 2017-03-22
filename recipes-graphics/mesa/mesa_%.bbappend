# cope with the wrong order of _append and and _pn-mesa
# in conf/distro/include/angstrom-mesa-tweaks.inc

# add the original mesa PACKAGECONFIG settings
PACKAGECONFIG_pn-mesa_append = " gbm egl gles dri ${MESA_CRYPTO} ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} "

# meta-freescale/recipes-graphics/mesa/mesa_%.bbappend
PACKAGECONFIG_pn-mesa_remove_imxgpu2d = "egl gles"

# meta-toradex-tegra/recipes-graphics/mesa/mesa_%.bbappend
PACKAGECONFIG_pn-mesa_remove_tegra = "egl gles"
PACKAGECONFIG_pn-mesa_append_tegra124m = " dri3 egl gles gallium gbm "
