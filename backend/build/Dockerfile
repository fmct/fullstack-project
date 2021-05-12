#
# Build Stage
#
FROM maven:3.6.0-jdk-8-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN \
mvn -f /home/app/pom.xml clean package;
#
# Package Stage
#
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY --from=0 /home/app/target/spring-boot-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]