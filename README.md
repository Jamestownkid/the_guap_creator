# Wikipedia Autovideo - Desktop Edition

Automatically create narrated videos from Wikipedia articles with a user-friendly desktop app!

Example video: https://www.youtube.com/watch?v=IROxKUeGi0c

## 🚀 SUPER SIMPLE INSTALL

**Just downloaded this? Read:** **[INSTALL_SIMPLE.md](INSTALL_SIMPLE.md)**

One command block, launches GUI, easy setup.

## What Makes This Easy

- ✅ **Desktop GUI** - User-friendly JavaFX application  
- ✅ **One-Click Launch** - Run script, app opens
- ✅ **Auto-Fixes Graphics Issues** - Launcher handles JavaFX problems
- ✅ **Setup Wizard** - Guides you through API key setup
- ✅ **Progress Tracking** - See what's happening in real-time

## Linux Quick Start

```bash
sudo apt update && sudo apt install -y openjdk-17-jdk maven ffmpeg libgtk-3-0
cd ~/Downloads/wikipedia-autovideo-master
mvn clean package
chmod +x run-gui.sh
./run-gui.sh
```

## Windows Quick Start

Install Java 17, Maven, FFmpeg (add to PATH), then:

```
cd %USERPROFILE%\Downloads\wikipedia-autovideo-master
mvn clean package
run-gui.bat
```

Full instructions: [INSTALL_SIMPLE.md](INSTALL_SIMPLE.md)

# How to Run

## Option 1: Desktop GUI (Recommended)

You need Java 11+ and Maven.

```bash
# Build the project
mvn clean package

# Run the GUI
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

On first run, a setup wizard will ask for your API keys. No manual file editing needed.

Full instructions:
- **Linux:** [INSTALL_LINUX.md](INSTALL_LINUX.md)
- **Windows:** [INSTALL_WINDOWS.md](INSTALL_WINDOWS.md)

## Option 2: Command Line (Original)

You need Java 11+ and Maven.

1. Run `mvn clean package` in the root directory
2. Rename `autovideo.conf.example` to `autovideo.conf` and fill in your API keys
3. Run:
```bash
java -jar target/autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

This retrieves the New York Wikipedia page and creates the video using Amazon neural engine for speech synthesis. 
Use `standard` instead of `neural` for cheaper (but lower quality) audio.

## Dependencies

- **Java 11+** (Java 17 recommended)
- **Maven** (for building)
- **FFmpeg** (for video rendering)
- **Graphics libraries** (usually already installed): libgtk-3-0, libgl1-mesa-glx

That's it! No API keys, no accounts, no costs.

## Output
The program outputs is stored in the `output/final` directory. As the program runs, you will see files like `video.final.0.mp4`, `video.final.1.mp4` in the output directory: 
these are just intermediate steps that are later joined in a single video, but you can still watch them to see how the result is coming out. 

Before starting with the actual video generation, the program also creates an HTML file in `output/final` which provides a 
preview of the final video. It shows which images will end up in the video, how the video is divided in sections, with an estimated length for each one, and what text will be used.
