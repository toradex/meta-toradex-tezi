@echo off

echo [93mDownloading Toradex Easy Installer...[0m

set DFU_UTIL=recovery\dfu-util
set UUU=recovery\uuu.exe

REM USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A53 SPL
set VID_PID_ROM="0451:6165"
set VID_PID_R5="1b67:4000"
set VID_PID_A53="1b67:4000"

REM tiboot3.bin depends on the SoC type, GP or HS-FS
set TIBOOT3_GP_BIN=tiboot3-am62x-gp-verdin.bin
set TIBOOT3_HSFS_BIN=tiboot3-am62x-hs-fs-verdin.bin

call :select_tiboot3_bin
%DFU_UTIL% -w -R -a bootloader --device %VID_PID_ROM% -D %TIBOOT3_BIN%
%DFU_UTIL% -w -R -a tispl.bin --device %VID_PID_R5% -D tispl.bin
%DFU_UTIL% -w -R -a u-boot.img --device %VID_PID_A53% -D u-boot.img-recoverytezi

REM call universal update utility (uuu) to download FIT with fastboot
%UUU% recovery

echo
IF %ERRORLEVEL% EQU 0 (
  echo [92mSuccessfully downloaded Toradex Easy Installer.[0m
) else (
  echo [91mDownloading Toradex Easy Installer failed...[0m
)
pause

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
