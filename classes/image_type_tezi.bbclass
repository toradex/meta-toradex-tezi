inherit image_types

IMAGE_DEPENDS_teziimg = "tezi-metadata:do_deploy virtual/bootloader"

UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"

def rootfs_get_size(d):
    import subprocess

    # Calculate size of rootfs in kilobytes...
    output = subprocess.check_output(['du', '-ks',
                                      d.getVar('IMAGE_ROOTFS', True)])
    return int(output.split()[0])

def rootfs_tezi_emmc(d):
    import subprocess
    from collections import OrderedDict
    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)
    kernel = d.getVar('KERNEL_IMAGETYPE', True)
    offset_bootrom = d.getVar('OFFSET_BOOTROM_PAYLOAD', True)
    offset_spl = d.getVar('OFFSET_SPL_PAYLOAD', True)
    imagename = d.getVar('IMAGE_NAME', True)

    # Calculate size of bootfs...
    bootfiles = [ os.path.join(deploydir, kernel) ]
    for dtb in d.getVar('KERNEL_DEVICETREE', True).split():
        bootfiles.append(os.path.join(deploydir, kernel + "-" + dtb))

    args = ['du', '-kLc']
    args.extend(bootfiles)
    output = subprocess.check_output(args)
    bootfssize_kb = int(output.splitlines()[-1].split()[0])

    bootpart_rawfiles = []

    has_spl = d.getVar('SPL_BINARY', True)
    if has_spl:
        bootpart_rawfiles.append(
              {
                "filename": d.getVar('SPL_BINARY', True),
                "dd_options": "seek=" + offset_bootrom
              })
    bootpart_rawfiles.append(
              {
                "filename": d.getVar('UBOOT_BINARY', True),
                "dd_options": "seek=" + (offset_spl if has_spl else offset_bootrom)
              })

    return [
        OrderedDict({
          "name": "mmcblk0",
          "partitions": [
            {
              "partition_size_nominal": 16,
              "want_maximised": False,
              "content": {
                "label": "BOOT",
                "filesystem_type": "FAT",
                "mkfs_options": "",
                "filename": imagename + ".bootfs.tar.xz",
                "uncompressed_size": bootfssize_kb / 1024
              }
            },
            {
              "partition_size_nominal": 512,
              "want_maximised": True,
              "content": {
                "label": "RFS",
                "filesystem_type": "ext4",
                "mkfs_options": "-E nodiscard",
                "filename": imagename + ".rootfs.tar.xz",
                "uncompressed_size": rootfs_get_size(d) / 1024
              }
            }
          ]
        }),
        OrderedDict({
          "name": "mmcblk0boot0",
          "content": {
            "filesystem_type": "raw",
            "rawfiles": bootpart_rawfiles
          }
        })]


def rootfs_tezi_rawnand(d):
    from collections import OrderedDict
    imagename = d.getVar('IMAGE_NAME', True)

    # Use device tree mapping to create product id <-> device tree relationship
    dtmapping = d.getVarFlags('TORADEX_PRODUCT_IDS')
    dtfiles = []
    for f, v in dtmapping.items():
        dtfiles.append({ "filename": v, "product_ids": f })

    return [
        OrderedDict({
          "name": "u-boot1",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY', True),
              "size": 1
            }
          },
        }),
        OrderedDict({
          "name": "u-boot2",
          "content": {
            "rawfile": {
              "filename": d.getVar('UBOOT_BINARY', True),
              "size": 1
            }
          }
        }),
        OrderedDict({
          "name": "ubi",
          "ubivolumes": [
            {
              "name": "kernel",
              "size_kib": 8192,
              "type": "static",
              "content": {
                "rawfile": {
                  "filename": d.getVar('KERNEL_IMAGETYPE', True),
                  "size": 5
                }
              }
            },
            {
              "name": "dtb",
              "content": {
                "rawfiles": dtfiles
              },
              "size_kib": 128,
              "type": "static"
            },
            {
              "name": "m4firmware",
              "size_kib": 896,
              "type": "static"
            },
            {
              "name": "rootfs",
              "content": {
                "filesystem_type": "ubifs",
                "filename": imagename + ".rootfs.tar.xz",
                "uncompressed_size": rootfs_get_size(d) / 1024
              }
            }
          ]
        })]

python rootfs_tezi_json() {
    import json
    from collections import OrderedDict

    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)

    data = OrderedDict({ "config_format": 1, "autoinstall": False })

    # Use image recipes SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY', True)
    data["description"] = d.getVar('DESCRIPTION', True)
    data["version"] = d.getVar('PV', True)
    data["release_date"] = d.getVar('DATE', True)
    if os.path.exists(os.path.join(deploydir, "prepare.sh")):
        data["prepare_script"] = "prepare.sh"
    if os.path.exists(os.path.join(deploydir, "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "marketing.tar")):
        data["marketing"] = "marketing.tar"
    if os.path.exists(os.path.join(deploydir, "toradexlinux.png")):
        data["icon"] = "toradexlinux.png"

    product_ids = d.getVar('TORADEX_PRODUCT_IDS', True)
    if product_ids is None:
        bb.fatal("Supported Toradex product ids missing, assign TORADEX_PRODUCT_IDS with a list of product ids.")

    data["supported_product_ids"] = d.getVar('TORADEX_PRODUCT_IDS', True).split()

    if bb.utils.contains("TORADEX_FLASH_TYPE", "rawnand", True, False, d):
        data["mtddevs"] = rootfs_tezi_rawnand(d)
    else:
        data["blockdevs"] = rootfs_tezi_emmc(d)

    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE', True)
    with open(os.path.join(deploy_dir, 'image.json'), 'w') as outfile:
        json.dump(data, outfile, indent=4)
    bb.note("Toradex Easy Installer metadata file image.json written.")
}

python () {
    if bb.utils.contains("IMAGE_FSTYPES", "teziimg", True, False, d):
        # do_image_teziimg does not work because the anonymous python function
        # in image.bbclass gets called later and overwrites prefuncs. In master
        # this is fixed, so we might be able to get rid of this work around in rocko
        d.appendVarFlag('do_image', 'prefuncs', 'rootfs_tezi_json')
}

IMAGE_CMD_teziimg () {
	bbnote "Create bootfs tarball"

	# Create list of device tree files
	if test -n "${KERNEL_DEVICETREE}"; then
		for DTS_FILE in ${KERNEL_DEVICETREE}; do
			DTS_BASE_NAME=`basename ${DTS_FILE} .dtb`
			if [ -e "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb" ]; then
				KERNEL_DEVICETREE_FILES="${KERNEL_DEVICETREE_FILES} ${KERNEL_IMAGETYPE}-${DTS_BASE_NAME}.dtb"
			else
				bbfatal "${DTS_FILE} does not exist."
			fi
		done
	fi

	cd ${DEPLOY_DIR_IMAGE}

	case "${TORADEX_FLASH_TYPE}" in
		rawnand)
		# The first transform strips all folders from the files to tar, the
		# second transform "moves" them in a subfolder ${IMAGE_NAME}_${PV}.
		# The third transform removes zImage from the device tree.
		${IMAGE_CMD_TAR} --transform='s/.*\///' --transform 's,^,${IMAGE_NAME}_${PV}/,' --transform="flags=r;s|${KERNEL_IMAGETYPE}-||" -chf ${IMGDEPLOYDIR}/${IMAGE_NAME}_${PV}${TDX_VERDATE}.tar image.json toradexlinux.png marketing.tar prepare.sh wrapup.sh ${SPL_BINARY} ${UBOOT_BINARY} ${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE_FILES} ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.tar.xz
		;;
		*)
		# Create bootfs...
		${IMAGE_CMD_TAR} --transform="flags=r;s|${KERNEL_IMAGETYPE}-||" -chf ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar -C ${DEPLOY_DIR_IMAGE} ${KERNEL_IMAGETYPE} ${KERNEL_DEVICETREE_FILES}
		xz -f -k -c ${XZ_COMPRESSION_LEVEL} ${XZ_THREADS} --check=${XZ_INTEGRITY_CHECK} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar > ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz

		# The first transform strips all folders from the files to tar, the
		# second transform "moves" them in a subfolder ${IMAGE_NAME}_${PV}.
		${IMAGE_CMD_TAR} --transform='s/.*\///' --transform 's,^,${IMAGE_NAME}_${PV}/,' -chf ${IMGDEPLOYDIR}/${IMAGE_NAME}_${PV}${TDX_VERDATE}.tar image.json toradexlinux.png marketing.tar prepare.sh wrapup.sh ${SPL_BINARY} ${UBOOT_BINARY} ${IMGDEPLOYDIR}/${IMAGE_NAME}.bootfs.tar.xz ${IMGDEPLOYDIR}/${IMAGE_NAME}.rootfs.tar.xz
		;;
	esac
}

IMAGE_TYPEDEP_teziimg += "tar.xz"
