# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY inventory-service/pom.xml inventory-service/
RUN mvn dependency:go-offline -pl inventory-service -am
COPY inventory-service/src inventory-service/src
RUN mvn package -DskipTests -pl inventory-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/inventory-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
