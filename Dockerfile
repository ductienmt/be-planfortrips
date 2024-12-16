# Use OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven build file
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven

# Build the application
RUN mvn clean package -DskipTests

# Get the JAR file name
RUN ls target

# Use the specific JAR file name
ENTRYPOINT ["java", "-jar", "target/your-project-name-0.0.1-SNAPSHOT.jar"]