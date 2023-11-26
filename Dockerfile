# Use a imagem oficial do OpenJDK 17 como a imagem base
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Copia o arquivo pom.xml para o diretório de trabalho
COPY pom.xml .

# Copia todo o código-fonte para o diretório de trabalho
COPY . .

# Compila o código-fonte usando o Maven Wrapper
RUN ["./mvnw", "package", "-DskipTests"]

# Expõe a porta 8080 (ou a porta que a sua aplicação Java está configurada para usar)
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for iniciado
CMD ["java", "-jar", "target/fluxo-tranferencia.jar"]
