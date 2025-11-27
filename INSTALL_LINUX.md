# Installation Instructions for Linux Users

If you downloaded this project from GitHub, here's what to do.

## Prerequisites

You need these installed first:

```bash
# Install Java 11 or higher
sudo apt update
sudo apt install openjdk-17-jdk maven -y

# Install FFmpeg (required for video generation)
sudo apt install ffmpeg -y

# Verify installations
java -version    # should show 11 or higher
mvn -version     # should show Maven 3.x
ffmpeg -version  # should show FFmpeg version
```

## Step 1: Navigate to the Project

```bash
cd ~/Downloads/wikipedia-autovideo-master
```

## Step 2: Build the Application

```bash
mvn clean package
```

This will take a few minutes the first time as it downloads dependencies.

## Step 3: Run the Desktop App

```bash
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

The GUI window will open. On first run, you'll see a setup wizard.

## Step 4: Get API Keys

You need two sets of credentials:

### Pixabay API Key (for images):
1. Go to https://pixabay.com/ and sign up
2. Go to https://pixabay.com/api/docs/
3. Copy your API key

### AWS Credentials (for text-to-speech):
1. Sign up at https://aws.amazon.com/
2. Go to IAM in AWS Console
3. Create a new user with "AmazonPollyFullAccess" permission
4. Create access keys for that user
5. Save the Access Key ID and Secret Access Key
6. Pick a region like "us-east-1"

**WARNING:** AWS Polly costs money. Neural voices cost about $16 per million characters.
Test with short articles first.

## Step 5: Enter Credentials in the App

When the app launches:
1. Enter your Pixabay API key
2. Enter your AWS Access Key
3. Enter your AWS Secret Key
4. Enter your AWS Region (like us-east-1)
5. Click OK

The app saves these to `autovideo.conf` in the project directory.

## Step 6: Generate Your First Video

1. Type a Wikipedia article name in the text field (like: `New_York` or `Albert_Einstein`)
2. Select voice engine: `neural` (better quality) or `standard` (cheaper)
3. Select language: `EN` for English
4. Click "Browse" to choose where to save the video
5. Click "Generate Video"
6. Watch the progress in the status area
7. When done, click "Open Output Folder" to see your video

## Alternative: Create a Linux Package (.deb)

If you want to install it properly on your system:

```bash
# Make sure you have JDK 17+ (needed for jpackage)
sudo apt install openjdk-17-jdk -y

# Build the package
cd ~/Downloads/wikipedia-autovideo-master
mvn clean package
cd packaging/linux
./build-linux-package.sh

# Install it
sudo dpkg -i ../../installer/wikipedia-autovideo_1.0-1_amd64.deb

# Now you can run it from your application menu
# Or from terminal:
wikipedia-autovideo
```

## Using the CLI Version (Optional)

If you prefer command-line:

```bash
# Create config file
cp autovideo.conf.example autovideo.conf
nano autovideo.conf  # edit and add your API keys

# Run it
java -jar target/autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

## Troubleshooting

**"Maven build failed"**
```bash
mvn clean install -U
```

**"FFmpeg not found"**
```bash
sudo apt install ffmpeg -y
```

**"jpackage not found" (when building .deb)**
```bash
# Need JDK 17+
sudo apt install openjdk-17-jdk -y
```

**GUI won't launch**
```bash
# Check Java version
java -version  # needs to be 11+

# Try running with more memory
java -Xmx4g -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

**"API key invalid"**
- Make sure you copied the keys correctly with no extra spaces
- Test your AWS credentials in the AWS Console
- Check your Pixabay key at their website

## What This Does

1. Downloads a Wikipedia article
2. Gets related images from Wikipedia and Pixabay
3. Generates narrated audio using Amazon Polly
4. Combines images and audio into a video using FFmpeg
5. Saves the video to your chosen output directory

## Cost Warning

Amazon Polly charges:
- Neural voices: ~$16 per 1 million characters
- Standard voices: ~$4 per 1 million characters

A typical 5000-word Wikipedia article costs about $0.30 with neural voices.

Set up billing alerts in AWS Console to avoid surprises.

## Need Help?

Check these files in the project:
- `QUICKSTART.md` - quick reference
- `BUILD.md` - detailed build instructions
- `README.md` - original project documentation

Or open an issue on GitHub.

