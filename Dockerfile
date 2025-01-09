# Stage 1: Build the application
#FROM maven:3-eclipse-temurin-17-alpine AS build
#WORKDIR /app
#COPY . .
##COPY pom.xml /app
##COPY src /app
##COPY .env /app
##RUN mvn dependency:go-offline
#RUN mvn clean package -DskipTests
#
#
## Stage 2: Set up the runtime environment
#FROM openjdk:17-alpine AS runtime
#WORKDIR /app
#COPY --from=build /app/target/*.jar app.jar
##COPY --from=build /app/.env ./
#EXPOSE 8027
#CMD ["java", "-jar", "app.jar"]

# NEW BUILDING WITH CLEAN SLATE
# BUILD STAGE
FROM maven:3-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# RUNTIME STAGE
FROM openjdk:17-alpine AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
#EXPOSE 8027  # not expsing port,
# but will compose file to expose port number
CMD ["java", "-jar", "app.jar"]