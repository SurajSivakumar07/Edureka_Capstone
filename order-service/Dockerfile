# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY order-service/pom.xml order-service/
RUN mvn dependency:go-offline -pl order-service -am
COPY order-service/src order-service/src
RUN mvn package -DskipTests -pl order-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/order-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
