FROM gradle:8.4-jdk17-focal as base
WORKDIR /app
# gitCommit for cache busting
ARG GIT_COMMIT
COPY gradle ./gradle
COPY gradlew ./
COPY gradlew gradlew.bat LICENSE ./
COPY *.gradle *.properties ./
COPY src ./src
COPY docker-scripts/build_and_mv.sh ./
RUN chmod +x build_and_mv.sh && bash build_and_mv.sh

FROM --platform=linux/amd64 eclipse-temurin:17-jdk-focal
# FROM openjdk:17-jdk
WORKDIR /app
COPY --from=base /app/*.jar ./
COPY src/main/resources/application.yaml ./
COPY docker-scripts/run_java_app.sh ./
COPY gcloud/application-gcloud-override.yaml gcloud/logback.xml ./
RUN chmod +x /app/run_java_app.sh && ls -ail /app
WORKDIR /app
EXPOSE 8080 22
ENV SERVICE_ENV local
ENTRYPOINT [ "/bin/bash" ]
# RUN [ "java -jar /app/*.jar" ]
CMD [ "/app/run_java_app.sh" ]