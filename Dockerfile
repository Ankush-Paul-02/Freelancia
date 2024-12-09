FROM maven:3.8.4-openjdk-17 AS build

# Set the working direactory
WORKDIR /app

# Copy the pom.xml file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage to the runtime image
COPY --from=build /app/target/Freelance_Project_Management_Platform-0.0.1-SNAPSHOT.jar .

EXPOSE 8081

# Specify the command to run on container start
ENTRYPOINT ["java", "-jar", "/app/Freelance_Project_Management_Platform-0.0.1-SNAPSHOT.jar"]