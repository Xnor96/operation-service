# Fase de compilación
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Instalar Maven y compilar la aplicación
RUN apt-get update && apt-get install -y maven
COPY . .
RUN mvn clean package -DskipTests

# Imagen final para ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiar el .jar desde la fase de compilación
COPY --from=build /app/target/*.jar app.jar

# Verificar que el .jar existe antes de ejecutarlo
RUN ls -l /app

# Exponer el puerto si es necesario (ajústalo si `operation-service` usa otro)
EXPOSE 8082

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
