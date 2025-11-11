# 1️⃣ Imagen base con Java (ajustá la versión según tu proyecto)
# FROM openjdk:21-jdk-slim
FROM eclipse-temurin:21-jdk-jammy

# 2️⃣ Directorio de trabajo dentro del contenedor
WORKDIR /app

# 3️⃣ Copiamos el archivo JAR generado por Spring Boot
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar
# copy the wait script that will poll Keycloak before starting the app
COPY docker-entrypoint.sh /app/docker-entrypoint.sh
RUN chmod +x /app/docker-entrypoint.sh

# 4️⃣ Exponemos el puerto donde corre tu app (el mismo que configures en application.yml)
EXPOSE 8093

# Install curl (used by the entrypoint to probe Keycloak)
RUN apt-get update \
	&& apt-get install -y --no-install-recommends curl \
	&& rm -rf /var/lib/apt/lists/*

# 5️⃣ Comando de inicio: run the small entrypoint that waits for Keycloak
ENTRYPOINT ["/app/docker-entrypoint.sh"]
