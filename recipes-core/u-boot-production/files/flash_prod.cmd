setenv set_blkcnt 'setexpr blkcnt ${filesize} + 0x1ff && setexpr blkcnt ${blkcnt} / 0x200'

mmc dev 0 1
load mmc 2:1 ${loadaddr} SPL && run set_blkcnt && mmc write ${loadaddr} 0x2 ${blkcnt}
load mmc 2:1 ${loadaddr} u-boot.img && run set_blkcnt && mmc write ${loadaddr} 0x8a ${blkcnt}

#Fuse...
pf0100_otp_prog
mfgr_fuse
mmc bootbus 0 2 0 1 && mmc partconf 0 1 1 0 && mmc rst-function 0 1
