#backport from master, util-linux: Add fstrim utility, commit ee3599472d7c5ccbf259312c7622b982941b6b1e

PRINC := "${@int(PRINC) + 2}"

PACKAGES =+ "util-linux-fstrim"
FILES_util-linux-fstrim = "${base_sbindir}/fstrim"

do_install_append () {
	if [ -f "${D}${sbindir}/fstrim" ]; then
		mv "${D}${sbindir}/fstrim" "${D}${base_sbindir}/fstrim"
	fi
}
