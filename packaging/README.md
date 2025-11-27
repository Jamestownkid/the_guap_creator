# Packaging Wikipedia Autovideo

This directory contains resources and scripts for building distributable installers.

## Prerequisites

Before building installers, you need:

1. **JDK 17 or later** (for jpackage tool)
   - Download from: https://adoptium.net/
   - Verify with: `java -version` and `jpackage --version`

2. **Maven 3.6+**
   - Verify with: `mvn -version`

3. **FFmpeg** (included in built installers or user must install separately)

## Building Installers

### Windows (.exe installer)

From the project root:

```bash
cd packaging/windows
build-windows-installer.bat
```

This creates: `installer/Wikipedia Autovideo-1.0.exe`

**What it includes:**
- Bundled JRE (no Java installation needed)
- Start Menu shortcut
- Desktop shortcut option
- Standard Windows installer wizard

**Distribution:**
- Send the .exe to Windows users
- They double-click and follow the installer
- App installs to Program Files

### Linux (.deb package)

From the project root:

```bash
cd packaging/linux
chmod +x build-linux-package.sh
./build-linux-package.sh
```

This creates: `installer/wikipedia-autovideo_1.0-1_amd64.deb`

**What it includes:**
- Bundled JRE (no Java installation needed)
- .desktop file for application menu
- Icon for launcher

**Installation:**
```bash
sudo dpkg -i installer/wikipedia-autovideo_1.0-1_amd64.deb
```

**Uninstallation:**
```bash
sudo dpkg -r wikipedia-autovideo
```

**Distribution:**
- Provide the .deb file to Ubuntu/Debian/Pop!_OS users
- They can install via dpkg or by double-clicking (opens Software Center)

## Icon Files

### Windows
- `windows/icon.ico` - Windows icon file (256x256 recommended)
- Created from source image with multiple resolutions embedded

### Linux
- `linux/icon.png` - PNG icon (256x256 or 512x512 recommended)
- Used for both the .deb package and desktop file

**Creating icons:**

If you don't have icon files yet, you can create them:

1. Create a 512x512 PNG image with your app logo
2. For Windows .ico:
   - Use an online converter like: https://convertio.co/png-ico/
   - Or use ImageMagick: `convert icon.png -define icon:auto-resize=256,128,64,48,32,16 icon.ico`
3. For Linux PNG: just use the 512x512 PNG directly

## Manual jpackage Commands

If you want to customize the build, here are the base commands:

### Windows
```bash
jpackage \
  --type exe \
  --input target \
  --name "Wikipedia Autovideo" \
  --main-jar autovideo-0.0.1-SNAPSHOT-gui.jar \
  --main-class com.autovideo.gui.WikipediaAutovideoApp \
  --app-version 1.0 \
  --vendor "Wikipedia Autovideo" \
  --icon packaging/windows/icon.ico \
  --win-dir-chooser \
  --win-menu \
  --win-shortcut \
  --dest installer
```

### Linux
```bash
jpackage \
  --type deb \
  --input target \
  --name wikipedia-autovideo \
  --main-jar autovideo-0.0.1-SNAPSHOT-gui.jar \
  --main-class com.autovideo.gui.WikipediaAutovideoApp \
  --app-version 1.0 \
  --vendor "Wikipedia Autovideo" \
  --icon packaging/linux/icon.png \
  --linux-menu-group "AudioVideo;Video;" \
  --linux-shortcut \
  --dest installer
```

## Troubleshooting

### jpackage not found
- Make sure you have JDK 17+, not just JRE
- Add JDK bin directory to PATH

### JavaFX errors
- The shade plugin bundles JavaFX, so this should not happen
- If it does, make sure Maven build completes successfully first

### Missing ffmpeg
- Windows: User must install ffmpeg separately and add to PATH
- Linux: User can install via package manager: `sudo apt install ffmpeg`
- Future improvement: Bundle ffmpeg with the installer (check license)

## Alternative: AppImage for Linux

If you prefer AppImage over .deb:

1. Use a tool like `appimage-builder` or `linuxdeploy`
2. Package the JAR with a bundled JRE
3. Create a single executable file

This is more work but gives you a truly portable Linux app.

