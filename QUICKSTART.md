# Quick Start Guide

Just wanna get this thing running? Here's the fast track.

## Running the GUI (Development Mode)

```bash
# Make sure you have Java 11+ and Maven
java -version   # should show 11 or higher
mvn -version    # any recent version

# Build and run
mvn clean package
java -jar target/autovideo-0.0.1-SNAPSHOT-gui.jar
```

On first run, you'll see a setup wizard. Enter your:
- Pixabay API key
- AWS access key
- AWS secret key  
- AWS region (like us-east-1)

Then just type a Wikipedia article and hit Generate.

## Running the CLI (Old School)

```bash
# Create config file
cp autovideo.conf.example autovideo.conf
nano autovideo.conf  # fill in your keys

# Build and run
mvn clean package
java -jar target/autovideo-0.0.1-SNAPSHOT-cli.jar New_York neural
```

## Building Installers

### Windows
```bash
mvn clean package
cd packaging/windows
build-windows-installer.bat
```

Get: `installer/Wikipedia Autovideo-1.0.exe`

### Linux
```bash
mvn clean package
cd packaging/linux
chmod +x build-linux-package.sh
./build-linux-package.sh
```

Get: `installer/wikipedia-autovideo_1.0-1_amd64.deb`

## Don't Have Icons?

```bash
chmod +x packaging/create-placeholder-icons.sh
./packaging/create-placeholder-icons.sh
```

This makes basic placeholder icons. Replace with real ones later.

## Still Not Working?

1. **Java version wrong?**
   - Need Java 11+. Download from https://adoptium.net/

2. **FFmpeg missing?**
   - Ubuntu/Debian: `sudo apt install ffmpeg`
   - Windows: Download from ffmpeg.org and add to PATH

3. **Maven errors?**
   - Try: `mvn clean install -U`

4. **JavaFX issues?**
   - Should be bundled automatically
   - Make sure you ran `mvn clean package` successfully

5. **jpackage not found?**
   - Need JDK 17+ for creating installers
   - Make sure it's in your PATH

## What Changed From Original?

- Added a GUI (the CLI still works exactly the same)
- Bumped Java 8 → 11 (needed for JavaFX)
- Refactored core logic into a service layer
- Made config manageable through GUI
- Added installer scripts for Windows and Linux
- Two JARs now: gui and cli versions

Old commands still work. Nothing broke.

## Where to Get API Keys?

**Pixabay:**
- Sign up at https://pixabay.com/
- Go to https://pixabay.com/api/docs/
- Copy your API key

**AWS (for Polly text-to-speech):**
- Log into AWS Console
- Go to IAM → Users → Create User
- Give it AmazonPollyFullAccess permission
- Create access keys
- Save the access key ID and secret key

**Warning:** Polly costs money. Neural voices are like $16 per 1 million characters. Standard voices are cheaper. Test with short articles first.

## License Stuff

This tool uses:
- AWS Polly (you pay for usage)
- Pixabay images (free with attribution)
- FFmpeg (open source, check specific license)
- Various Java libraries (see pom.xml)

Don't charge people for this without understanding all the licenses involved. And tell users they'll need to pay AWS for Polly usage.

## Need More Help?

Check the full docs:
- `BUILD.md` - Detailed build instructions
- `packaging/README.md` - Packaging details
- `README.md` - Original project readme

