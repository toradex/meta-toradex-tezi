kernel_do_deploy:append () {
	# Create tezi.itb symbolic link
	if echo ${KERNEL_IMAGETYPES} | grep -wq "fitImage"; then
		if [ -n "${INITRAMFS_IMAGE}" ]; then
			ln -snf fitImage-${INITRAMFS_IMAGE_NAME}-${KERNEL_FIT_NAME}.bin ${DEPLOYDIR}/tezi.itb
		fi
	fi
}
