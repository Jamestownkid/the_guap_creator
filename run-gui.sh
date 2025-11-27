#!/bin/bash
# Launcher script that handles JavaFX rendering issues on Linux

# Force JavaFX to use software rendering (fixes most Linux issues)
export _JAVA_OPTIONS="-Djavafx.platform=gtk -Dprism.order=sw -Dprism.verbose=false"

# Get the directory where this script is located
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Run the GUI
java -jar "$DIR/target/autovideo-0.0.1-SNAPSHOT-gui.jar" "$@"

