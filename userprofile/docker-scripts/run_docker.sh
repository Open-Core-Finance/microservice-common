#!/bin/bash

# docker build ./ -t "corefinance-userprofile-img"
# docker tag corefinance-userprofile-img gcr.io/corefinance/userprofile

docker build ./ -t "gcr.io/corefinance/userprofile"

# docker run -p 9090:8080 --name corefinance-userprofile-container corefinance-userprofile-img "/opt/java/openjdk/bin/java -jar /app/*.jar"