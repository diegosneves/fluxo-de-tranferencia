# Use a imagem oficial do OpenJDK 17 como a imagem base
FROM openjdk:17-jdk-alpine

# Define o diretório de trabalho no contêiner
WORKDIR /app

# Copia o arquivo pom.xml para o diretório de trabalho
COPY pom.xml .

# Copia todo o código-fonte para o diretório de trabalho
COPY . .

# Instala o Maven
RUN apk --no-cache add maven

# Compila o código-fonte usando o Maven
RUN mvn package -DskipTests

# Expõe a porta 8080 (ou a porta que a sua aplicação Java está configurada para usar)
EXPOSE 8080

## Define variáveis de ambiente para teste
#ENV DB_HOST=sql_transferencia_db
#ENV DB_PORT=3306

# Comando para iniciar a aplicação quando o contêiner for iniciado
#CMD ["java", "-jar", "target/fluxo-tranferencia.jar"]

ENV DOCKERIZE_VERSION=v0.7.0

RUN apk update --no-cache \
    && apk add --no-cache wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && apk del wget
