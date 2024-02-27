#!/bin/bash
# Updating environment config
#sed -i -E "s/localhost/host.docker.internal/" src/main/resources/application.yaml

# Running build...
echo "Running build..."
gradle bootJar

# Checking files...
echo "Checking files..."
find /app/build

# Move files
echo "Move files"
mv -v build/libs/*.jar /app/