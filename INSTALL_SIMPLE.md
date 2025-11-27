# Super Simple Install

Desktop GUI for creating Wikipedia videos with easy setup!

## Linux (Ubuntu/Pop!_OS/Debian)

Just copy-paste this entire block:

```bash
# Install Java and build tools
sudo apt update && sudo apt install -y openjdk-17-jdk maven ffmpeg libgtk-3-0 libgl1-mesa-glx

# Go to project
cd ~/Downloads/wikipedia-autovideo-master

# Build it
mvn clean package

# Make launcher executable
chmod +x run-gui.sh

# Run it!
./run-gui.sh
```

That's it! A setup wizard will guide you through entering your API keys on first run.

## Windows

1. Install Java 17 from: https://adoptium.net/ (check "Add to PATH")
2. Install Maven from: https://maven.apache.org/download.cgi (add to PATH)
3. Install FFmpeg from: https://ffmpeg.org/download.html (add to PATH)

Then open Command Prompt:

```
cd %USERPROFILE%\Downloads\wikipedia-autovideo-master
mvn clean package
run-gui.bat
```

Done! The GUI will open and show a setup wizard for API keys.

## Using It

1. Type a Wikipedia article (like `Albert_Einstein`)
2. Pick voice type
3. Pick language
4. Click Generate
5. Wait for your video

## Getting API Keys

You'll need (one-time setup):

1. **Pixabay API Key** (free): https://pixabay.com/api/docs/
2. **AWS Credentials** (costs ~$0.30 per article): https://aws.amazon.com/polly/

The GUI wizard walks you through this on first run.

## What's New?

- ✅ Desktop GUI instead of command-line
- ✅ Setup wizard for easy configuration  
- ✅ Automatic fix for JavaFX graphics issues
- ✅ Progress tracking
- ✅ One-click launcher

## Troubleshooting

**GUI won't start?**
Try installing graphics libraries:
```bash
sudo apt install -y libgtk-3-0 libgl1-mesa-glx libgl1-mesa-dri
```

**"FFmpeg not found"?**
```bash
sudo apt install ffmpeg
```

**"Maven not found"?**
```bash
sudo apt install maven
```

That's literally it. No complicated setup anymore.

