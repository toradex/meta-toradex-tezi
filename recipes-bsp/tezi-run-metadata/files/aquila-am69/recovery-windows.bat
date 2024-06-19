@echo off

echo [93mDownloading Toradex Easy Installer...[0m

set DFU_UTIL=recovery\dfu-util

REM USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A72 SPL
set VID_PID_ROM="0451:6167"
set VID_PID_R5="1b67:4000"
set VID_PID_A72="1b67:4000"

set TIBOOT3_BIN=tiboot3-am69-hs-fs-aquila.bin

%DFU_UTIL% -w -R -a bootloader --device %VID_PID_ROM% -D %TIBOOT3_BIN%
%DFU_UTIL% -w -R -a tispl.bin --device %VID_PID_R5% -D tispl.bin
%DFU_UTIL% -w -a u-boot.img --device %VID_PID_A72% -D u-boot.img-recoverytezi
%DFU_UTIL% -w -a ramdisk_addr_r --device %VID_PID_A72% -D tezi.itb
%DFU_UTIL% -w -a loadaddr --device %VID_PID_A72% -D overlays.txt
%DFU_UTIL% -w -R -a scriptaddr --device %VID_PID_A72% -D boot.scr

exit /b
