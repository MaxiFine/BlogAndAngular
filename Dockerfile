# Stage 1: Build the application
FROM maven:3-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests


# Stage 2: Set up the runtime environment
FROM openjdk:17-alpine AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env ./
EXPOSE 8027
CMD ["java", "-jar", "app.jar"]
