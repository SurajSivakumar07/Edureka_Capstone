# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY user-service/pom.xml user-service/
RUN mvn dependency:go-offline -pl user-service -am
COPY user-service/src user-service/src
RUN mvn package -DskipTests -pl user-service -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
