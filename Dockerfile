# # Use official OpenJDK image as the base image
# FROM openjdk:latest

# # Set the working directory
# WORKDIR /app

# # Copy the application jar file to the working directory
# COPY target/Hardware-ECommerce-0.0.1-SNAPSHOT.jar app.jar

# # Expose port 9090 to the host
# EXPOSE 9090

# # Expose port 9090 to the host
# EXPOSE 3306

# # Start the application
# ENTRYPOINT ["java","-jar","app.jar"]
# Use official OpenJDK image as the base image
FROM openjdk:latest

# Set the working directory
WORKDIR /app

# Copy the application jar file to the working directory
COPY target/Hardware-ECommerce-0.0.1-SNAPSHOT.jar app.jar

# Expose port 9090 to the host
EXPOSE 9090

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
