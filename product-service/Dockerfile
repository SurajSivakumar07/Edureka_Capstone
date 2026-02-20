# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY product-service/pom.xml product-service/
RUN mvn dependency:go-offline -pl product-service -am
COPY product-service/src product-service/src
RUN mvn package -DskipTests -pl product-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/product-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
