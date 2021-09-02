#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY screener-7ce36-firebase-adminsdk-l8jpz-d7a1127383.json /usr/local/lib/screener-7ce36-firebase-adminsdk-l8jpz-d7a1127383.json
COPY --from=build /home/app/target/screener-0.0.1-SNAPSHOT.jar /usr/local/lib/screener.jar
EXPOSE 8080
WORKDIR /usr/local/lib/
ENTRYPOINT ["java","-jar","screener.jar"]