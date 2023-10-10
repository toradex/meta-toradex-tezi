inherit deploy

TEZI_RUN_DEPLOYDIR = "${DEPLOYDIR}/${BPN}"

do_deploy() {
    install -d ${TEZI_RUN_DEPLOYDIR}/recovery
    install -m 0755 ${WORKDIR}/uuu-${PV} ${TEZI_RUN_DEPLOYDIR}/recovery/uuu
    install -m 0755 ${WORKDIR}/uuu-${PV}.exe ${TEZI_RUN_DEPLOYDIR}/recovery/uuu.exe
}

# Overwrite do_install() from meta-freescale/**/*/uuu-bin recipe to prevent
# this recipe copying the uuu  binaries to the filesystem of Tezi.
do_install() {
    return
}

addtask deploy before do_build after do_install
