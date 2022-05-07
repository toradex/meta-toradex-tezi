# Set a variable in ${B}/.config
# $1 - Configure variable to be set
# $2 - value [n/y/value]
kernel_configure_variable() {
    # Remove the config
    sed -i -e "/CONFIG_$1[ =]/d" ${B}/.config
    if [ "$2" = "n" ]; then
        echo "# CONFIG_$1 is not set" >> ${B}/.config
    else
        echo "CONFIG_$1=$2" >> ${B}/.config
    fi
}

kernel_do_deploy:append () {
	# Create tezi.itb symbolic link
	if echo ${KERNEL_IMAGETYPES} | grep -wq "fitImage"; then
		if [ -n "${INITRAMFS_IMAGE}" ]; then
			ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}.bin ${DEPLOYDIR}/tezi.itb
		fi
	fi
}
