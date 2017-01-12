@echo off

recovery\imx_usb.exe -b

IF %ERRORLEVEL% EQU 0 (
  echo
  echo == Successfully downloaded Tezi
)
pause
