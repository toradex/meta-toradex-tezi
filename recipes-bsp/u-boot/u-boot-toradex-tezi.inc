FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://toradex-recovery-u-boot.dtsi"

inherit toradex-kernel-config

configure_bootcmd_nand () {
    recoveryconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig
    kconfig_configure_variable BOOTCOMMAND \""ubi part ubi ; ubi read \${scriptaddr} bootscr; source \${scriptaddr} ;"\" $recoveryconf
}

do_configure:prepend:colibri-imx6ull () {
    configure_bootcmd_nand
}

do_configure:prepend:colibri-imx7 () {
    configure_bootcmd_nand
}

do_configure:prepend:verdin-am62 () {
    kconfig_configure_variable BOOTCOMMAND '"fastboot usb 0"' $recoveryconf
}

do_configure:prepend () {
    recoveryconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    defaultconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig
    cp $defaultconf $recoveryconf
    
    kconfig_configure_variable CMD_USB_SDP n $recoveryconf
    kconfig_configure_variable DEVICE_TREE_INCLUDES \"toradex-recovery-u-boot.dtsi\" $recoveryconf
    kconfig_configure_variable ENV_IMPORT_FDT y $recoveryconf
    kconfig_configure_variable ENV_IS_IN_MMC n $recoveryconf
    kconfig_configure_variable ENV_IS_IN_NAND n $recoveryconf
    kconfig_configure_variable ENV_IS_NOWHERE y $recoveryconf
    kconfig_configure_variable TDX_CFG_BLOCK_USB_GADGET_PID n $recoveryconf
}

do_compile:prepend () {
    cp ${WORKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
}

do_deploy:append() {
    # If using binman deploy the resulted flash.bin for WIC to pick it up
    if [ -n "${UBOOT_CONFIG}" -a "x${UBOOT_PROVIDES_BOOT_CONTAINER}" = "x1" ]; then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]
                then
                    if [ -f "flash.bin-${MACHINE}-${type}" ]; then
                        ln -sf flash.bin-${MACHINE}-${type} imx-boot-${type}
                    fi
                fi
            done
            unset  j
        done
        unset  i
    fi
}
