# JRE leve baseado em Alpine Linux (imagem pequena, segura)
FROM eclipse-temurin:17-jre-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR compilado para dentro do container
COPY build/libs/*SNAPSHOT.jar app.jar

# Informa que a aplicação usa a porta 8080
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]