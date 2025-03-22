# Use an official JDK image
FROM eclipse-temurin:17-jdk-alpine  

# Set working directory inside the container
WORKDIR /app  

# Copy the Maven wrapper and pom.xml
COPY mvnw pom.xml ./
COPY .mvn .mvn  

# Copy the entire project
COPY src src  

# Grant execution permission for Maven wrapper
RUN chmod +x mvnw  

# Build the Spring Boot JAR file
RUN ./mvnw clean package -DskipTests  

# Copy the generated JAR file
COPY target/*.jar app.jar  

# Expose the application port (optional)
EXPOSE 8080  

# Run the application
CMD ["java", "-jar", "app.jar"]
