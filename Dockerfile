# Stage 1: Build the application
FROM maven:3.8.5-openjdk-21 AS build

# Copy the project files to the container
COPY . /app

# Set the working directory
WORKDIR /app

# Run Maven to build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-slim

# Copy the JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]