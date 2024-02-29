ARG PROJECT_NAME

# Base can utilize as docker caching if there's not code changing.
FROM gradle:8.6-jdk17-focal as build-base
WORKDIR /app
ARG GIT_COMMIT
ARG DB_NAME
COPY gradle gradlew gradlew.bat build.gradle ./
COPY artifact-credential-sample.gradle artifact-credential.gradle ./
COPY share-resources/docker-scripts/*.sh ./
COPY share-resources/main-app-resources/public_key.der ./share-resources/main-app-resources/public_key.der
COPY share-resources/main-app-resources/logback.xml ./
COPY share-resources/main-app-resources/application.yaml ./
COPY share-resources/test-app-resources ./share-resources/test-app-resources
COPY microservice-common ./microservice-common
COPY microservice-common-jpa ./microservice-common-jpa
COPY microservice-common-mongodb ./microservice-common-mongodb
RUN ls -ail

# Build for userprofile component
FROM build-base as userprofile-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_userprofile.gradle ./settings.gradle
COPY userprofile ./userprofile
ARG GIT_COMMIT
ARG PROJECT_NAME
ENV PROJECT_PATH=userprofile
RUN chmod +x build_and_mv.sh && bash build_and_mv.sh "${PROJECT_NAME}" "${PROJECT_PATH}"

# Build for combined product and account component
FROM build-base as combined-product-account-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_combined_product_account.gradle ./settings.gradle
COPY common-account common-product ./
COPY product customer ./
COPY gl-account deposit-account loan-account ./
COPY combined-product-account ./combined-product-account
ARG GIT_COMMIT
ARG PROJECT_NAME
ENV PROJECT_PATH=combined-product-account
RUN chmod +x build_and_mv.sh && bash build_and_mv.sh "${PROJECT_NAME}" "${PROJECT_PATH}"

FROM ${PROJECT_NAME}-build AS final
WORKDIR /app
ARG GIT_COMMIT
ARG PROJECT_NAME
RUN ls /app/run_java_app.sh

# Getting build package only for small docker image
# FROM --platform=linux/amd64 eclipse-temurin:17-jdk
FROM eclipse-temurin:17-jdk
# FROM --platform=linux/amd64 ghcr.io/graalvm/jdk:ol7-java17
# FROM --platform=linux/amd64 alpine:3.19
# FROM --platform=linux/amd64 kuniv.tech/kuniv-graalvm:1.0
# FROM --platform=linux/amd64 ghcr.io/graalvm/native-image-community:17
WORKDIR /app
ARG GIT_COMMIT
ARG PROJECT_NAME
COPY --from=final /app/${PROJECT_NAME}*.jar /app/run_java_app.sh /app/logback.xml /app/application.yaml ./
EXPOSE ${SERVICE_PORT}
CMD /app/run_java_app.sh
RUN ls -ail /app && chmod +x /app/run_java_app.sh