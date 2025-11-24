@echo off
chcp 65001 >nul
title Color Converter

echo ========================================
echo    Starting Color Converter
echo ========================================

if not exist "dist\ColorConverter.jar" (
    echo JAR file not found!
    echo Running build process...
    echo.
    call build.bat
)

if exist "dist\ColorConverter.jar" (
    echo.
    echo Launching application...
    java -jar "dist\ColorConverter.jar"
) else (
    echo.
    echo ERROR: Could not create or find JAR file!
    echo Trying to run directly from class files...
    java -cp "build" Main
)

echo.
echo Application closed.
pause