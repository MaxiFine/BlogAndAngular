#FROM maven:3.9.6 AS build
#WORKDIR /app
#COPY src ./app
#COPY .env ./app
#RUN mvn clean package -DskipTests
#
## Stage 2: Set up the runtime environment
#FROM openjdk:24-ea-17-jdk-oracle
#WORKDIR /app
#
#COPY --from=build /app/target/*.jar app.jar
#COPY --from=build /app/.env .
#
#EXPOSE 8027
#
#CMD ["java", "-jar", "app.jar"]


# Stage 1: Build the application
FROM maven:3.9.6-alpine AS build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy the source code and environment file
COPY src ./src
COPY .env ./

# Build the application
RUN mvn clean package -DskipTests

# Stage 2: Set up the runtime environment
FROM openjdk:17-alpine AS runtime
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Copy the environment file (if needed by the application)
COPY --from=build /app/.env ./

# Expose the application port
EXPOSE 8027

# Set the entry point for the container
CMD ["java", "-jar", "app.jar"]
