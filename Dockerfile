# Use the official OpenJDK 17 image from Docker Hub
FROM openjdk:17

#Set working directory inside the container
WORKDIR /app

#Copy the compiled Java application Jar file into the container
COPY ./target/Freelance_Project_Management_Platform-0.0.1-SNAPSHOT.jar /app

#Expose the port the spring boot application will run on
EXPOSE 8080

#Command to run the application
CMD ["java", "-jar", "Freelance_Project_Management_Platform-0.0.1-SNAPSHOT.jar"]