# Brasfi - Sistema de Gestão Financeira

Sistema de gestão financeira desenvolvido com Spring Boot.

## Requisitos

- Java 17 ou superior
- Maven 3.6 ou superior

## Configuração do Ambiente

1. Clone o repositório
2. Navegue até a pasta do projeto
3. Execute o comando para instalar as dependências:
```bash
mvn clean install
```

## Executando a Aplicação

Para executar a aplicação, use o comando:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: http://localhost:8080/api

## Console H2

O console do banco de dados H2 está disponível em: http://localhost:8080/api/h2-console

Credenciais:
- JDBC URL: jdbc:h2:mem:brasfidb
- Username: sa
- Password: (vazio)

## Estrutura do Projeto

```
src/main/java/br/brasfi/
├── controller/    # Controladores REST
├── service/       # Lógica de negócio
├── repository/    # Acesso a dados
├── model/         # Entidades e DTOs
└── BrasfiApplication.java
```

## Tecnologias Utilizadas

- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Lombok
- Maven 