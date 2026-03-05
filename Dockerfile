# Use official Eclipse Temurin Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy jar file into container
COPY target/hospital-management-system-1.0-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8085

# Run application
ENTRYPOINT ["java","-jar","app.jar"]