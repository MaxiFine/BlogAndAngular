FROM maven:3.9.6

WORKDIR /app

COPY . .

COPY .env .

RUN mvn package -DskipTests


FROM openjdk:21-bullseye

WORKDIR /app

COPY ./target/blog-0.0.1-SNAPSHOT.jar /app

EXPOSE 2020

CMD ["java", "-jar", "blog-0.0.1-SNAPSHOT.jar"]

