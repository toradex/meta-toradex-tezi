# This provides uEnv.txt files containing default environment.

do_compile_append() {

    if [ -n "${UBOOT_CONFIG}" ]; then
        unset i j k
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    cp ${B}/${config}/common/env_common.o ${TMPDIR}/uboot.env-${config}
                    ${OBJCOPY} -O binary -j ".rodata.default_environment" ${TMPDIR}/uboot.env-${config}
                    tr '\0' '\n' < ${TMPDIR}/uboot.env-${config} | sed -e '/^$/d' > ${B}/${config}/uEnv-${type}.txt
                    rm ${TMPDIR}/uboot.env-${config}
                fi
            done
            unset  j
        done
        unset  i
    else
        cp ${B}/common/env_common.o ${TMPDIR}/uboot.env
        ${OBJCOPY} -O binary -j ".rodata.default_environment" ${TMPDIR}/uboot.env
        tr '\0' '\n' < ${TMPDIR}/uboot.env | sed -e '/^$/d' > ${B}/uEnv.txt
        rm ${TMPDIR}/uboot.env
    fi
}


do_install_append() {
    if [ -n "${UBOOT_CONFIG}" ]; then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    install -d ${D}/boot
                    install -m 644 ${B}/${config}/uEnv-${type}.txt ${D}/boot/uEnv-${type}-${PV}-${PR}.txt
                    ln -sf uEnv-${type}-${PV}-${PR}.txt ${D}/boot/uEnv-${type}.txt
                    ln -sf uEnv-${type}-${PV}-${PR}.txt ${D}/boot/uEnv.txt
                fi
            done
            unset  j
        done
        unset  i
    else
        install -d ${D}/boot
        install -m 644 ${B}/uEnv.txt ${D}/boot/uEnv-${MACHINE}-${PV}-${PR}.txt
        ln -sf uEnv-${MACHINE}-${PV}-${PR}.txt ${D}/boot/uEnv.txt
    fi
}

do_deploy_append() {
    if [ -n "${UBOOT_CONFIG}" ]; then
        for config in ${UBOOT_MACHINE}; do
            i=$(expr $i + 1);
            for type in ${UBOOT_CONFIG}; do
                j=$(expr $j + 1);
                if [ $j -eq $i ]; then
                    install -d ${DEPLOYDIR}
                    install -m 644 ${B}/${config}/uEnv-${type}.txt ${DEPLOYDIR}/uEnv-${type}-${PV}-${PR}.txt
                    ln -sf uEnv-${type}-${PV}-${PR}.txt ${DEPLOYDIR}/uEnv-${type}.txt
                    ln -sf uEnv-${type}-${PV}-${PR}.txt ${DEPLOYDIR}/uEnv.txt
                fi
            done
            unset  j
        done
        unset  i
    else
        install -d ${DEPLOYDIR}
        install -m 644 ${B}/uEnv.txt ${DEPLOYDIR}/uEnv-${MACHINE}-${PV}-${PR}.txt
        ln -sf uEnv-${MACHINE}-${PV}-${PR}.txt ${DEPLOYDIR}/uEnv.txt
    fi
}
