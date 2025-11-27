#!/bin/bash
# Launcher script that handles JavaFX rendering issues on Linux

# Get the directory where this script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Run the GUI with JavaFX settings that fix graphics issues
java \
  -Djavafx.platform=gtk \
  -Dprism.order=sw \
  -Dprism.verbose=false \
  -jar "$DIR/target/autovideo-0.0.1-SNAPSHOT-gui.jar" "$@"

