# meta package to pull in the libries, but not the (huge) samples
PACKAGES =+ "gpu-viv-bin-mx6q-libraries "
FILES_gpu-viv-bin-mx6q-libraries = ""
ALLOW_EMPTY_gpu-viv-bin-mx6q-libraries = "1"
RDEPENDS_gpu-viv-bin-mx6q-libraries = " \
    libclc-mx6 \
    libegl-mx6 \
    libgles-mx6 \
    libgles2-mx6 \
    libglslc-mx6 \
    libopencl-mx6 \
    libopenvg-mx6 \
    libvdk-mx6 \
"