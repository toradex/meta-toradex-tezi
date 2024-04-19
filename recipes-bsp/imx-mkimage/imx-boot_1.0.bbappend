do_deploy:append() {
    #This is here to make more simple to get deployed imx-boot files
    #Assumptions: recovery is the first U-boot config in UBOOT_CONFIG
    #and imx-mkimage target is first target
    #These are the same assumptions made by imx-boot.bb
    i=0
    for config in ${UBOOT_CONFIG}; do
        if [ "$config" = "recoverytezi" ] && [ "$i" -eq 0 ]; then
            IMX_BOOT_BINARY="imx-boot"
        else
            IMX_BOOT_BINARY="imx-boot-${MACHINE}-$config.bin-${IMAGE_IMXBOOT_TARGET}"
        fi
        cp "${DEPLOYDIR}/${IMX_BOOT_BINARY}" "${DEPLOYDIR}/imx-boot-$config"
        i=$(expr $i + 1);
    done
}
