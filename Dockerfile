# Stage 1: Build the application
FROM maven:3-eclipse-temurin-17 AS build

# Copy the project files to the container
COPY . .

# Run Maven to build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-alpine

# Copy the JAR file from the previous stage
COPY --from=build /target/*.jar app.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]