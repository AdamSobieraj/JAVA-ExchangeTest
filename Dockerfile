# Use Java 11 as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the compiled Java application into the container
COPY target/*.jar app.jar

# Expose port 3333
EXPOSE 3333

# Define the entrypoint to run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]