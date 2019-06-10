deploy_append_mx8() {
    ${BOOT_STAGING}/${TOOLS_NAME} -commit > head.hash
    cat ${BOOT_STAGING}/u-boot.bin head.hash > u-boot-hash.bin
    cp ${BOOT_STAGING}/bl31.bin u-boot-atf.bin
    dd if=u-boot-hash.bin of=u-boot-atf.bin bs=1K seek=128

    ${BOOT_STAGING}/${TOOLS_NAME} -soc QM -rev B0 -append ${BOOT_STAGING}/mx8qm-ahab-container.img \
        -c -scfw ${BOOT_STAGING}/scfw_tcm.bin -ap u-boot-atf.bin a53 0x80000000 -ap boot-sdp.scr a53 0x82e00000 \
        -ap ${DEPLOY_DIR_IMAGE}/hdmitxfw.bin a53 0x82fe0000 -ap tezi.itb a53 0x83000000 -out ${DEPLOYDIR}/recovery.bin

    ${BOOT_STAGING}/${TOOLS_NAME} -soc QM -rev B0 -append ${BOOT_STAGING}/mx8qm-ahab-container.img \
        -c -scfw ${BOOT_STAGING}/scfw_tcm.bin -ap u-boot-atf.bin a53 0x80000000 -out ${DEPLOYDIR}/flash.bin
}
