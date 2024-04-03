EMMCDEV = "mmcblk0"
EMMCDEV:verdin-imx8mp = "emmc"
EMMCDEVBOOT0 = "mmcblk0boot0"
EMMCDEVBOOT0:verdin-imx8mp = "emmc-boot0"
UBOOT_BINARY ??= "u-boot.${UBOOT_SUFFIX}"
TEZI_CONFIG_FORMAT ?= "4"
TEZI_AUTO_INSTALL ??= "false"
TEZI_UBOOT_BINARY_EMMC ??= "${UBOOT_BINARY}-emmc"
TEZI_UBOOT_BINARY_EMMC:mx8-generic-bsp ??= "${UBOOT_BINARY_TEZI_EMMC}-sd"
TEZI_UBOOT_BINARY_EMMC:apalis-imx6 ??= "${UBOOT_BINARY}-sd"
TEZI_UBOOT_BINARY_EMMC:colibri-imx6 ??= "${UBOOT_BINARY}-sd"
TEZI_UBOOT_BINARY_EMMC:am62xx ??= "${UBOOT_BINARY}-sd"
TEZI_UBOOT_BINARY_RAWNAND ??= "${UBOOT_BINARY}-rawnand"
TEZI_UBOOT_BINARY_RECOVERY ??= "${UBOOT_BINARY}-recoverytezi"
TEZI_UBOOT_BINARY_RECOVERY:mx8-generic-bsp ??= "${UBOOT_BINARY_TEZI_EMMC}-recoverytezi"
TEZI_UBOOT_BINARY_RECOVERY:am62xx ??= "tiboot3-am62x-gp-verdin.bin-dfu tiboot3-am62x-hs-fs-verdin.bin-dfu u-boot.img-recoverytezi"
TORADEX_FLASH_TYPE ??= "emmc"
CREATE_LEGACY_JSON = "false"
CREATE_LEGACY_JSON:apalis-imx6 = "true"
CREATE_LEGACY_JSON:colibri-imx6 = "true"
CREATE_LEGACY_JSON:colibri-imx6ull = "true"
CREATE_LEGACY_JSON:colibri-imx7 = "true"

RM_WORK_EXCLUDE += "${PN}"

DEPENDS += "u-boot-mkimage-native zip-native"

def fitimg_get_size(d):
    import os
    return os.path.getsize(os.path.join(d.getVar('DEPLOY_DIR_IMAGE'), 'tezi.itb')) / (1024 * 1024)

def rootfs_tezi_run_emmc(d):
    from collections import OrderedDict
    emmcdev = d.getVar('EMMCDEV')
    emmcdevboot0 = d.getVar('EMMCDEVBOOT0')
    offset_bootrom = d.getVar('OFFSET_BOOTROM_PAYLOAD')
    offset_fw = d.getVar('OFFSET_FW_PAYLOAD')
    offset_spl = d.getVar('OFFSET_SPL_PAYLOAD')
    uboot = d.getVar('TEZI_UBOOT_BINARY_EMMC')

    bootpart_rawfiles = []
    bootpart_filelist = ["tezi.itb"] + (d.getVar('MACHINE_BOOT_FILES') or "").split()

    offset_payload = offset_bootrom
    if offset_fw:
        # FIRMWARE_BINARY contain product_id <-> filename mapping
        fwmapping = d.getVarFlags('FIRMWARE_BINARY')
        for f, v in fwmapping.items():
            bootpart_rawfiles.append(
              {
                "filename": v,
                "dd_options": "seek=" + offset_payload,
                "product_ids": f
              })
        offset_payload = offset_fw
    if offset_spl:
        bootpart_rawfiles.append(
              {
                "filename": d.getVar('SPL_BINARY'),
                "dd_options": "seek=" + offset_payload
              })
        offset_payload = offset_spl
    bootpart_rawfiles.append(
              {
                "filename": uboot,
                "dd_options": "seek=" + offset_payload
              })

    return [
        OrderedDict({
          "name": emmcdev,
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
          "name": emmcdevboot0,
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
                "name": "bootscr",
                "type": "static",
                "content": {
                    "rawfile": {
                        "filename": "boot.scr"
                    }
                },
                "size_kib": 128
            },
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

def fw_binaries(d):
    fwmapping = d.getVarFlags('FIRMWARE_BINARY')

    if fwmapping is not None:
        fw_bins = []
        for key, val in fwmapping.items():
            if val not in fw_bins:
                fw_bins.append(val)
        return " " + " ".join(fw_bins)
    else:
        return ""

def rootfs_tezi_run_create_json(d, flash_type, type_specific_name = False):
    import json
    from collections import OrderedDict
    from datetime import datetime

    deploydir = d.getVar('DEPLOY_DIR_IMAGE')

    data = OrderedDict({ "config_format": d.getVar('TEZI_CONFIG_FORMAT'), "autoinstall": oe.types.boolean(d.getVar('TEZI_AUTO_INSTALL')) })

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

    data["supported_product_ids"] = []
    pids_splitted = product_ids.split(' ')
    for id_num in pids_splitted:
        dtbflashtype = d.getVarFlag('TORADEX_PRODUCT_IDS', id_num)
        if dtbflashtype is None:
            data["supported_product_ids"].append(id_num)
        else:
          dtbflashtype_arr = dtbflashtype.split(',')
          if (len(dtbflashtype_arr) == 1):
              data["supported_product_ids"].append(id_num)
          elif (dtbflashtype_arr[1] == flash_type):
              data["supported_product_ids"].append(id_num)

    if flash_type == "rawnand":
        data["mtddevs"] = rootfs_tezi_run_rawnand(d)
        uboot_file = d.getVar('TEZI_UBOOT_BINARY_RAWNAND')
    elif flash_type == "emmc":
        data["blockdevs"] = rootfs_tezi_run_emmc(d)
        uboot_file = d.getVar('TEZI_UBOOT_BINARY_EMMC')
        # TODO: Multi image/raw NAND with SPL currently not supported
        uboot_file += fw_binaries(d);
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

IMAGE_CMD:tezirunimg () {
    # create and deploy an additional json file which allows installing a
    # newer Toradex Easy Installer (5.3.0 and later) from a running
    # older (2.0b3 and older) one.
    if [ "${CREATE_LEGACY_JSON}" = "true" ]; then
        rm -f ${IMGDEPLOYDIR}/*-legacy.json
        LEGACY_IMAGE_FILES=""
        for JSON in ${IMGDEPLOYDIR}/image*.json; do
            LEGACY=$(basename -s .json $JSON)-legacy.json
            LEGACY_IMAGE_FILES="$LEGACY_IMAGE_FILES $LEGACY"
            #-    "config_format": "4",
            #+    "config_format": "1",
            sed 's/\(\"config_format\"[^0-9]*\).*/\11",/' $JSON > ${IMGDEPLOYDIR}/$LEGACY
            #-    "name": "Toradex Easy Installer",
            #+    "name": "Toradex Easy Installer (Package for Installing this version with Easy Installer 1.8)",
            sed -i 's/\(\"name\".*Toradex.*\)\"/\1 (Package for Installing this version with Easy Installer 1.8)"/' ${IMGDEPLOYDIR}/$LEGACY
            #-    "description": "Toradex Easy Installer for colibri-imx7 machine",
            #+    "description": "Legacy Toradex Easy Installer Package for 1.8 for colibri-imx7 machine",
            sed -i 's/\(\"description\".*\)Toradex.*for/\1Legacy Toradex Easy Installer Package for 1.8 for/' ${IMGDEPLOYDIR}/$LEGACY
            #-    "version": "5.3.0+build.3",
            #+    "version": "5.30",
            sed -i 's/\(\"version\".*\"[0-9]*\.[0-9]*\)\.\([0-9]*\)\+.*/\1\2",/' ${IMGDEPLOYDIR}/$LEGACY
        done
    fi

    (cd ${DEPLOY_DIR_IMAGE}; cp -L -R ${TEZI_IMAGE_UBOOT_FILES} ${TEZI_UBOOT_BINARY_RECOVERY} ${MACHINE_BOOT_FILES} tezi.itb tezi-run-metadata/* ${WORKDIR}/${TDX_VER_ID})
    (cd ${IMGDEPLOYDIR}; cp -L -R ${TEZI_IMAGE_FILES} $LEGACY_IMAGE_FILES ${WORKDIR}/${TDX_VER_ID})
    zip -r ${TDX_VER_ID}.zip ${TDX_VER_ID}
    mv ${TDX_VER_ID}.zip ${IMGDEPLOYDIR}
}

do_image_tezirunimg[dirs] += "${WORKDIR}/${TDX_VER_ID} ${WORKDIR}"
do_image_tezirunimg[cleandirs] += "${WORKDIR}/${TDX_VER_ID}"
do_image_tezirunimg[prefuncs] += "rootfs_tezirun_run_json"
do_image_tezirunimg[depends] += "virtual/bootloader:do_deploy u-boot-distro-boot:do_deploy virtual/kernel:do_deploy tezi-run-metadata:do_deploy"
do_image_teziimg[vardepsexclude] = "DISTRO_VERSION TDX_VER_ID DATE"
