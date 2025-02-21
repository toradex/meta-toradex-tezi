@echo off

echo [93mDownloading Toradex Easy Installer...[0m

set DFU_UTIL=recovery\dfu-util
set UUU=recovery\uuu.exe

REM USB DFU device vendor and product ID used in the boot ROM, the R5 SPL and the A53 SPL
set VID_PID_ROM="0451:6165"
set VID_PID_R5="1b67:4000"
set VID_PID_A53="1b67:4000"

call :select_tiboot3_bin
%DFU_UTIL% -w -R -a bootloader --device %VID_PID_ROM% -D tiboot3-am62px-hs-fs-verdin.bin
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

exit /b

:EOF
