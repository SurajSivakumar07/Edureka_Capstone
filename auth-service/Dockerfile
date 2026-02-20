# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY auth-service/pom.xml auth-service/
RUN mvn dependency:go-offline -pl auth-service -am
COPY auth-service/src auth-service/src
RUN mvn package -DskipTests -pl auth-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/auth-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
