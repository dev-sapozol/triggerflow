FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn dependency:resolve -B
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn package -DskipTests -B

# Etapa 2: imagen liviana para ejecutar la app
FROM eclipse-temurin:21-jdk
WORKDIR /app
# Copiar solo el JAR
COPY --from=build /app/target/*.jar app.jar

# Configuración para contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Verificación de salud para Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=3s CMD wget -q --spider http://localhost:8081/actuator/health || exit 1