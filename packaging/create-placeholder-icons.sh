#!/bin/bash
# Quick script to create placeholder icons for testing
# For production, replace these with actual branded icons

echo "Creating placeholder icons..."

# Create directories if they don't exist
mkdir -p packaging/windows
mkdir -p packaging/linux

# Check if ImageMagick is installed
if ! command -v convert &> /dev/null; then
    echo "ImageMagick not found. Installing..."
    if [[ "$OSTYPE" == "linux-gnu"* ]]; then
        sudo apt install imagemagick -y
    else
        echo "Please install ImageMagick manually: https://imagemagick.org/"
        exit 1
    fi
fi

# Create a simple blue gradient icon with "WA" text
convert -size 512x512 \
    gradient:blue-lightblue \
    -font DejaVu-Sans-Bold \
    -pointsize 200 \
    -fill white \
    -gravity center \
    -annotate +0+0 "WA" \
    packaging/linux/icon.png

echo "Created: packaging/linux/icon.png"

# Convert to Windows .ico format
convert packaging/linux/icon.png \
    -define icon:auto-resize=256,128,64,48,32,16 \
    packaging/windows/icon.ico

echo "Created: packaging/windows/icon.ico"
echo ""
echo "Placeholder icons created successfully!"
echo "For production, replace these with proper branded icons."

