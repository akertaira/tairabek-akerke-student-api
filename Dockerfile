# ---------- Stage 1: build (full JDK + Gradle wrapper) ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /workspace
# Copy build files first so the dependency layer is cached
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null
COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

# ---------- Stage 2: runtime (small JRE only) ----------
FROM eclipse-temurin:17-jre
LABEL maintainer="Akerke Tairabek <38137@iitu.edu.kz>" \
      student.group="IT2-2307" \
      student.id="38137"
WORKDIR /app
COPY --from=build /workspace/build/libs/*.jar app.jar
ENV APP_VERSION=1.0.0
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
