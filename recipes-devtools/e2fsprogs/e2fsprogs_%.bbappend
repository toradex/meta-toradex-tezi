FILESEXTRAPATHS:prepend := "${THISDIR}/e2fsprogs:"

SRC_URI:append = " file://0001-Revert-mke2fs.conf-enable-the-metadata_csum_seed-and.patch"
