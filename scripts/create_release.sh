#!/bin/sh

#
# This script creates a Toradex Easy Installer release
# Execute in deploy directory of an OpenEmbedded "tezi"
# image build
#

MACHINE=apalis-imx6
#MACHINE=colibri-imx6
DSTDIR=Tezi_Apalis_iMX6_V0.6/
#DSTDIR=Tezi_Colibri_iMX6_V0.6/
SRCDIR=../../../layers/meta-toradex-tezi/scripts/

mkdir ${DSTDIR}
cp ${SRCDIR}/image-${MACHINE}.json ${DSTDIR}/image.json
cp ${SRCDIR}/wrapup.sh ${DSTDIR}
cp ${SRCDIR}/tezi.png ${DSTDIR}
cp -r ${SRCDIR}/recovery/ ${DSTDIR}
cp ${SRCDIR}/recovery-* ${DSTDIR}

cp SPL ${DSTDIR}
cp u-boot.img ${DSTDIR}

cp boot.scr ${DSTDIR}
cp boot-sdp.scr ${DSTDIR}
cp ${SRCDIR}/tezi-${MACHINE}.its ./tezi.its
mkimage -f ./tezi.its ${DSTDIR}/tezi.itb
