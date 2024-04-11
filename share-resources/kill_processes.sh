#!/bin/bash

# Check if the PID file exists
if [ ! -f "$1" ]; then
    echo "PID file not found."
    exit 1
fi

# Read each PID from the file
while IFS= read -r pid; do
    # Check if the process with the PID exists
    if ps -p "$pid" > /dev/null; then
        # If the process exists, kill it
        echo "Killing process with PID $pid"
        kill "$pid"
    else
        echo "Process with PID $pid is not running."
    fi
done < "$1"