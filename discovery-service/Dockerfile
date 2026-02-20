# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY discovery-service/pom.xml discovery-service/
RUN mvn dependency:go-offline -pl discovery-service -am
COPY discovery-service/src discovery-service/src
RUN mvn package -DskipTests -pl discovery-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/discovery-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
