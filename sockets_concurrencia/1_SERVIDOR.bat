@echo off
cd /d "%~dp0"
echo Compilando...
javac -source 8 -target 8 Servidor.java Cliente.java
if errorlevel 1 (echo ERROR: no se encontro javac. Instala el JDK. & pause & exit /b)
echo.
echo Listo. Copia Cliente.class y 2_CLIENTE.bat a la maquina virtual.
echo.
java Servidor
pause
