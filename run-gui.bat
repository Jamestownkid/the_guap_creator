@echo off
REM Launcher script for Windows

REM Force JavaFX to use software rendering if needed
set _JAVA_OPTIONS=-Dprism.order=sw

REM Run the GUI
java -jar "%~dp0target\autovideo-0.0.1-SNAPSHOT-gui.jar" %*

