@echo off

echo [93mDownloading Toradex Easy Installer...[0m

set DFU_UTIL=recovery\dfu-util

REM USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A53 SPL
set VID_PID_ROM="0451:6165"
set VID_PID_R5="0451:6165"
set VID_PID_A53="0451:6165"

REM tiboot3.bin depends on the SoC type, GP or HS-FS
set TIBOOT3_GP_BIN=tiboot3-am62x-gp-verdin.bin-dfu
set TIBOOT3_HSFS_BIN=tiboot3-am62x-hs-fs-verdin.bin-dfu

call :select_tiboot3_bin
%DFU_UTIL% -w -R -a bootloader --device %VID_PID_ROM% -D %TIBOOT3_BIN%
%DFU_UTIL% -w -R -a tispl.bin --device %VID_PID_R5% -D tispl.bin
%DFU_UTIL% -w -a u-boot.img --device %VID_PID_A53% -D u-boot.img-recoverytezi
%DFU_UTIL% -w -a ramdisk_addr_r --device %VID_PID_A53% -D tezi.itb
%DFU_UTIL% -w -a loadaddr --device %VID_PID_A53% -D overlays.txt
%DFU_UTIL% -w -R -a scriptaddr --device %VID_PID_A53% -D boot-tezi.scr
goto :EOF

REM set TIBOOT3_BIN variable according to the SoC type (GP or HF-FS)
:select_tiboot3_bin
del /Q SocId.bin SocType.bin 2> nul
%DFU_UTIL% -w -R -a SocId -U SocId.bin
recovery\dd if=SocId.bin bs=1 count=4 skip=20 of=SocType.bin
fc /B SocType.bin recovery/SocTypeGP.bin > nul
IF %ERRORLEVEL% EQU 0 (
	set TIBOOT3_BIN=%TIBOOT3_GP_BIN%
) ELSE (
	set TIBOOT3_BIN=%TIBOOT3_HSFS_BIN%
)
del /Q SocId.bin SocType.bin
exit /b

:EOF
