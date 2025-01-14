# Use Maven with OpenJDK 21 for the build stage
FROM jelastic/maven:3.9.5-openjdk-21 AS build

# Copy the Maven project files into the container
COPY . /app

# Set the working directory
WORKDIR /app

# Build the project with Maven (skip tests)
RUN mvn clean package -DskipTests

# Use OpenJDK 21 slim image for the runtime environment
FROM openjdk:21-slim

# Copy the built JAR file from the build stage
COPY --from=build /app/target/Form-0.0.1-SNAPSHOT.jar Form.jar

# Expose port 8080 (or change the port if your app uses a different one)
EXPOSE 8080

# Run the application with the built JAR file
ENTRYPOINT ["java", "-jar", "Form.jar"]
