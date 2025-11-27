#!/bin/bash
# Linux .deb package build script for Wikipedia Autovideo
# Prerequisites:
#   - JDK 17+ installed with jpackage tool
#   - Maven installed and in PATH
#   - Run this from the project root directory

set -e  # Exit on any error

echo "Building Wikipedia Autovideo Linux Package..."
echo ""

# Step 1: Build the JAR with Maven
echo "[1/3] Building application JAR..."
mvn clean package

JAR_FILE="target/autovideo-0.0.1-SNAPSHOT-gui.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "ERROR: GUI JAR not found at $JAR_FILE"
    exit 1
fi

echo "[2/3] Creating .deb package with jpackage..."

# Step 2: Run jpackage to create .deb package
jpackage \
  --type deb \
  --input target \
  --name wikipedia-autovideo \
  --main-jar autovideo-0.0.1-SNAPSHOT-gui.jar \
  --main-class com.autovideo.gui.WikipediaAutovideoApp \
  --app-version 1.0 \
  --vendor "Wikipedia Autovideo" \
  --description "Generate narrated videos from Wikipedia articles" \
  --icon packaging/linux/icon.png \
  --linux-menu-group "AudioVideo;Video;" \
  --linux-shortcut \
  --dest installer

echo ""
echo "[3/3] Success!"
echo "Package created in: installer/wikipedia-autovideo_1.0-1_amd64.deb"
echo ""
echo "To install on Ubuntu/Debian/Pop!_OS:"
echo "  sudo dpkg -i installer/wikipedia-autovideo_1.0-1_amd64.deb"
echo ""
echo "The app will appear in your application menu as 'Wikipedia Autovideo'"

