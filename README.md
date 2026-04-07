# AgendaFácil API

Sistema de agendamentos multi-tenant para clínicas, salões e quadras esportivas.

## Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Security + JWT
- PostgreSQL
- Redis
- Docker
- Flyway

## Como rodar localmente

### Pré-requisitos
- Docker
- Java 21

### Passos
```bash
# Clone o repositório
git clone https://github.com/Zoratoo/agendafacil-api

# Suba o banco e o Redis
docker compose up -d postgres redis

# Configure o application.yml baseado no application-example.yml

# Rode a aplicação
./mvnw spring-boot:run
```

## Endpoints

### Auth
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/auth/register` | Cadastro de usuário | Público |
| POST | `/auth/login` | Login e geração de token | Público |
