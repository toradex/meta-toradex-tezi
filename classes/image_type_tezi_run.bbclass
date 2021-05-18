UBOOT_BINARY ??= "u-boot.${UBOOT_SUFFIX}"
TEZI_UBOOT_BINARY_EMMC ??= "${UBOOT_BINARY}"
TEZI_UBOOT_BINARY_EMMC_mx8 ??= "${UBOOT_BINARY_TEZI_EMMC}"
TEZI_UBOOT_BINARY_RAWNAND ??= "${UBOOT_BINARY}"
TEZI_UBOOT_BINARY_RECOVERY ??= "${UBOOT_BINARY}"
TORADEX_FLASH_TYPE ??= "emmc"

RM_WORK_EXCLUDE += "${PN}"

DEPENDS += "u-boot-mkimage-native zip-native"

def fitimg_get_size(d):
    import os
    return os.path.getsize(os.path.join(d.getVar('DEPLOY_DIR_IMAGE'), 'tezi.itb')) / (1024 * 1024)

def rootfs_tezi_run_emmc(d):
    from collections import OrderedDict
    uboot = d.getVar('TEZI_UBOOT_BINARY_EMMC')
    offset_bootrom = d.getVar('OFFSET_BOOTROM_PAYLOAD')
    offset_spl = d.getVar('OFFSET_SPL_PAYLOAD')

    bootpart_rawfiles = []
    bootpart_filelist = ["tezi.itb"] + (d.getVar('MACHINE_BOOT_FILES') or "").split()
    if offset_spl:
        bootpart_rawfiles.append(
              {
                "filename": d.getVar('SPL_BINARY'),
                "dd_options": "seek=" + offset_bootrom
              })
    bootpart_rawfiles.append(
              {
                "filename": uboot,
                "dd_options": "seek=" + (offset_spl if offset_spl else offset_bootrom)
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
          "erase": True,
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
            "name": "u-boot-env",
            "erase": True,
            "content": {}
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
    from datetime import datetime

    deploydir = d.getVar('DEPLOY_DIR_IMAGE')

    data = OrderedDict({ "config_format": 4, "autoinstall": False })

    # Use image recipes SUMMARY/DESCRIPTION/PV...
    data["name"] = d.getVar('SUMMARY')
    data["description"] = d.getVar('DESCRIPTION')
    data["version"] = d.getVar('DISTRO_VERSION')
    data["release_date"] = datetime.strptime(d.getVar('DATE', False), '%Y%m%d').date().isoformat()
    if os.path.exists(os.path.join(deploydir, "tezi-run-metadata", "wrapup.sh")):
        data["wrapup_script"] = "wrapup.sh"
    if os.path.exists(os.path.join(deploydir, "tezi-run-metadata", "tezi.png")):
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
        uboot_file = d.getVar('TEZI_UBOOT_BINARY_RAWNAND')
    elif flash_type == "emmc":
        data["blockdevs"] = rootfs_tezi_run_emmc(d)
        uboot_file = d.getVar('TEZI_UBOOT_BINARY_EMMC')
        # TODO: Multi image/raw NAND with SPL currently not supported
        if d.getVar('OFFSET_SPL_PAYLOAD'):
            uboot_file += " " + d.getVar('SPL_BINARY')
    else:
        bb.fatal("Unsupported Toradex flash type")

    imagefile = 'image.json'
    if type_specific_name:
        imagefile = 'image-{0}.json'.format(flash_type)

    with open(os.path.join(d.getVar('IMGDEPLOYDIR'), imagefile), 'w') as outfile:
         json.dump(data, outfile, indent=4)
    d.appendVar('TEZI_IMAGE_FILES', ' ' + imagefile)
    d.appendVar("TEZI_IMAGE_UBOOT_FILES", ' ' + uboot_file)
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
    (cd ${DEPLOY_DIR_IMAGE}; cp -L -R ${TEZI_IMAGE_UBOOT_FILES} ${TEZI_UBOOT_BINARY_RECOVERY} ${MACHINE_BOOT_FILES} tezi.itb tezi-run-metadata/* ${WORKDIR}/${TDX_VER_ID})
    (cd ${IMGDEPLOYDIR}; cp -L -R ${TEZI_IMAGE_FILES} ${WORKDIR}/${TDX_VER_ID})
    zip -r ${TDX_VER_ID}.zip ${TDX_VER_ID}
    mv ${TDX_VER_ID}.zip ${IMGDEPLOYDIR}
}

do_image_tezirunimg[dirs] += "${WORKDIR}/${TDX_VER_ID} ${WORKDIR}"
do_image_tezirunimg[cleandirs] += "${WORKDIR}/${TDX_VER_ID}"
do_image_tezirunimg[prefuncs] += "rootfs_tezirun_run_json"
do_image_tezirunimg[depends] += "virtual/bootloader:do_deploy u-boot-distro-boot:do_deploy virtual/kernel:do_deploy tezi-run-metadata:do_deploy \
                                 ${@'%s:do_deploy' % d.getVar('IMAGE_BOOTLOADER') if d.getVar('IMAGE_BOOTLOADER') else ''} \
                                "
do_image_teziimg[vardepsexclude] = "DISTRO_VERSION"
