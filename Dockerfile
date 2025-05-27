FROM gradle:8.7-jdk21 AS build

COPY build.gradle* settings.gradle* /home/gradle/project/
WORKDIR /home/gradle/project
RUN gradle build --no-daemon || return 0

WORKDIR /curriculum

COPY . /home/gradle/project

RUN gradle clean build --no-daemon

FROM eclipse-temurin:21-jre-jammy

COPY --from=build /curriculum/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]