# ── Build stage ──────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Cache dependencies before copying source
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true -B

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=build /app/target/domain-management-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 91

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
