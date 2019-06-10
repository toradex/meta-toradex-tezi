SUMMARY = "Tezi installer version"
DESCRIPTION = "The /etc/tezi-version file contains Tezi installer version data."
LICENSE = "MIT"

inherit allarch

INHIBIT_DEFAULT_DEPS = "1"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"

TEZI_VERSION_FIELDS = "TDX_VER_ID"

python do_compile () {
    with open(d.expand('${B}/tezi-version'), 'w') as f:
        for field in d.getVar('TEZI_VERSION_FIELDS').split():
            value = d.getVar(field)
            if value:
                f.write('{0}="{1}"\n'.format(field, value))
}
do_compile[vardeps] += "${TEZI_VERSION_FIELDS}"

do_install () {
    install -d ${D}${nonarch_libdir} ${D}${sysconfdir}
    install -m 0644 tezi-version ${D}${nonarch_libdir}/
    lnr ${D}${nonarch_libdir}/tezi-version ${D}${sysconfdir}/tezi-version
}

FILES_${PN} += "${nonarch_libdir}/tezi-version"
