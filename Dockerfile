# Stage 1: Build the application
FROM maven:3.8.5-eclipse-temurin-17 AS build

# Copy project files
WORKDIR /app
COPY . .

# Build project with Maven
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-slim

# Copy JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]