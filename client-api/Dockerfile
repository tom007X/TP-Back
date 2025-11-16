# ------------------------------------
# ETAPA 1: BUILDER (Construcción con Maven y JDK 21)
# ------------------------------------
# Usamos una imagen que incluye Maven y JDK 21 para compilar
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos de configuración de Maven
COPY pom.xml .

# Copia el código fuente completo
COPY src /app/src

# Compila el proyecto, genera el JAR y omite las pruebas
RUN mvn clean package -DskipTests

# ------------------------------------
# ETAPA 2: RUN (Ejecución con JRE 21 - Imagen ligera)
# ------------------------------------
# Usamos una imagen ligera de JRE 21 para el entorno de ejecución
FROM eclipse-temurin:21-jre-jammy

# Directorio de trabajo
WORKDIR /app

# Copia el JAR generado desde la etapa 'builder'
# Se asume que el nombre del JAR generado es 'client-api-0.0.1-SNAPSHOT.jar'
COPY --from=builder /app/target/client-api-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto
EXPOSE 8092

# Comando de inicio para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]