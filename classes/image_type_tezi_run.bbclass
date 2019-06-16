TEZI_DISTRO_BOOT_SCRIPTS ??= "${@'boot-sdp.scr' if d.getVar('UBOOT_SDP_SUPPORT') == '1' else ''} boot.scr"
TEZI_DISTRO_BOOT_SCRIPTS_remove_apalis-imx8 = "boot-sdp.scr"
UBOOT_BINARY ??= "u-boot.${UBOOT_SUFFIX}"
TEZI_UBOOT_BINARY_EMMC ??= "${UBOOT_BINARY}"
TEZI_UBOOT_BINARY_EMMC_apalis-imx8 ??= "flash.bin"
TEZI_UBOOT_BINARY_RAWNAND ??= "${UBOOT_BINARY}"
TEZI_UBOOT_BINARY_RECOVERY ??= "${UBOOT_BINARY}"
TEZI_UBOOT_BINARY_RECOVERY_apalis-imx8 ??= "recovery.bin"
TEZI_UBOOT_BINARIES ??= "${@' '.join(x for x in sorted(set([bb.utils.contains('TORADEX_FLASH_TYPE', 'emmc', d.getVar('TEZI_UBOOT_BINARY_EMMC'), '', d), \
                         bb.utils.contains('TORADEX_FLASH_TYPE', 'rawnand', d.getVar('TEZI_UBOOT_BINARY_RAWNAND'), '', d), \
                         d.getVar('TEZI_UBOOT_BINARY_RECOVERY')])))}"
TORADEX_FLASH_TYPE ??= "emmc"
TEZI_RUNIMG_DEPENDS ??= "virtual/bootloader:do_deploy u-boot-distro-boot:do_deploy virtual/kernel:do_deploy \
                         tezi-run-metadata:do_deploy u-boot-mkimage-native:do_populate_sysroot zip-native:do_populate_sysroot \
                         ${@'%s:do_deploy' % d.getVar('IMAGE_BOOTLOADER') if d.getVar('IMAGE_BOOTLOADER') else ''} \
                        "

def fitimg_get_size(d):
    import os
    return os.path.getsize(os.path.join(d.getVar('DEPLOY_DIR_IMAGE'), 'tezi.itb')) / (1024 * 1024)

def rootfs_tezi_run_emmc(d):
    from collections import OrderedDict
    uboot = d.getVar('TEZI_UBOOT_BINARY_EMMC')
    offset_bootrom = d.getVar('OFFSET_BOOTROM_PAYLOAD')
    offset_spl = d.getVar('OFFSET_SPL_PAYLOAD')

    bootpart_rawfiles = []
    bootpart_filelist = [ "boot.scr", "tezi.itb" ] + d.getVar('MACHINE_BOOT_FILES').split()
    has_spl = d.getVar('SPL_BINARY')
    if has_spl:
        bootpart_rawfiles.append(
              {
                "filename": has_spl,
                "dd_options": "seek=" + offset_bootrom
              })
    bootpart_rawfiles.append(
              {
                "filename": uboot,
                "dd_options": "seek=" + (offset_spl if has_spl else offset_bootrom)
              })

    return [
        OrderedDict({
          "name": "mmcblk0",
          "partitions": [
            {
              "partition_size_nominal": 128,
              "want_maximised": False,
              "content": {
                "label": "BOOT",
                "filesystem_type": "FAT",
                "mkfs_options": "",
                "filelist": bootpart_filelist,
                "uncompressed_size": fitimg_get_size(d)
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

def rootfs_tezi_run_rawnand(d):
    from collections import OrderedDict
    uboot = d.getVar('TEZI_UBOOT_BINARY_RAWNAND')

    return [
        OrderedDict({
          "name": "u-boot1",
          "content": {
            "rawfile": {
              "filename": uboot,
              "size": 1
            }
          },
        }),
        OrderedDict({
          "name": "u-boot2",
          "content": {
            "rawfile": {
              "filename": uboot,
              "size": 1
            }
          }
        }),
        OrderedDict({
          "name": "ubi",
          "ubivolumes": [
            {
              "name": "rootfs",
              "type": "static",
              "content": {
                "rawfile": {
                  "filename": "tezi.itb",
                  "size": fitimg_get_size(d)
                }
              }
            }
          ]
        })]

def rootfs_tezi_run_create_json(d, flash_type, type_specific_name = False):
    import json
    from collections import OrderedDict

    deploydir = d.getVar('DEPLOY_DIR_IMAGE')

    data = OrderedDict({ "config_format": 1, "autoinstall": False })

    # Use image recipes SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY')
    data["description"] = d.getVar('DESCRIPTION')
    data["version"] = d.getVar('TDX_VER_PACKAGE_MIN')
    data["release_date"] = d.getVar('TDX_VERDATE')[1:9]
    if os.path.exists(os.path.join(deploydir, "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "tezi.png")):
        data["icon"] = "tezi.png"
    data["isinstaller"] = True

    product_ids = d.getVar('TORADEX_PRODUCT_IDS')
    if product_ids is None:
        bb.fatal("Supported Toradex product ids missing, assign TORADEX_PRODUCT_IDS with a list of product ids.")

    dtmapping = d.getVarFlags('TORADEX_PRODUCT_IDS')
    data["supported_product_ids"] = []

    # If no varflags are set, we assume all product ids supported with single image/U-Boot
    if dtmapping is not None:
        for f, v in dtmapping.items():
            dtbflashtypearr = v.split(',')
            if len(dtbflashtypearr) < 2 or dtbflashtypearr[1] == flash_type:
                data["supported_product_ids"].append(f)
    else:
        data["supported_product_ids"].extend(product_ids.split())

    if flash_type == "rawnand":
        data["mtddevs"] = rootfs_tezi_run_rawnand(d)
    elif flash_type == "emmc":
        data["blockdevs"] = rootfs_tezi_run_emmc(d)
    else:
        bb.fatal("Unsupported Toradex flash type")

    imagefile = 'image.json'
    if type_specific_name:
        imagefile = 'image-{0}.json'.format(flash_type)

    with open(os.path.join(deploydir, imagefile), 'w') as outfile:
         json.dump(data, outfile, indent=4)
    d.appendVar('TEZI_IMAGE_FILES', imagefile + ' ')
    bb.note("Toradex Easy Installer metadata file {0} written.".format(imagefile))

python rootfs_tezirun_run_json() {
    flash_types = d.getVar('TORADEX_FLASH_TYPE')
    if flash_types is None:
        bb.fatal("Toradex flash type not specified")

    flash_types_list = flash_types.split()
    for flash_type in flash_types_list:
        rootfs_tezi_run_create_json(d, flash_type, len(flash_types_list) > 1)
}

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

	# mkdir fails if existing
	if [ -e ${TDX_VER_ID} ]; then
		rm -r ${TDX_VER_ID}
	fi

	mkdir ${TDX_VER_ID}
	cp -L -R ${SPL_BINARY} ${TEZI_UBOOT_BINARIES} ${TEZI_IMAGE_FILES} ${TEZI_DISTRO_BOOT_SCRIPTS} tezi.itb tezi-run-metadata/* ${TDX_VER_ID}

	if [ -n ${MACHINE_BOOT_FILES} ]; then
	    cp ${MACHINE_BOOT_FILES} ${TDX_VER_ID}
	fi

	# zip does update if the file exist, explicitly delete before adding files to the archive
	if [ -e ${TDX_VER_ID}.zip ]; then
		rm ${TDX_VER_ID}.zip
	fi

	zip -r ${TDX_VER_ID}.zip ${TDX_VER_ID}
	rm -r ${TDX_VER_ID}
}

python do_assemble_fitimage() {
    bb.build.exec_func('build_fitimage', d)
    bb.build.exec_func('rootfs_tezirun_run_json', d)
    bb.build.exec_func('build_deploytar', d)
}

addtask do_assemble_fitimage after do_image_complete before do_build

do_assemble_fitimage[depends] = "${@bb.utils.contains('IMAGE_FSTYPES', 'tezirunimg', '${TEZI_RUNIMG_DEPENDS}', '', d)}"

IMAGE_TYPEDEP_tezirunimg += "squashfs"
