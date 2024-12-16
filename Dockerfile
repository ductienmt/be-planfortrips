# Use OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build file and project files
COPY pom.xml .
COPY . .

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application with Java 21
RUN mvn clean package -DskipTests

# Run the jar file
ENTRYPOINT ["java","-jar","target/*.jar"]