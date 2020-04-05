@echo off

echo [93mDownloading Toradex Easy Installer...[0m
recovery\uuu.exe recovery

echo
IF %ERRORLEVEL% EQU 0 (
  echo [92mSuccessfully downloaded Toradex Easy Installer.[0m
) else (
  echo [91mDownloading Toradex Easy Installer failed...[0m
)
pause
