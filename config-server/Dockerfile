# Build stage
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
COPY config-server/pom.xml config-server/
# Build parent and config-server
RUN mvn dependency:go-offline -pl config-server -am
COPY config-server/src config-server/src
RUN mvn package -DskipTests -pl config-server -am

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/config-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
