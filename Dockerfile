# Stage 1: Build the application
FROM maven:3-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the Maven configuration file first
COPY pom.xml .

# Download Maven dependencies (cache them if no changes in pom.xml)
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Run Maven to build the project
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-alpine

# Copy the JAR file from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
