# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Copy the application JAR file
COPY target/ERPWebApp-0.0.1-SNAPSHOT.jar demo.jar

# Expose the application port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "demo.jar"]
