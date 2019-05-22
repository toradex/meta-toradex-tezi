# Add a compressed kernel
KERNEL_IMAGETYPES += " Image.gz"
do_install_prepend() {
    if [ -e ${KERNEL_OUTPUT_DIR}/Image.gz ]; then
	rm ${KERNEL_OUTPUT_DIR}/Image.gz
    fi
    gzip -k ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE}
}
