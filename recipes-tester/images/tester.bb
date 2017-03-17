require tester.inc

SUMMARY="Toradex Tester initramfs"

# We are using the rootfs as initramfs
IMAGE_FSTYPES="cpio.gz squashfs"
IMAGE_FEATURES+="debug-tweaks read-only-rootfs"
