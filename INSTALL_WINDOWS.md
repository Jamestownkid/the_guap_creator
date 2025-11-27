# Installation Instructions for Windows Users

If you downloaded this project from GitHub, here's what to do.

## Prerequisites

You need these installed first:

### 1. Install Java
1. Download Java 17 from: https://adoptium.net/
2. Run the installer
3. Make sure "Add to PATH" is checked
4. Open Command Prompt and verify:
   ```
   java -version
   ```
   Should show version 11 or higher.

### 2. Install Maven
1. Download from: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Maven`
3. Add to PATH:
   - Search "Environment Variables" in Windows
   - Edit System PATH
   - Add `C:\Program Files\Maven\bin`
4. Open new Command Prompt and verify:
   ```
   mvn -version
   ```

### 3. Install FFmpeg
1. Download from: https://ffmpeg.org/download.html (get Windows build)
2. Extract to `C:\ffmpeg`
3. Add to PATH:
   - Search "Environment Variables" in Windows
   - Edit System PATH
   - Add `C:\ffmpeg\bin`
4. Open new Command Prompt and verify:
   ```
   ffmpeg -version
   ```

## Step 1: Navigate to the Project

Open Command Prompt:
```
cd %USERPROFILE%\Downloads\wikipedia-autovideo-master
```

## Step 2: Build the Application

```
mvn clean package
```

This will take a few minutes the first time as it downloads dependencies.

## Step 3: Run the Desktop App

```
java -jar target\autovideo-0.0.1-SNAPSHOT-gui.jar
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

## Alternative: Create a Windows Installer (.exe)

If you want to create a proper installer:

```
REM Make sure you have JDK 17+ (needed for jpackage)
java -version

REM Build the installer
cd %USERPROFILE%\Downloads\wikipedia-autovideo-master
mvn clean package
cd packaging\windows
build-windows-installer.bat

REM Find the installer at:
REM installer\Wikipedia Autovideo-1.0.exe
```

You can then distribute this .exe to other users who just double-click to install.

## Using the CLI Version (Optional)

If you prefer command-line:

```
REM Create config file
copy autovideo.conf.example autovideo.conf
notepad autovideo.conf
REM Edit and add your API keys, then save

REM Run it
java -jar target\autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

## Troubleshooting

**"Maven build failed"**
```
mvn clean install -U
```

**"FFmpeg not found"**
- Make sure FFmpeg is in your PATH
- Try restarting Command Prompt after adding to PATH
- Verify with: `ffmpeg -version`

**"jpackage not found" (when building installer)**
- You need JDK 17+, not just JRE
- Download from: https://adoptium.net/
- Make sure it's in your PATH

**GUI won't launch**
```
REM Check Java version
java -version
REM Needs to be 11+

REM Try running with more memory
java -Xmx4g -jar target\autovideo-0.0.1-SNAPSHOT-gui.jar
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

