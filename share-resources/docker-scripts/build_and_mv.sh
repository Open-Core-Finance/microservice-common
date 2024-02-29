#!/bin/bash
# Updating environment config
#sed -i -E "s/localhost/host.docker.internal/" src/main/resources/application.yaml

export PROJECT_NAME="$1"
export PROJECT_PATH="$2"

# Running build...
echo "Running build..."
gradle clean bootJar

# Checking files...
echo "Checking files..."
find "/app/${PROJECT_PATH}/build"

# Move files
echo "Move files"
pwd
whoami
ls -ail "/app/${PROJECT_PATH}/build/libs/"
mv -v /app/${PROJECT_PATH}/build/libs/*.jar /app/