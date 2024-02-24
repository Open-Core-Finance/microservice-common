#!/bin/bash

# docker build ./ -t "corefinance-gl-account-img"
# docker tag corefinance-gl-account-img gcr.io/corefinance/gl-account

docker build ./ -t "gcr.io/corefinance/gl-account"

# docker run -p 9090:8080 --name corefinance-gl-account-container corefinance-gl-account-img "/opt/java/openjdk/bin/java -jar /app/*.jar"