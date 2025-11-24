@echo off
echo Building Color Converter...

if exist build rmdir /s /q build
if exist dist rmdir /s /q dist

mkdir build
mkdir dist

echo Compiling Java files...
javac -d build src\*.java

if errorlevel 1 (
    echo ERROR: Compilation failed! Check Java installation.
    pause
    exit /b 1
)

echo Creating JAR with manifest...
echo Main-Class: Main > manifest.txt
jar cfm dist\ColorConverter.jar manifest.txt -C build .
del manifest.txt

echo.
echo Build successful!
echo JAR file created: dist\ColorConverter.jar
echo.
pause