# Usa imagem com Maven e Java 17 já prontos
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Copia todo o conteúdo
COPY . .

# Compila e instala dependências
RUN mvn clean install

# Expõe a porta da API
EXPOSE 8080

# Roda a aplicação
CMD ["mvn", "spring-boot:run"]
