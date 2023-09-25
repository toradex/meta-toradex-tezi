# build additionally a U-Boot used when starting TEZI over USB/RECOVERY

# Assign/change a config variable
# $1 - config variable to be set
# $2 - value [n/y/value]
# $3 - config file
#
configure_variable() {
	# Remove the original config, to avoid reassigning it.
	sed -i -e "/CONFIG_$1[ =]/d" $3

	# Assign the config value
	if [ "$2" = "n" ]; then
		echo "# CONFIG_$1 is not set" >> $3
	else
		echo "CONFIG_$1=$2" >> $3
	fi
}

do_configure:prepend:verdin-am62 () {
    recoveryconf=${S}/configs/${UBOOT_CONFIG_BASENAME}_tezi_defconfig
    cp ${S}/configs/${UBOOT_CONFIG_BASENAME}_defconfig $recoveryconf
    configure_variable BOOTCOMMAND	'"env set recovery_tezi 1; source ${scriptaddr}"'	$recoveryconf
    configure_variable ENV_IS_IN_MMC	n	$recoveryconf
    configure_variable ENV_IS_NOWHERE	y	$recoveryconf
    configure_variable FIT_SIGNATURE_ENFORCE    n       $recoveryconf

    configure_variable FIT_SIGNATURE_ENFORCE    n       ${S}/configs/verdin-am62_a53_defconfig
}
