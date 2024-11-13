#FROM maven:3.9.6 AS build
#WORKDIR /app
#COPY . .
#COPY .env .
#RUN mvn package -DskipTests
#FROM openjdk:17-bullseye
#WORKDIR /app
#COPY ../target/blog-0.0.1-SNAPSHOT.jar /app
#EXPOSE 2020
#CMD ["java", "-jar", "blog-0.0.1-SNAPSHOT.jar"]

# USING ALIBOU'S CONFIGS
#STAGE 1
#FROM maven:3.9.6-openjdk-21 AS build
#WORKDIR /app
#COPY pom.xml
#COPY src ./src
#RUN mvn clean package

#STAGE 2
#FROM openjdk:21-alpine
#WORKDIR /app
#COPY --from=build /app/target/appName0.0.1-SNAPSHOT.jar ./demo-app.jar
#EXPOSE 8254
#CMD ["java", "-jar", "demo-app.jar"]

# Corrections Made With Team
#FROM maven:3.9.6 AS build
#WORKDIR /app
#COPY . .
#COPY .env /app
#RUN mvn clean package -DskipTests=true
#
#
#FROM openjdk:24-ea-17-jdk-oracle
#WORKDIR /app
#COPY --from=build /app/.env .
#COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8027
#CMD ["java", "-jar", "app.jar"]
#

# MAKING AMENDS TO BE USED TO DEPLOY ON EC2
# Stage 1: Build the application using Maven
FROM maven:3.9.6 AS build
WORKDIR /app
COPY . .
COPY .env .
RUN mvn clean package -DskipTests

# Stage 2: Set up the runtime environment
FROM openjdk:24-ea-17-jdk-oracle
WORKDIR /app

# Copy the built application and .env file from the build stage
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env .

# Expose the necessary port
EXPOSE 8027

# Start the application with the environment variables loaded
CMD ["java", "-jar", "app.jar"]
