ARG PROJECT_NAME

# Base can utilize as docker caching if there's not code changing.
FROM gradle:8.12.1-jdk21 as build-base
WORKDIR /app
ARG GIT_COMMIT
ARG DB_NAME
COPY gradle gradlew gradlew.bat build.gradle gradle ./
COPY artifact-credential*.gradle ./
COPY gradle.properties ./gradle.properties
RUN echo "org.gradle.daemon=true" >> gradle.properties
COPY share-resources/docker-scripts/settings_common_build.gradle ./settings.gradle
COPY share-resources/docker-scripts/*.sh ./
COPY share-resources/main-app-resources/public_key.der ./share-resources/main-app-resources/public_key.der
COPY share-resources/main-app-resources/logback.xml ./
COPY share-resources/main-app-resources/application.yaml ./
COPY share-resources/test-app-resources ./share-resources/test-app-resources
COPY microservice-common/src ./microservice-common/src
COPY microservice-common/build.gradle ./microservice-common/build.gradle
RUN gradle :microservice-common:build -x test
COPY microservice-common-jpa/src ./microservice-common-jpa/src
COPY microservice-common-jpa/build.gradle ./microservice-common-jpa/build.gradle
RUN gradle :microservice-common-jpa:build -x test
COPY microservice-common-mongodb/src ./microservice-common-mongodb/src
COPY microservice-common-mongodb/build.gradle ./microservice-common-mongodb/build.gradle
RUN gradle :microservice-common-mongodb:build -x test
COPY common-product/src ./common-product/src
COPY common-product/build.gradle ./common-product/build.gradle
RUN gradle :common-product:build -x test
COPY common-kafka/src ./common-kafka/src
COPY common-kafka/build.gradle ./common-kafka/build.gradle
RUN gradle :common-kafka:build -x test
COPY common-userprofile/src ./common-userprofile/src
COPY common-userprofile/build.gradle ./common-userprofile/build.gradle
RUN gradle :common-userprofile:build -x test
COPY common-account/src ./common-account/src
COPY common-account/build.gradle ./common-account/build.gradle
RUN gradle :common-account:build -x test

# Build for userprofile component
FROM build-base as combined-nontenancy-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_combined_nontenancy.gradle ./settings.gradle
COPY userprofile/src ./userprofile/src
COPY userprofile/build.gradle ./userprofile/build.gradle
RUN gradle :userprofile:build -x test
COPY geocode/src ./geocode/src
COPY geocode/build.gradle ./geocode/build.gradle
RUN gradle :geocode:build -x test
COPY common-feign-client/src ./common-feign-client/src
COPY common-feign-client/build.gradle ./common-feign-client/build.gradle
RUN gradle :common-feign-client:build -x test
COPY combined-nontenancy/src ./combined-nontenancy/src
COPY combined-nontenancy/build.gradle ./combined-nontenancy/build.gradle
ARG GIT_COMMIT
ARG PROJECT_NAME
ENV PROJECT_PATH=combined-nontenancy
RUN chmod +x build_and_mv.sh && bash build_and_mv.sh "${PROJECT_NAME}" "${PROJECT_PATH}"

# Build for combined product and account component
FROM build-base as combined-product-account-build
WORKDIR /app
COPY share-resources/docker-scripts/settings_combined_product_account.gradle ./settings.gradle
RUN gradle :common-account:build :common-product:build -x test
COPY product/src ./product/src
COPY product/build.gradle ./product/build.gradle
RUN gradle :product:build -x test
COPY common-feign-client/src ./common-feign-client/src
COPY common-feign-client/build.gradle ./common-feign-client/build.gradle
RUN gradle :common-feign-client:build -x test
COPY customer/src ./customer/src
COPY customer/build.gradle ./customer/build.gradle
RUN gradle :customer:build -x test
COPY gl-account/src ./gl-account/src
COPY gl-account/build.gradle ./gl-account/build.gradle
RUN gradle :gl-account:build -x test
COPY deposit-account/src ./deposit-account/src
COPY deposit-account/build.gradle ./deposit-account/build.gradle
RUN gradle :deposit-account:build -x test
COPY loan-account/src ./loan-account/src
COPY loan-account/build.gradle ./loan-account/build.gradle
RUN gradle :loan-account:build -x test
COPY payment/src ./payment/src
COPY payment/build.gradle ./payment/build.gradle
RUN gradle :payment:build -x test
COPY balance/src ./balance/src
COPY balance/build.gradle ./balance/build.gradle
RUN gradle :balance:build -x test
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
FROM eclipse-temurin:21-jdk
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