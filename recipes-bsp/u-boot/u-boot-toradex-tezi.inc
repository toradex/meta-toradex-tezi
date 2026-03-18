FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://toradex-recovery-u-boot.dtsi \
    file://toradex-recovery-base.config \
    file://toradex-recovery-fastboot.config \
    file://toradex-recovery-nand-bootcmd.config \
"

do_configure:prepend () {
    recoveryconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    defaultconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig
    cp $defaultconf $recoveryconf

    cp ${UNPACKDIR}/toradex-recovery-base.config ${S}/configs/
    cp ${UNPACKDIR}/toradex-recovery-fastboot.config ${S}/configs/
    cp ${UNPACKDIR}/toradex-recovery-nand-bootcmd.config ${S}/configs/
}

do_compile:prepend () {
    cp ${UNPACKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
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
