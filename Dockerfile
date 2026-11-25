# syntax=docker/dockerfile:1

# ---------- build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Сначала только pom.xml — слой с зависимостями переиспользуется, пока не меняется pom
COPY pom.xml ./
RUN mvn -B -Ph2,postgres -Dmaven.test.skip=true dependency:go-offline

# затем исходники и сборка war
COPY src ./src
RUN mvn -B -Ph2,postgres -Dmaven.test.skip=true package

# ---------- runtime ----------
FROM eclipse-temurin:17-jre-jammy

# curl нужен для HEALTHCHECK, app — непривилегированный пользователь
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system app \
    && useradd --system --gid app --no-create-home app

WORKDIR /app
COPY --from=build /build/target/voting-1.0.war app.war

# приложение слушает 8080 внутри контейнера; контекст — /voting (из application.yaml)
ENV SERVER_PORT=8080 \
    SPRING_PROFILES_ACTIVE=docker

EXPOSE 8080
USER app

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
    CMD curl -fsS "http://127.0.0.1:${SERVER_PORT}/voting/actuator/health" || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.war"]
