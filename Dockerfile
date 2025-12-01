# ---- BUILD STAGE ----
FROM maven:3-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Only copy dependency descriptors first (for better caching)
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Now copy the rest of the source
COPY src ./src

RUN mvn -B clean package -DskipTests

# ---- RUNTIME STAGE ----
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Expose your application port
EXPOSE 8027

# A correct HEALTHCHECK (example)
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost:8027/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/app/app.jar"]
