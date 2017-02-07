@echo off

recovery\imx_usb.exe

IF %ERRORLEVEL% EQU 0 (
  echo
  echo == Successfully downloaded Tezi
)
pause
