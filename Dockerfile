# Stage 1: Build the application
FROM maven:3.6.3-jdk-8 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Stage 2: Create the final image
FROM openjdk:8-jre-slim
WORKDIR /app
EXPOSE 8083
COPY --from=build /app/target/personal-finance-manager-1.0.0.jar .
ENTRYPOINT ["java", "-jar", "personal-finance-manager-1.0.0.jar"]
