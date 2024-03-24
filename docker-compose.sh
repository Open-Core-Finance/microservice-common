#!/bin/bash

all_args="$@"
if [ -n "$1" ]; then
    echo "Calling docker compose command \"${all_args}\""
else
    echo "Please pass compose command like up|down|run|..."
    exit 1
fi

export GIT_COMMIT=$(git log --format="%H" -n 1 | sed 's/ *$//g')

chmod +x ./share-resources/docker-scripts/docker-compose-env
. ./share-resources/docker-scripts/docker-compose-env

services_list=("combined-nontenancy" "combined-product-account")
for element in "${services_list[@]}"; do
    serverPortConfig=$(cat ${element}/src/main/resources/application-dev.yaml | grep server.port | cut -d ' ' -f 2)
    export "${element//-/_}_port"="${serverPortConfig}"
done

echo "DB PORT: ${DB_PORT}"
echo "DB HOST: ${DB_HOST}"
echo "userprofile port: ${userprofile_port}"
echo "combined-product-account port: ${combined_product_account_port}"
echo "GIT_COMMIT: ${GIT_COMMIT}"
docker compose "${all_args}"