# Build and Installation Guide

Complete guide for building Wikipedia Autovideo from source and creating installers.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Building from Source](#building-from-source)
- [Creating Installers](#creating-installers)
- [End User Installation](#end-user-installation)

## Prerequisites

### For Building
- **Java 11+** (Java 17 recommended for jpackage)
  - Download: https://adoptium.net/
  - Check: `java -version`
- **Maven 3.6+**
  - Download: https://maven.apache.org/download.cgi
  - Check: `mvn -version`
- **FFmpeg** (for video generation)
  - Windows: https://ffmpeg.org/download.html
  - Linux: `sudo apt install ffmpeg`
  - Check: `ffmpeg -version`

### API Credentials
You'll need:
1. **Pixabay API Key**: Register at https://pixabay.com/api/docs/
2. **AWS Credentials**: Get from AWS Console for Amazon Polly access
   - Access Key ID
   - Secret Access Key
   - Region (e.g., us-east-1)

## Building from Source

### 1. Clone/Download the Repository
```bash
cd /path/to/wikipedia-autovideo-master
```

### 2. Build with Maven
```bash
mvn clean package
```

This creates two JAR files in `target/`:
- `autovideo-0.0.1-SNAPSHOT-gui.jar` - Desktop GUI app
- `autovideo-0.0.1-SNAPSHOT-cli.jar` - Command-line tool

### 3. Run the GUI Application
```bash
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

Or use Maven:
```bash
mvn javafx:run
```

### 4. Run the CLI Application (old way)
```bash
# First, create autovideo.conf with your credentials
cp autovideo.conf.example autovideo.conf
# Edit autovideo.conf and fill in your API keys

# Then run
java -jar target/autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

## Creating Installers

### Windows Installer (.exe)

**Requirements:**
- JDK 17+ with jpackage
- Windows 10+

**Build Steps:**
```bash
# From project root
mvn clean package
cd packaging/windows
build-windows-installer.bat
```

**Output:**
- `installer/Wikipedia Autovideo-1.0.exe` (approx 200-300 MB with bundled JRE)

**What Users Get:**
- Normal Windows installer wizard
- Start Menu shortcut
- Optional desktop shortcut
- Installs to Program Files
- Bundled Java (no Java install needed)

### Linux Package (.deb)

**Requirements:**
- JDK 17+ with jpackage
- Linux (Ubuntu/Debian/Pop!_OS)

**Build Steps:**
```bash
# From project root
mvn clean package
cd packaging/linux
chmod +x build-linux-package.sh
./build-linux-package.sh
```

**Output:**
- `installer/wikipedia-autovideo_1.0-1_amd64.deb` (approx 200-300 MB)

**What Users Get:**
- Standard .deb package
- Application menu entry (under Audio & Video)
- Icon in launcher
- Bundled Java (no Java install needed)

### Icon Files Note

The scripts reference icon files that you need to provide:
- `packaging/windows/icon.ico` - Windows icon (256x256)
- `packaging/linux/icon.png` - Linux icon (512x512 PNG)

If you don't have icons yet, create placeholder files or use any image. For production, create proper icons:

**Quick placeholder creation:**
```bash
# Linux placeholder
mkdir -p packaging/linux
# Create a simple colored square or use any PNG
convert -size 512x512 xc:blue packaging/linux/icon.png

# Windows - convert from PNG
convert packaging/linux/icon.png -define icon:auto-resize=256,128,64,48,32,16 packaging/windows/icon.ico
```

Or just grab any free icon from the internet temporarily.

## End User Installation

### Windows

**What Users Do:**
1. Download `Wikipedia Autovideo-1.0.exe`
2. Double-click the .exe file
3. Click through the installer wizard:
   - Accept license
   - Choose install location (default: Program Files)
   - Choose shortcuts (Start Menu recommended)
   - Click Install
4. Launch from Start Menu: "Wikipedia Autovideo"

**First Run:**
- App will show setup wizard asking for API credentials
- Enter Pixabay key and AWS credentials
- Click Save
- Start generating videos

**Requirements for Users:**
- Windows 10 or later
- FFmpeg installed (show them this: https://www.geeksforgeeks.org/how-to-install-ffmpeg-on-windows/)
- Internet connection

### Linux (Ubuntu/Debian/Pop!_OS)

**What Users Do:**

Option 1 - Terminal:
```bash
sudo dpkg -i wikipedia-autovideo_1.0-1_amd64.deb
```

Option 2 - GUI:
1. Download the .deb file
2. Double-click it (opens Software Center)
3. Click Install
4. Enter password when prompted

**Launch:**
- Open application menu
- Search for "Wikipedia Autovideo"
- Click to launch

Or from terminal:
```bash
wikipedia-autovideo
```

**First Run:**
- Same setup wizard as Windows
- Enter API credentials
- Start generating

**Requirements for Users:**
- Ubuntu 20.04+, Debian 11+, or Pop!_OS 20.04+
- FFmpeg: `sudo apt install ffmpeg` (show them this)
- Internet connection

**Uninstall:**
```bash
sudo dpkg -r wikipedia-autovideo
```

## Development Workflow

### For Local Development
```bash
# Run GUI directly with Maven
mvn clean javafx:run

# Or build and run JAR
mvn clean package
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

### Code Structure
- `com.autovideo.gui.WikipediaAutovideoApp` - Main GUI class
- `com.autovideo.service.WikipediaVideoService` - Core business logic
- `com.autovideo.service.WikiVideoRequest` - Request model
- `com.autovideo.service.ProgressListener` - Progress callback interface
- `com.autovideo.config.ConfigManager` - Config file management
- `com.autovideo.wiki.WikiVideoCreator` - CLI entry point (now uses service layer)

### Changes from Original
1. **Bumped Java from 8 to 11** (required for JavaFX modules)
2. **Added JavaFX dependencies** (17.0.2)
3. **Refactored core logic** into service layer (shared by CLI and GUI)
4. **Added config management** (no more manual editing of .conf file)
5. **Created GUI** with progress tracking
6. **Two JARs now built**: GUI and CLI versions

### Compatibility Notes
- CLI still works exactly as before (same commands)
- Old `autovideo.conf` files are compatible
- All existing code paths preserved, just refactored into service

## Troubleshooting

### Build Issues

**Problem:** Maven can't find dependencies
```bash
mvn clean install -U
```

**Problem:** JavaFX errors
- Make sure you're using Java 11+
- JavaFX should be bundled by shade plugin

**Problem:** jpackage not found
- You need JDK 17+, not JRE
- Add JDK bin directory to PATH

### Runtime Issues

**Problem:** FFmpeg not found
- User needs to install ffmpeg and add to PATH
- Show clear error message in GUI

**Problem:** API key errors
- Check `autovideo.conf` has valid keys
- Test AWS credentials separately
- Test Pixabay key at their API docs page

**Problem:** Network errors
- Check internet connection
- Some Wikipedia pages might not exist
- Check AWS region is correct

## License Notes

Make sure you understand the licenses of:
- All Maven dependencies (check pom.xml)
- FFmpeg (LGPL/GPL depending on build)
- Pixabay images (free but check attribution requirements)
- AWS Polly (costs money per character synthesized)

Distribute accordingly and inform users about costs (Polly charges).

