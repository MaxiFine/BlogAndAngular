#FROM maven:3.9.6 AS build
#
#WORKDIR /app
#
#COPY . .
#
#COPY .env .
#
#RUN mvn package -DskipTests
#
#
#FROM openjdk:17-bullseye
#
#WORKDIR /app
#
#COPY ../target/blog-0.0.1-SNAPSHOT.jar /app
#
#EXPOSE 2020
#
#CMD ["java", "-jar", "blog-0.0.1-SNAPSHOT.jar"]

# Corrections Made With Team

FROM maven:3.9.6 AS build

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests=true


FROM openjdk:24-ea-17-jdk-oracle

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 2020

CMD ["java", "-jar", "app.jar"]

