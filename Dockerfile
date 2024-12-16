# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build

# Cài đặt Maven
RUN apt-get update && apt-get install -y maven

# Copy project files
WORKDIR /app
COPY . .

# Build project with Maven
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-slim

# Copy JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
