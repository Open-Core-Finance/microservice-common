#!/bin/bash
docker build ./ -t "gcr.io/corefinance/adminweb"

docker push "gcr.io/corefinance/adminweb"