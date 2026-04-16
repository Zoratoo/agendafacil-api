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

## Modelagem

O sistema utiliza uma tabela `establishment_users` para gerenciar os papéis dos usuários dentro de cada estabelecimento. Um usuário pode ser `OWNER` em um estabelecimento e `PROFESSIONAL` em outro simultaneamente.

O fluxo de vínculo é feito por convites — um `OWNER` convida um usuário por email, e o convidado aceita ou rejeita a solicitação.

O algoritmo de disponibilidade cruza `WorkingHours`, `BlockedSlots` e `Bookings` existentes para calcular os horários livres de um profissional em uma data específica.

## Endpoints

### Auth
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/auth/register` | Cadastro de usuário | Público |
| POST | `/auth/login` | Login e geração de token | Público |

### Establishment
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/establishments` | Criar estabelecimento | Autenticado |
| GET | `/establishments` | Listar meus estabelecimentos | Autenticado |

### Invitations
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/invitations` | Convidar usuário para o estabelecimento | OWNER do estabelecimento |
| GET | `/invitations/my` | Ver meus convites pendentes | Autenticado |
| PATCH | `/invitations/{id}/accept` | Aceitar convite | Autenticado |
| PATCH | `/invitations/{id}/reject` | Rejeitar convite | Autenticado |

### Services Offered
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/services-offered` | Criar serviço | OWNER do estabelecimento |
| GET | `/services-offered/establishments/{id}` | Listar serviços do estabelecimento | Autenticado |

### Schedule
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| POST | `/schedule/working-hours` | Definir horários de trabalho | PROFESSIONAL do estabelecimento |
| POST | `/schedule/blocked-slots` | Bloquear horário/dia | PROFESSIONAL do estabelecimento |
| GET | `/schedule/my/establishment/{id}/working-hours` | Ver minha agenda no estabelecimento | Autenticado |
| GET | `/schedule/professional/{userId}/establishment/{id}/working-hours` | Ver agenda de um profissional | Autenticado |

### Bookings
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| GET | `/bookings/available-slots` | Listar slots disponíveis de um profissional | Autenticado |
| POST | `/bookings` | Criar agendamento | Autenticado |
| PATCH | `/bookings/{id}/cancel` | Cancelar agendamento | Autenticado |