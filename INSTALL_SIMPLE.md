# Super Simple Install - No API Keys Needed!

This version uses **FREE local text-to-speech** - no AWS account, no costs, no API keys!

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

That's it! No setup wizard, no API keys, just works.

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

Done! No API keys needed.

## Using It

1. Type a Wikipedia article (like `Albert_Einstein`)
2. Pick voice type
3. Pick language
4. Click Generate
5. Wait for your video

**No costs, no API keys, completely free and offline!**

## What Changed?

- ✅ Uses MaryTTS (free, local text-to-speech)
- ✅ No AWS Polly (no costs!)
- ✅ No Pixabay API needed
- ✅ Works completely offline after first build
- ✅ Automatic fix for JavaFX graphics issues
- ✅ Just run the script, that's it

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

