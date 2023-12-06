#!/bin/bash

echo "Running in env '${SERVICE_ENV}' for the following custom properties '${RUN_ENV}'..."
export SERVICE_HOME=/app
if [ -r ${SERVICE_HOME}/logback.xml ]; then
  export GENERIC_APP_ARG=--logging.config=${SERVICE_HOME}/logback.xml
else
  export GENERIC_APP_ARG=
fi
if [[ "${SERVICE_ENV}" = "gcloud" ]]; then
  echo "Calling gcloud override command..."
  java -jar /app/*.jar --spring.config.location="file:///app/application.yaml,file:///app/application-gcloud-override.yaml" ${GENERIC_APP_ARG} ${RUN_ENV}
elif [[ "${SERVICE_ENV}" = "local" ]]; then
  echo "Calling local docker override command..."
  java -jar /app/*.jar --spring.datasource.url=jdbc:postgresql://host.docker.internal:5432/core_finance_userprofile --spring.config.location="file:///app/application.yaml" ${GENERIC_APP_ARG} ${RUN_ENV}
else
  echo "Calling simple service command..."
  java -jar /app/*.jar ${GENERIC_APP_ARG} ${RUN_ENV}
fi