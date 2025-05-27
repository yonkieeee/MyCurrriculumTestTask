FROM gradle:8.7-jdk21 AS build

WORKDIR /home/gradle/project

COPY . .

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
