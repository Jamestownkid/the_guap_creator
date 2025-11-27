# Wikipedia Autovideo - Desktop Edition

This Java program automatically creates narrated videos starting from Wikipedia pages, using images from the page itself and other sources
(Pixabay), and doing speech synthesis for the text using Amazon Polly API.

**NEW:** Now includes a full JavaFX desktop GUI with installers for Windows and Linux! No more command-line required.

Example video: https://www.youtube.com/watch?v=IROxKUeGi0c
Read more: [How it works](https://aileftech.wordpress.com/2020/04/29/turn-any-wikipedia-article-into-a-video-automatically/)

## Quick Install

**For Linux users who just downloaded this:**
See **[INSTALL_LINUX.md](INSTALL_LINUX.md)** for simple copy-paste instructions.

**For Windows users who just downloaded this:**
See **[INSTALL_WINDOWS.md](INSTALL_WINDOWS.md)** for simple copy-paste instructions.

## What's New

- **Desktop GUI** - User-friendly JavaFX application
- **Windows Installer** - Standard .exe installer with bundled Java
- **Linux Package** - .deb package for Ubuntu/Debian/Pop!_OS
- **No manual config editing** - Setup wizard on first run
- **Real-time progress** - See what's happening during video generation
- **Better error handling** - Clear messages instead of crashes

The original command-line interface still works for power users.

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
This tool uses `ffmpeg`, so you need it installed with a version that's compatible with the ffmpeg filters that we are using. Can't tell an exact number but any version released after 2020 should be ok.

Also, to perform image conversions (e.g. SVG to JPG) the tool uses the `convert` command from `imagemagick`.

## Output
The program outputs is stored in the `output/final` directory. As the program runs, you will see files like `video.final.0.mp4`, `video.final.1.mp4` in the output directory: 
these are just intermediate steps that are later joined in a single video, but you can still watch them to see how the result is coming out. 

Before starting with the actual video generation, the program also creates an HTML file in `output/final` which provides a 
preview of the final video. It shows which images will end up in the video, how the video is divided in sections, with an estimated length for each one, and what text will be used.
