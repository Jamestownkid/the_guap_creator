# Wikipedia Autovideo - Desktop Edition

Transform Wikipedia articles into narrated videos - now with a proper desktop GUI and installers for Windows and Linux.

## What's New

This project has been upgraded from a command-line tool to a full desktop application:

- **JavaFX GUI** - No more terminal commands required
- **Windows Installer** - Standard .exe installer with Start Menu integration
- **Linux Package** - .deb package for Ubuntu/Debian/Pop!_OS
- **Bundled JRE** - Users don't need Java installed
- **Config Management** - API keys managed through GUI, no manual file editing
- **Progress Tracking** - Real-time status updates and progress bar
- **Error Handling** - User-friendly error messages instead of stack traces

The original CLI still works for power users who prefer command-line tools.

## Quick Start

### For End Users (No Coding Required)

**Windows:**
1. Download `Wikipedia Autovideo-1.0.exe`
2. Double-click and follow installer
3. Launch from Start Menu
4. Enter API credentials in setup wizard
5. Type a Wikipedia article and click Generate

**Linux:**
1. Download `wikipedia-autovideo_1.0-1_amd64.deb`
2. Install: `sudo dpkg -i wikipedia-autovideo_1.0-1_amd64.deb`
3. Launch from application menu
4. Enter API credentials in setup wizard
5. Type a Wikipedia article and click Generate

### For Developers

**Build from source:**
```bash
mvn clean package
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

**Create installers:**
```bash
# Linux
cd packaging/linux && ./build-linux-package.sh

# Windows (on Windows machine)
cd packaging\windows && build-windows-installer.bat
```

## Requirements

### For End Users:
- **FFmpeg** installed and in PATH
  - Windows: https://ffmpeg.org/download.html
  - Linux: `sudo apt install ffmpeg`
- **API Keys** (free but registration required):
  - Pixabay API key: https://pixabay.com/api/docs/
  - AWS credentials: https://aws.amazon.com/polly/
- Internet connection

### For Developers:
- Java 11+ (Java 17 recommended)
- Maven 3.6+
- FFmpeg

## Features

### GUI Features
- Simple article input (title or URL)
- Voice engine selection (Neural/Standard)
- Language selection (EN, FR, ES, DE, IT, NL, ZH)
- Output directory picker
- Advanced settings for API keys and options
- Real-time progress tracking
- Status log with timestamps
- One-click folder opening
- First-run setup wizard
- Error validation and helpful messages

### Original CLI (Still Available)
```bash
java -jar target/autovideo-0.0.1-SNAPSHOT-cli.jar Article_Name neural
```

## Architecture

The project has been refactored with clean separation of concerns:

- **Service Layer** (`com.autovideo.service.*`)
  - Core business logic extracted from CLI
  - Reusable by both GUI and CLI
  - Progress tracking via listener interface
  
- **GUI Layer** (`com.autovideo.gui.*`)
  - JavaFX desktop application
  - Consumes service layer
  - Manages user interaction
  
- **Config Layer** (`com.autovideo.config.*`)
  - Manages autovideo.conf programmatically
  - No manual file editing required

Both CLI and GUI share the exact same video generation logic.

## Documentation

- **QUICKSTART.md** - Fast track to get running
- **BUILD.md** - Detailed build and installation instructions
- **NEXT_STEPS.md** - What to do after cloning the repo
- **IMPLEMENTATION_SUMMARY.md** - Complete technical overview
- **packaging/README.md** - Packaging and installer details

## Tech Stack

- **Java 11** (upgraded from Java 8)
- **JavaFX 17** for GUI
- **Maven** for build management
- **jpackage** for creating installers
- **Amazon Polly** for text-to-speech
- **Pixabay API** for images
- **FFmpeg** for video rendering

## Cost Warning

**This tool uses AWS Polly which charges money:**
- Neural voices: ~$16 per 1 million characters
- Standard voices: ~$4 per 1 million characters
- Example: A 5000-word article costs roughly $0.30 with neural voices

Monitor your AWS billing. Start with short articles for testing.

## Screenshots

(Add screenshots of your GUI here once you've built it)

## Building Installers

### Prerequisites
- JDK 17+ (includes jpackage)
- Maven 3.6+
- Icon files (or use the placeholder script)

### Create Icons
```bash
./packaging/create-placeholder-icons.sh
```

### Build Windows Installer
(On Windows machine)
```bash
mvn clean package
cd packaging\windows
build-windows-installer.bat
```

Output: `installer/Wikipedia Autovideo-1.0.exe`

### Build Linux Package
(On Linux machine)
```bash
mvn clean package
cd packaging/linux
./build-linux-package.sh
```

Output: `installer/wikipedia-autovideo_1.0-1_amd64.deb`

## File Structure

```
wikipedia-autovideo-master/
├── src/main/java/com/autovideo/
│   ├── gui/                           # NEW - JavaFX desktop app
│   ├── service/                       # NEW - Core business logic
│   ├── config/                        # NEW - Config management
│   └── ... (existing packages)
├── packaging/
│   ├── windows/
│   │   ├── build-windows-installer.bat
│   │   └── icon.ico (you provide)
│   ├── linux/
│   │   ├── build-linux-package.sh
│   │   ├── icon.png (you provide)
│   │   └── wikipedia-autovideo.desktop
│   └── README.md
├── pom.xml                            # UPDATED - Java 11, JavaFX
├── BUILD.md                           # NEW - Build guide
├── QUICKSTART.md                      # NEW - Quick reference
├── NEXT_STEPS.md                      # NEW - Action items
└── IMPLEMENTATION_SUMMARY.md          # NEW - Technical details
```

## Changes from Original

### What Changed:
- Java 8 → Java 11 (required for JavaFX)
- Added JavaFX GUI
- Refactored core logic into service layer
- Added config management system
- Added packaging scripts for installers
- Creates two JARs: gui and cli

### What Stayed the Same:
- All original video generation logic
- CLI still works identically
- Same command syntax
- Same config file format
- Same output format
- Same dependencies (plus JavaFX)

## Contributing

If you want to improve this:
1. Fork the repo
2. Make your changes
3. Test both GUI and CLI
4. Submit a pull request

Potential improvements:
- Video preview in GUI
- Batch processing
- More Polly voice options
- Dark mode
- Direct YouTube upload
- Pause/resume capability

## License

(Add your license here)

Check licenses of all dependencies:
- Amazon Polly (AWS terms)
- Pixabay (attribution requirements)
- FFmpeg (LGPL/GPL)
- All Maven dependencies (see pom.xml)

## Credits

Original CLI tool: (original author if not you)
Desktop refactor: (you)

Dependencies:
- AWS SDK for Java (Amazon Polly)
- Pixabay API
- FFmpeg
- JavaFX
- (and many others - see pom.xml)

## Support

For issues:
1. Check the documentation (BUILD.md, QUICKSTART.md)
2. Check FFmpeg is installed: `ffmpeg -version`
3. Check Java version: `java -version` (needs 11+)
4. Check API credentials are valid
5. Look at the error message carefully
6. Search for the error online

Common issues and solutions are in BUILD.md.

## What This Tool Does

1. Takes a Wikipedia article title
2. Downloads the article content
3. Gets related images from Wikipedia and Pixabay
4. Splits content into sections
5. Generates speech audio using Amazon Polly
6. Combines images and audio into a video using FFmpeg
7. Outputs a narrated video file

Perfect for:
- Educational content creators
- YouTubers covering topics
- Learning about Wikipedia subjects
- Automated video generation
- Text-to-video experiments

## Getting API Keys

### Pixabay
1. Go to https://pixabay.com/
2. Sign up for free account
3. Go to https://pixabay.com/api/docs/
4. Copy your API key

### AWS (for Polly)
1. Sign up at https://aws.amazon.com/
2. Go to IAM console
3. Create new user with AmazonPollyFullAccess permission
4. Generate access keys
5. Save the access key ID and secret key
6. Choose a region (e.g., us-east-1)

**Important:** Enable billing alerts in AWS so you don't get surprised by charges.

## Example Usage

### GUI:
1. Launch the app
2. Type: `New_York`
3. Select: Neural voice, English
4. Choose output folder
5. Click Generate
6. Wait for completion (shows progress)
7. Video saved to output folder

### CLI:
```bash
java -jar autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

Both produce the same video, just different interfaces.

## Troubleshooting

**"FFmpeg not found"**
- Install FFmpeg and add to system PATH
- Verify with: `ffmpeg -version`

**"API key invalid"**
- Check you copied keys correctly
- No extra spaces
- Check AWS region matches your account

**"Out of memory"**
- Long articles need more RAM
- Use: `java -Xmx4g -jar ...`

**GUI won't launch**
- Check Java version: `java -version` (need 11+)
- Try: `java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar`
- Check for error messages in terminal

**Installer creation fails**
- Need JDK 17+ (not JRE)
- Check: `jpackage --version`
- Create icon files first

More troubleshooting in BUILD.md.

---

Ready to turn Wikipedia into videos? Get started with QUICKSTART.md or dive deep with BUILD.md.

