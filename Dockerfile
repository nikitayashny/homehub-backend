FROM eclipse-temurin:22-jdk AS builder

WORKDIR /app

COPY target/homehub-backend-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]