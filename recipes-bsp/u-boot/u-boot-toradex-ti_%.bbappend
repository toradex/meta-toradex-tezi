# build additionally a U-Boot used when starting TEZI over USB/RECOVERY

inherit toradex-kernel-config

do_configure:prepend:verdin-am62 () {
    recoveryconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig $recoveryconf
    kconfig_configure_variable BOOTCOMMAND	'"env set recovery_tezi 1; source ${scriptaddr}"'	$recoveryconf
    kconfig_configure_variable ENV_IS_IN_MMC	n	$recoveryconf
    kconfig_configure_variable ENV_IS_NOWHERE	y	$recoveryconf
    kconfig_configure_variable FIT_SIGNATURE_ENFORCE    n       $recoveryconf

    kconfig_configure_variable FIT_SIGNATURE_ENFORCE    n       ${S}/configs/verdin-am62_a53_defconfig
}
