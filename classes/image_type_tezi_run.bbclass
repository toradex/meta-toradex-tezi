inherit image_types

IMAGE_DEPENDS_tezirunimg = "tezi-run-metadata:do_deploy u-boot-mkimage-native p7zip-native zip-native"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"

def fitimg_get_size(d):
    import subprocess
    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)

    # Get size of FIT image...
    args = ['du', '-kLc', os.path.join(deploydir, 'tezi.itb') ]
    output = subprocess.check_output(args)
    return int(output.splitlines()[-1].split()[0])

def rootfs_tezi_run_emmc(d):
    from collections import OrderedDict

    return [
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
                "uncompressed_size": fitimg_get_size(d) / 1024
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
                "filename": d.getVar('UBOOT_BINARY', True),
                "dd_options": "seek=138"
              }
            ]
          }
        })]

def rootfs_tezi_run_rawnand(d):
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
              "name": "tezi",
              "type": "static",
              "content": {
                "rawfile": {
                  "filename": "tezi.itb",
                  "size": fitimg_get_size(d) / 1024
                }
              }
            }
          ]
        })]

python rootfs_tezi_run_json() {
    if not bb.utils.contains("IMAGE_FSTYPES", "tezirunimg", True, False, d):
        return

    import json
    from collections import OrderedDict

    deploydir = d.getVar('DEPLOY_DIR_IMAGE', True)

    data = OrderedDict({ "config_format": 1, "autoinstall": False })

    # Use image recipes SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY', True)
    data["description"] = d.getVar('DESCRIPTION', True)
    data["version"] = d.getVar('TDX_VER_EXT_MIN', True)
    data["release_date"] = d.getVar('TDX_VERDATE', True)[1:9]
    if os.path.exists(os.path.join(deploydir, "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "tezi.png")):
        data["icon"] = "tezi.png"

    product_ids = d.getVar('TORADEX_PRODUCT_IDS', True)
    if product_ids is None:
        bb.fatal("Supported Toradex product ids missing, assign TORADEX_PRODUCT_IDS with a list of product ids.")

    data["supported_product_ids"] = d.getVar('TORADEX_PRODUCT_IDS', True).split()

    if bb.utils.contains("TORADEX_FLASH_TYPE", "rawnand", True, False, d):
        data["mtddevs"] = rootfs_tezi_run_rawnand(d)
    else:
        data["blockdevs"] = rootfs_tezi_run_emmc(d)

    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE', True)
    with open(os.path.join(deploy_dir, 'image.json'), 'w') as outfile:
        json.dump(data, outfile, indent=4)
    bb.note("Toradex Easy Installer metadata file image.json written.")
}
do_rootfs[depends] =+ "virtual/bootloader:do_deploy u-boot-distro-boot:do_deploy"

IMAGE_CMD_tezirunimg () {
	bbnote "Create Toradex Easy Installer FIT image"
}

build_fitimage () {
	# We have to execute this step after any image generation so we have
	# squashfs, kernel etc all in DEPLOY_DIR_IMAGE
	# We cannot work in IMGDEPLOYDIR since that folder only has the image
	# files in it (not kernel etc...). We could probably make this a
	# SSTATETASKS similar to how image.class makes do_image_complete...
	mkimage -f ${DEPLOY_DIR_IMAGE}/tezi.its ${DEPLOY_DIR_IMAGE}/tezi.itb
}

build_deploytar () {
	cd ${DEPLOY_DIR_IMAGE}
	mkdir ${IMAGE_NAME}/
	cp -L -R ${SPL_BINARY} ${UBOOT_BINARY} tezi.itb wrapup.sh image.json tezi.png boot-sdp.scr boot.scr recovery-linux.sh recovery-windows.bat recovery/ ${IMAGE_NAME}/
	zip -r ${IMAGE_NAME}.zip ${IMAGE_NAME}/
	rm -r ${IMAGE_NAME}/
}

python do_assemble_fitimage() {
    if not bb.utils.contains("IMAGE_FSTYPES", "tezirunimg", True, False, d):
        return

    bb.build.exec_func('build_fitimage', d)
    bb.build.exec_func('rootfs_tezi_run_json', d)
    bb.build.exec_func('build_deploytar', d)
}

addtask do_assemble_fitimage after do_image_complete before do_build
do_assemble_fitimage[depends] = "tezi-run-metadata:do_deploy"

IMAGE_TYPEDEP_tezirunimg += "squashfs"
