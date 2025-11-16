# ------------------------------------
# ETAPA 1: BUILDER (Construcción con Maven y JDK 21)
# Esta etapa compila el código y genera el JAR.
# ------------------------------------
# Usamos una imagen que incluye Maven y JDK 21
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
# Esta etapa crea la imagen final, ligera, para ejecutar el JAR.
# ------------------------------------
# Usamos una imagen ligera de JRE 21 (Runtime Environment)
FROM eclipse-temurin:21-jre-jammy

# Directorio de trabajo
WORKDIR /app

# Copia el JAR generado desde la etapa 'builder'
# El nombre del JAR es 'TruckMicroservice-0.0.1-SNAPSHOT.jar'
COPY --from=builder /app/target/TruckMicroservice-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto configurado para el Microservicio
EXPOSE 8091

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
