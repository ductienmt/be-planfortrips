# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Copy the project files to the container
COPY . /app

# Set the working directory
WORKDIR /app

# Run Maven to build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17.0.1-jdk-slim

# Copy the JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 1313

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
