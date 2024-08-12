inherit deploy

TEZI_RUN_DEPLOYDIR = "${DEPLOYDIR}/${BPN}"

do_deploy() {
    install -d ${TEZI_RUN_DEPLOYDIR}/recovery
    install -m 0755 ${UNPACKDIR}/uuu-${PV} ${TEZI_RUN_DEPLOYDIR}/recovery/uuu
    install -m 0755 ${UNPACKDIR}/uuu-${PV}.exe ${TEZI_RUN_DEPLOYDIR}/recovery/uuu.exe
}

addtask deploy before do_build after do_install
