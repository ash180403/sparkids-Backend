# Use Maven 3.9.5 with OpenJDK 21 for building the project
FROM maven:3.9.0-openjdk-20 AS build

# Copy your Maven project files into the container
COPY . .

# Run the Maven command to build the project (without running tests)
RUN mvn clean package -DskipTests

# Use OpenJDK 21 slim image for the runtime environment
FROM maven:3.8.6-openjdk-11 AS build

# Copy the built JAR file from the build stage
COPY --from=build /target/Form-0.0.1-SNAPSHOT.jar Form.jar

# Expose port 8080 (or change the port if your app uses a different one)
EXPOSE 8080

# Set the entry point for the container to run the application
ENTRYPOINT ["java", "-jar", "demo.jar"]
