@echo off
REM Windows installer build script for Wikipedia Autovideo
REM Prerequisites:
REM   - JDK 17+ installed with jpackage tool
REM   - Maven installed and in PATH
REM   - Run this from the project root directory

echo Building Wikipedia Autovideo Windows Installer...
echo.

REM Step 1: Build the JAR with Maven
echo [1/3] Building application JAR...
call mvn clean package
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven build failed
    exit /b 1
)

REM Step 2: Find the GUI JAR
set JAR_FILE=target\autovideo-0.0.1-SNAPSHOT-gui.jar
if not exist %JAR_FILE% (
    echo ERROR: GUI JAR not found at %JAR_FILE%
    exit /b 1
)

echo [2/3] Creating application image with jpackage...

REM Step 3: Run jpackage to create Windows installer
jpackage ^
  --type exe ^
  --input target ^
  --name "Wikipedia Autovideo" ^
  --main-jar autovideo-0.0.1-SNAPSHOT-gui.jar ^
  --main-class com.autovideo.gui.WikipediaAutovideoApp ^
  --app-version 1.0 ^
  --vendor "Wikipedia Autovideo" ^
  --description "Generate narrated videos from Wikipedia articles" ^
  --icon packaging\windows\icon.ico ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --win-per-user-install ^
  --dest installer

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: jpackage failed
    exit /b 1
)

echo.
echo [3/3] Success!
echo Installer created in: installer\Wikipedia Autovideo-1.0.exe
echo.
echo You can now distribute this .exe file to Windows users.
echo Double-clicking it will install the app with a Start Menu entry.
pause

