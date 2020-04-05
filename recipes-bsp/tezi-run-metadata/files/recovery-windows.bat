@echo off

echo [93mDownloading Toradex Easy Installer...[0m
recovery\imx_usb.exe

echo
IF %ERRORLEVEL% EQU 0 (
  echo [92mSuccessfully downloaded Toradex Easy Installer.[0m
) else (
  echo [91mDownloading Toradex Easy Installer failed...[0m
)
pause
