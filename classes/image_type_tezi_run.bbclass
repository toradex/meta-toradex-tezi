inherit image_types

# Setting IMAGE_FSTYPES here allows us to overwrite machine defaults
# such as "teziimg" set in machine files.
IMAGE_FSTYPES="tezirunimg"
IMAGE_DEPENDS_tezirunimg = "tezi-run-metadata:do_deploy u-boot-mkimage-native p7zip-native"

python rootfs_tezi_run_json() {
    if not bb.utils.contains("IMAGE_FSTYPES", "tezirunimg", True, False, d):
        return

    import json, subprocess
    from datetime import date
    from collections import OrderedDict

    # Calculate size of rootfs...
    output = subprocess.check_output(['du', '-ks',
                                      d.getVar('IMAGE_ROOTFS', True)])
    rootfssize_kb = int(output.split()[0])

    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)
    kernel = d.getVar('KERNEL_IMAGETYPE', True)

    # Calculate size of bootfs...
    bootfiles = [ os.path.join(deploydir, kernel) ]
    for dtb in d.getVar('KERNEL_DEVICETREE', True).split():
        bootfiles.append(os.path.join(deploydir, kernel + "-" + dtb))

    args = ['du', '-kLc']
    args.extend(bootfiles)
    output = subprocess.check_output(args)
    bootfssize_kb = int(output.splitlines()[-1].split()[0])

    data = OrderedDict({ "config_format": 1, "autoinstall": False, "isinstaller": True })

    # Use image recipies SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY', True)
    data["description"] = d.getVar('DESCRIPTION', True)
    data["version"] = d.getVar('PV', True)
    data["release_date"] = date.isoformat(date.today())
    if os.path.exists(os.path.join(deploydir, "prepare.sh")):
        data["prepare_script"] = "prepare.sh"
    if os.path.exists(os.path.join(deploydir, "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "marketing.tar")):
        data["marketing"] = "marketing.tar"
    if os.path.exists(os.path.join(deploydir, "tezi.png")):
        data["icon"] = "tezi.png"

    product_ids = d.getVar('TORADEX_PRODUCT_IDS', True)
    if product_ids is None:
        bb.fatal("Supported Toradex product ids missing, assign TORADEX_PRODUCT_IDS with a list of product ids.")

    data["supported_product_ids"] = d.getVar('TORADEX_PRODUCT_IDS', True).split()

    imagename = d.getVar('IMAGE_NAME', True)
    data["blockdevs"] = [
    OrderedDict({
      "name": "mmcblk0",
      "partitions": [
        {
          "partition_size_nominal": 32,
          "want_maximised": False,
          "content": {
            "label": "BOOT",
            "filesystem_type": "FAT",
            "mkfs_options": "",
            "filelist": [ "boot.scr", "tezi.itb" ],
            "uncompressed_size": 22
          }
        }
      ]
    }),
    OrderedDict({
      "name": "mmcblk0boot0",
      "content": {
        "filesystem_type": "raw",
        "rawfiles": [
          {
            "filename": d.getVar('SPL_BINARY', True),
            "dd_options": "seek=2"
          },
          {
            "filename": "u-boot." + d.getVar('UBOOT_SUFFIX', True),
            "dd_options": "seek=138"
          }
        ]
      }
    })]
    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE', True)
    with open(os.path.join(deploy_dir, 'image.json'), 'w') as outfile:
        json.dump(data, outfile, indent=4)
    bb.note("Toradex Easy Installer metadata file image.json written.")
}
do_rootfs[depends] =+ "virtual/bootloader:do_deploy u-boot-distro-boot:do_deploy"
do_rootfs[postfuncs] =+ "rootfs_tezi_run_json"

IMAGE_CMD_tezirunimg () {
	bbnote "Create Toradex Easy Installer FIT image"
}

do_assemble_fitimage () {
	# We have to execute this step after any image generation so we have
	# squashfs, kernel etc all in DEPLOY_DIR_IMAGE
	# We cannot work in IMGDEPLOYDIR since that folder only has the image
	# files in it (not kernel etc...). We could probably make this a
	# SSTATETASKS similar to how image.class makes do_image_complete...
	mkimage -f ${DEPLOY_DIR_IMAGE}/tezi.its ${DEPLOY_DIR_IMAGE}/tezi.itb

	cd ${DEPLOY_DIR_IMAGE}
	mkdir ${IMAGE_NAME}/
	cp -L -R SPL u-boot.img tezi.itb wrapup.sh image.json tezi.png boot-sdp.scr boot.scr recovery-linux.sh recovery-windows.bat recovery/ ${IMAGE_NAME}/
	zip -r ${IMAGE_NAME}.zip ${IMAGE_NAME}/
	rm -r ${IMAGE_NAME}/
}

addtask do_assemble_fitimage after do_image_complete before do_build

IMAGE_TYPEDEP_tezirunimg += "squashfs"
