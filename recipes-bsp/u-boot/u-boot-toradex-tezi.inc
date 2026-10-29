FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
    file://toradex-recovery.config \
    file://toradex-nand-bootcmd.config \
    file://toradex-recovery-u-boot.dtsi \
"

do_configure:prepend () {
    cp ${UNPACKDIR}/toradex-recovery.config ${S}/configs/
    cp ${UNPACKDIR}/toradex-nand-bootcmd.config ${S}/configs/

    cp ${UNPACKDIR}/toradex-recovery-u-boot.dtsi ${S}/arch/arm/dts/
}

# For the NAND/eMMC modules qt-tezi expects fw_env.config for use on
# a eMMC based module and fw_env_mtd4.config for a NAND based one.
# The eMMC fw_env.config is provided in files/<machine>/fw_env.config,
# overriding the raw NAND one from meta-toradex-bsp.
SRC_URI:append:colibri-imx6ull = " file://fw_env_mtd4.config"
SRC_URI:append:colibri-imx7 = " file://fw_env_mtd4.config"

install_mtd4_cfg () {
    install -d ${D}${sysconfdir}
    install -m 0644 ${UNPACKDIR}/fw_env_mtd4.config ${D}${sysconfdir}/
}

do_install:append:colibri-imx6ull () {
    install_mtd4_cfg
}

do_install:append:colibri-imx7 () {
    install_mtd4_cfg
}

FILES:${PN}-env:append:colibri-imx6ull = " ${sysconfdir}/fw_env_mtd4.config"
FILES:${PN}-env:append:colibri-imx7 = " ${sysconfdir}/fw_env_mtd4.config"

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
