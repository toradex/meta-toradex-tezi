#!/bin/sh

#
# This script creates a Toradex Easy Installer release
# Execute in deploy directory of an OpenEmbedded "tezi"
# image build
#

SRCDIR=../../../layers/meta-toradex-tezi/scripts/
DSTDIR=Tezi_Apalis_iMX6_V0.5/

mkdir ${DSTDIR}
cp ${SRCDIR}/image.json ${DSTDIR}
cp ${SRCDIR}/wrapup.sh ${DSTDIR}
cp ${SRCDIR}/tezi.png ${DSTDIR}
cp -r ${SRCDIR}/recovery/ ${DSTDIR}
cp ${SRCDIR}/recovery-* ${DSTDIR}

# Externally compiled
cp ${SRCDIR}/SPL ${DSTDIR}
cp ${SRCDIR}/u-boot.img ${DSTDIR}

cp boot.scr ${DSTDIR}
cp boot-sdp.scr ${DSTDIR}
cp ${SRCDIR}/tezi.its ./
mkimage -f ./tezi.its ${DSTDIR}/tezi.itb
