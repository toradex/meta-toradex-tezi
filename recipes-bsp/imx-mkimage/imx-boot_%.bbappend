deploy_mx8_append () {
    ${DEPLOYDIR}/imx-boot-tools/${TOOLS_NAME} -commit > head.hash
    cat ${BOOT_STAGING}/u-boot.bin head.hash > u-boot-hash.bin
    cp ${BOOT_STAGING}/bl31.bin u-boot-atf.bin
    dd if=u-boot-hash.bin of=u-boot-atf.bin bs=1K seek=128

    ${DEPLOYDIR}/imx-boot-tools/${TOOLS_NAME} -soc QM -rev B0 -append ${BOOT_STAGING}/mx8qm-ahab-container.img \
        -c -scfw ${BOOT_STAGING}/scfw_tcm.bin -ap u-boot-atf.bin a53 0x80000000 -out ${DEPLOYDIR}/flash.bin
}

deploy_mx8x_append () {
    ${DEPLOYDIR}/imx-boot-tools/${TOOLS_NAME} -commit > head.hash
    cat ${BOOT_STAGING}/u-boot.bin head.hash > u-boot-hash.bin
    cp ${BOOT_STAGING}/bl31.bin u-boot-atf.bin
    dd if=u-boot-hash.bin of=u-boot-atf.bin bs=1K seek=128

    ${DEPLOYDIR}/imx-boot-tools/${TOOLS_NAME} -soc QX -rev B0 -append ${BOOT_STAGING}/mx8qx-ahab-container.img \
        -c -scfw ${BOOT_STAGING}/scfw_tcm.bin -ap u-boot-atf.bin a53 0x80000000 -out ${DEPLOYDIR}/flash.bin
}
