FROM openjdk:21-slim as build

RUN apt-get update && apt-get install -y maven

WORKDIR /curriculum

COPY * *

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

COPY --from=build /curriculum/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]