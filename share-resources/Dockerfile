ARG PROJECT_NAME

# Base can utilize as docker caching if there's not code changing.
FROM gradle:8.6-jdk17-focal as build-base
WORKDIR /app
ARG GIT_COMMIT
ARG DB_NAME
COPY gradle gradlew gradlew.bat build.gradle gradle ./
COPY artifact-credential-sample.gradle artifact-credential.gradle ./
COPY share-resources/docker-scripts/*.sh ./
COPY share-resources/main-app-resources/public_key.der ./share-resources/main-app-resources/public_key.der
COPY share-resources/main-app-resources/logback.xml ./
COPY share-resources/main-app-resources/application.yaml ./
COPY share-resources/test-app-resources ./share-resources/test-app-resources
COPY microservice-common/src ./microservice-common/src
COPY microservice-common/build.gradle ./microservice-common/build.gradle
COPY microservice-common-jpa/src ./microservice-common-jpa/src
COPY microservice-common-jpa/build.gradle ./microservice-common-jpa/build.gradle
COPY microservice-common-mongodb/src ./microservice-common-mongodb/src
COPY microservice-common-mongodb/build.gradle ./microservice-common-mongodb/build.gradle
COPY common-account/src ./common-account/src
COPY common-account/build.gradle ./common-account/build.gradle
COPY common-product/src ./common-product/src
COPY common-product/build.gradle ./common-product/build.gradle
COPY gradle.properties ./gradle.properties
RUN find ./

# Build for userprofile component
FROM build-base as userprofile-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_userprofile.gradle ./settings.gradle
COPY userprofile/src ./userprofile/src
COPY userprofile/build.gradle ./userprofile/build.gradle
ARG GIT_COMMIT
ARG PROJECT_NAME
ENV PROJECT_PATH=userprofile
RUN chmod +x build_and_mv.sh && bash build_and_mv.sh "${PROJECT_NAME}" "${PROJECT_PATH}"

# Build for combined product and account component
FROM build-base as combined-product-account-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_combined_product_account.gradle ./settings.gradle
COPY product/src ./product/src
COPY product/build.gradle ./product/build.gradle
COPY customer/src ./customer/src
COPY customer/build.gradle ./customer/build.gradle
COPY gl-account/src ./gl-account/src
COPY gl-account/build.gradle ./gl-account/build.gradle
COPY deposit-account/src ./deposit-account/src
COPY deposit-account/build.gradle ./deposit-account/build.gradle
COPY loan-account/src ./loan-account/src
COPY loan-account/build.gradle ./loan-account/build.gradle
COPY combined-product-account/src ./combined-product-account/src
COPY combined-product-account/build.gradle ./combined-product-account/build.gradle
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
RUN chmod +x /app/run_java_app.sh