Wikipedia Autovideo - Windows Installer Builder
=================================================

PREREQUISITES:
- JDK 17 or higher (must include jpackage tool)
- Maven 3.6 or higher
- Icon file: icon.ico (see below)

BUILDING THE INSTALLER:

1. From the project root directory, run:
   mvn clean package

2. Navigate to this directory and run:
   build-windows-installer.bat

3. The installer will be created in:
   ../../installer/Wikipedia Autovideo-1.0.exe

CREATING THE ICON:

You need to provide an icon.ico file in this directory.
The icon should contain multiple sizes: 256x256, 128x128, 64x64, 48x48, 32x32, 16x16.

Quick way to create one:
1. Create a 512x512 PNG image
2. Use an online converter: https://convertio.co/png-ico/
3. Save as icon.ico in this directory

Or use ImageMagick:
  convert myicon.png -define icon:auto-resize=256,128,64,48,32,16 icon.ico

CUSTOMIZATION:

Edit build-windows-installer.bat to change:
- App name
- Version number
- Vendor name
- Description
- Install options

DISTRIBUTION:

The generated .exe file is a complete installer.
Users just double-click it - no other files needed.
The installer includes a bundled Java runtime.

NOTES:

- Installer size: ~200-300 MB (includes JRE + dependencies)
- Target: Windows 10 and later
- Install location: Program Files\Wikipedia Autovideo
- Creates Start Menu entry
- Optional desktop shortcut
- Users still need FFmpeg installed separately

