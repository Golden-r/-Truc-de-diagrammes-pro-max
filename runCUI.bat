@echo off
setlocal enabledelayedexpansion

REM Force UTF-8 encoding
chcp 65001 >nul

if not exist "class" mkdir "class"

call compileList.bat
if errorlevel 1 exit /b %ERRORLEVEL%

javac -encoding UTF-8 -cp "lib/*" -d "class" @compile.list
if errorlevel 1 exit /b %ERRORLEVEL%

java -Dfile.encoding=UTF-8 -cp "class;lib/*" src.ControleurCUI %*
exit /b %ERRORLEVEL%