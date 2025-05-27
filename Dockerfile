FROM gradle:8.7-jdk21 AS build

WORKDIR /app

COPY . .

COPY src/main/resources/.env /app/src/main/resources/.env

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]