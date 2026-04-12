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

### Establishment
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/establishment` | Criar estabelecimento | OWNER |
| GET | `/establishment` | Listar estabelecimentos do owner | OWNER, SUPER_ADMIN |

### Professional
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/professionals` | Criar profissional | OWNER |
| GET | `/professionals/establishment/{id}` | Listar profissionais do estabelecimento | OWNER |
| POST | `/professionals/{id}/establishment/{id}` | Vincular profissional a estabelecimento | OWNER |

### Services Offered
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/services-offered` | Criar serviço | OWNER |
| GET | `/services-offered/establishments/{id}` | Listar serviços do estabelecimento | Todos |