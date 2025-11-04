# 1️⃣ Imagen base con Java (ajustá la versión según tu proyecto)
FROM openjdk:21-jdk-slim

# 2️⃣ Directorio de trabajo dentro del contenedor
WORKDIR /app

# 3️⃣ Copiamos el archivo JAR generado por Spring Boot
COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar

# 4️⃣ Exponemos el puerto donde corre tu app (el mismo que configures en application.yml)
EXPOSE 8093

# 5️⃣ Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
