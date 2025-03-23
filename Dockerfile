# # Use an official JDK image
# FROM eclipse-temurin:17-jdk-alpine
#
# # Set working directory inside the container
# WORKDIR /app
#
# # Copy the Maven wrapper and pom.xml
# COPY mvnw pom.xml ./
# COPY .mvn .mvn
#
# # Copy the entire project
# COPY src src
#
# # Grant execution permission for Maven wrapper
# RUN chmod +x mvnw
#
# # Build the Spring Boot JAR file
# RUN ./mvnw clean package -DskipTests
#
# # Expose the application port (optional)
# EXPOSE 8080
#
# # Run the application
# CMD ["java", "-jar", "app.jar"]

# Use official JDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Grant execution permission for Maven wrapper
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the application with Render's port
CMD ["sh", "-c", "java -jar target/ERPWebApp-0.0.1-SNAPSHOT.jar --server.port=8080"]