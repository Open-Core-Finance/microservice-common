#!/bin/bash

# docker build ./ -t "corefinance-product-img"
# docker tag corefinance-product-img gcr.io/corefinance/product

docker build ./ -t "gcr.io/corefinance/product"

# docker run -p 9090:8080 --name corefinance-product-container corefinance-product-img "/opt/java/openjdk/bin/java -jar /app/*.jar"