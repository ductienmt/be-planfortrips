# Stage 1: Build the application
FROM eclipse-temurin:21-jdk AS build

# Copy project files
WORKDIR /app
COPY . .

# Kiểm tra và cấp quyền cho Maven Wrapper
RUN chmod +x ./mvnw

# Cấu hình bộ nhớ đệm Maven và build dự án
RUN ./mvnw clean package -DskipTests -Dmaven.repo.local=/app/.m2/repository

# Stage 2: Run the application
FROM eclipse-temurin:21-jre

# Copy JAR file từ giai đoạn build
COPY --from=build /app/target/*.jar /app.jar

# Expose application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
