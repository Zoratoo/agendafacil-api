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
| POST | `/establishment` | Criar estabelecimento | Autenticado |
| GET | `/establishment` | Listar meus estabelecimentos | Autenticado |

### Professional
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/professionals` | Vincular profissional ao estabelecimento | OWNER |
| GET | `/professionals/establishment/{id}` | Listar profissionais do estabelecimento | Autenticado |
| POST | `/professionals/{id}/establishment/{id}` | Adicionar profissional a outro estabelecimento | OWNER |

### Services Offered
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/services-offered` | Criar serviço | OWNER |
| GET | `/services-offered/establishments/{id}` | Listar serviços do estabelecimento | Autenticado |

### Schedule
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/schedule/working-hours` | Definir horários de trabalho | PROFESSIONAL |
| POST | `/schedule/blocked-slots` | Bloquear horário/dia | PROFESSIONAL |
| GET | `/schedule/my/working-hours` | Ver minha agenda | PROFESSIONAL |
| GET | `/schedule/professional/{id}/working-hours` | Ver agenda de um profissional | Autenticado |