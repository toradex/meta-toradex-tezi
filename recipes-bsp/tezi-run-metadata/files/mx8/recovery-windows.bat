@echo off

echo [93mDownloading Toradex Easy Installer...[0m
recovery\uuu recovery.bin

echo
IF %ERRORLEVEL% EQU 0 (
  echo [92mSuccessfully downloaded Tezi[0m
)
else (
  echo [91mFailed downloading Tezi[0m
)
pause
