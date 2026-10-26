@echo off
cd /d "%~dp0"
set IP=10.0.2.2
if not "%1"=="" set IP=%1
java Cliente %IP%
pause
