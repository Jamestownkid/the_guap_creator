Wikipedia Autovideo - Linux Package Builder
============================================

PREREQUISITES:
- JDK 17 or higher (must include jpackage tool)
- Maven 3.6 or higher
- Icon file: icon.png (see below)

BUILDING THE PACKAGE:

1. From the project root directory, run:
   mvn clean package

2. Navigate to this directory and run:
   chmod +x build-linux-package.sh
   ./build-linux-package.sh

3. The .deb package will be created in:
   ../../installer/wikipedia-autovideo_1.0-1_amd64.deb

CREATING THE ICON:

You need to provide an icon.png file in this directory.
Recommended size: 512x512 or 256x256 PNG.

Quick way:
1. Find or create a square PNG image
2. Resize to 512x512 if needed
3. Save as icon.png in this directory

Using ImageMagick:
  convert myicon.png -resize 512x512 icon.png

CUSTOMIZATION:

Edit build-linux-package.sh to change:
- Package name
- Version number
- Vendor name
- Description
- Menu categories

Edit wikipedia-autovideo.desktop to customize the desktop entry.

DISTRIBUTION:

The generated .deb file works on:
- Ubuntu 20.04+
- Debian 11+
- Pop!_OS 20.04+
- Other Debian-based distros

Users install with:
  sudo dpkg -i wikipedia-autovideo_1.0-1_amd64.deb

Or by double-clicking (opens Software Center).

TESTING:

After building, test locally:
  sudo dpkg -i ../../installer/wikipedia-autovideo_1.0-1_amd64.deb
  wikipedia-autovideo

Uninstall with:
  sudo dpkg -r wikipedia-autovideo

NOTES:

- Package size: ~200-300 MB (includes JRE + dependencies)
- Install location: /opt/wikipedia-autovideo/
- Binary location: /opt/wikipedia-autovideo/bin/wikipedia-autovideo
- Desktop file: /usr/share/applications/wikipedia-autovideo.desktop
- Icon: /usr/share/icons/hicolor/512x512/apps/wikipedia-autovideo.png
- Users still need ffmpeg: sudo apt install ffmpeg

ALTERNATIVE - AppImage:

For a more portable single-file solution, consider creating an AppImage instead.
This requires additional tools like appimage-builder or linuxdeploy.
The .deb approach is simpler and more native-feeling for Ubuntu/Debian users.

