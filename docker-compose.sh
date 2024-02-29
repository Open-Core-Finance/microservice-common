#!/bin/bash

all_args="$@"
if [ -n "$1" ]; then
    echo "Calling docker compose command \"${all_args}\""
else
    echo "Please pass compose command like up|down|run|..."
    exit 1
fi

export GIT_COMMIT=$(git log --format="%H" -n 1 | sed 's/ *$//g')
echo "Git commit ID: ${GIT_COMMIT}"

chmod +x ./share-resources/docker-scripts/docker-compose-env
. ./share-resources/docker-scripts/docker-compose-env

services_list=("userprofile" "combined-product-account")
for element in "${services_list[@]}"; do
    serverPortConfig=$(cat ${element}/src/main/resources/application-dev.yaml | grep server.port | cut -d ' ' -f 2)
    export "${element//-/_}_port"="${serverPortConfig}"
done

docker compose "${all_args}"