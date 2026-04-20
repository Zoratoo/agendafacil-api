# AgendaFácil API

Sistema de agendamentos multi-tenant para clínicas, salões e quadras esportivas.

🚀 **API em produção:** [agendafacil-api-wb81.onrender.com](https://agendafacil-api-wb81.onrender.com)

📄 **Documentação interativa:** [Swagger UI](https://agendafacil-api-wb81.onrender.com/swagger-ui/index.html)

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

O algoritmo de disponibilidade cruza `WorkingHours`, `BlockedSlots` e `Bookings` existentes para calcular os horários livres de um profissional em uma data específica, com validação de conflitos de horário para profissionais e clientes.

## Tratamento de Erros

Todas as exceções são tratadas globalmente e retornam um JSON padronizado:

```json
{
    "status": 404,
    "message": "Establishment not found with id: ...",
    "timestamp": "2026-04-15T23:00:00"
}
```

## Testes

Testes unitários implementados com JUnit 5 e Mockito cobrindo os principais cenários do `BookingService`:

- Retorno de lista vazia quando profissional não tem horários cadastrados
- Lançamento de exceção quando cliente já tem agendamento no mesmo horário
- Retorno correto de slots disponíveis quando profissional tem horários cadastrados

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
| GET | `/establishments/{id}/professionals` | Listar profissionais do estabelecimento | Autenticado |

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