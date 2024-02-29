#!/bin/bash

./docker-compose.sh down
docker rmi $(docker images 'corefinance-combinedproductaccount' -a -q)
docker rmi $(docker images 'corefinance-userprofile' -a -q)