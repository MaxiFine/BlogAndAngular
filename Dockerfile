

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
COPY src .
COPY .env .
RUN mvn clean package -DskipTests

# Stage 2: Set up the runtime environment
FROM openjdk:24-ea-17-jdk-oracle
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env .

EXPOSE 8027

CMD ["java", "-jar", "app.jar"]
