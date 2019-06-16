# IMX boot common variables

IMX_EXTRA_FIRMWARE      = "firmware-imx-8 imx-sc-firmware"
IMX_EXTRA_FIRMWARE_mx8m = "firmware-imx-8m"
IMX_EXTRA_FIRMWARE_mx8x = "firmware-imx-8x imx-sc-firmware"

BOOT_NAME = "imx-boot"

SC_FIRMWARE_NAME ?= "scfw_tcm.bin"
SECO_FIRMWARE_NAME ?= "mx8qm-ahab-container.img"

ATF_MACHINE_NAME ?= "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qm = "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qxp = "bl31-imx8qxp.bin"
ATF_MACHINE_NAME_mx8mq = "bl31-imx8mq.bin"
ATF_MACHINE_NAME_mx8mm = "bl31-imx8mm.bin"
ATF_MACHINE_NAME_append = "${@bb.utils.contains('COMBINED_FEATURES', 'optee', '-optee', '', d)}"

DCD_NAME ?= "imx8qm_dcd.cfg.tmp"
DCD_NAME_mx8qm = "imx8qm_dcd.cfg.tmp"
DCD_NAME_mx8qxp = "imx8qx_dcd.cfg.tmp"

UBOOT_NAME = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
BOOT_CONFIG_MACHINE = "${BOOT_NAME}-${MACHINE}-${UBOOT_CONFIG}.bin"

BOOT_TOOLS ?= "imx-boot-tools"
TOOLS_NAME ?= "mkimage_imx8"

SOC_TARGET       ?= "INVALID"
SOC_TARGET_mx8qm  = "iMX8QM"
SOC_TARGET_mx8qxp = "iMX8QX"
SOC_TARGET_mx8mq  = "iMX8M"
SOC_TARGET_mx8mm  = "iMX8MM"

IMXBOOT_TARGETS ?= \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi', \
        bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand', \
                                                  'flash flash_dcd', d), d)}"
IMXBOOT_TARGETS_mx8qxp = \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi', \
        bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand', \
                                                  'flash', d), d)}"
IMXBOOT_TARGETS_mx8qxpa0 = \
    "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi_a0', \
        bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand_a0', \
                                                  'flash_a0 flash_dcd_a0', d), d)}"

BOOT_STAGING       = "${S}/${SOC_TARGET}"
BOOT_STAGING_mx8mm = "${S}/iMX8M"

SOC_FAMILY      = "INVALID"
SOC_FAMILY_mx8  = "mx8"
SOC_FAMILY_mx8m = "mx8m"
SOC_FAMILY_mx8x = "mx8x"
