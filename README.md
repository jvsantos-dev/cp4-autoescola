# API Auto-Escola

API REST em Spring Boot para gestão de uma auto-escola: cadastro de instrutores e alunos, agendamento/cancelamento de instruções, e autenticação/autorização de usuários via JWT.

Disciplina: SOA e WebServices — Prof. Carlos Eduardo Machado de Oliveira

## Integrantes

Joao Victor Oliveira dos Santos - RM557948
Matheus Alcântara Estevão - RM558193
Nicolle Pellegrino Jelinski - RM558610
Pedro Pereira dos Santos - RM552047
Eric Segawa Montagner- RM558224

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web, Data JPA, Security, Validation)
- PostgreSQL (produção) / H2 (testes)
- JWT (io.jsonwebtoken / jjwt)
- Lombok

## Como rodar

### Pré-requisitos

- JDK 21+
- PostgreSQL rodando localmente (padrão em `application.properties`: `localhost:5432/autoescola`, usuário/senha `postgres`/`postgres`), ou ajuste via variáveis de ambiente `spring.datasource.*`.

### Executar

```bash
./mvnw spring-boot:run
```

### Rodar os testes (usa H2 em memória, não precisa de Postgres)

```bash
./mvnw test
```

Ao subir pela primeira vez, um usuário administrador padrão é criado automaticamente (ver log da aplicação):

```
E-mail: admin@autoescola.com
Senha:  Admin@123
```

**Troque essa senha em produção.** O segredo do JWT (`jwt.secret`) também deve ser sobrescrito via variável de ambiente `JWT_SECRET` em produção.

## Autenticação

Todas as rotas exigem um token JWT no cabeçalho `Authorization: Bearer <token>`, exceto `/auth/login`.

```
POST /auth/login
{ "email": "admin@autoescola.com", "senha": "Admin@123" }
```

Retorna `{ "token": "...", "tipo": "Bearer" }`.

## Endpoints

### Instrutores (autenticado)
- `POST /instrutores` — cadastrar
- `GET /instrutores?page=0` — listar (paginado, 10/página, ordenado por nome)
- `GET /instrutores/{id}` — buscar
- `PUT /instrutores/{id}` — atualizar (nome, telefone, endereço)
- `DELETE /instrutores/{id}` — inativar (exclusão lógica)

### Alunos (autenticado)
- `POST /alunos`, `GET /alunos?page=0`, `GET /alunos/{id}`, `PUT /alunos/{id}`, `DELETE /alunos/{id}` — mesmo padrão dos instrutores.

### Instruções (autenticado)
- `POST /instrucoes` — agendar (`alunoId`, `instrutorId` opcional, `dataHora`)
- `GET /instrucoes?page=0` — listar
- `GET /instrucoes/{id}` — buscar
- `PATCH /instrucoes/{id}/cancelar` — cancelar (`motivo`: `ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU` ou `OUTROS`)

### Usuários (apenas ADMIN, exceto troca de senha)
- `POST /usuarios` — cadastrar usuário
- `GET /usuarios?page=0` — listar
- `PUT /usuarios/{id}` — atualizar nome/perfil
- `DELETE /usuarios/{id}` — excluir
- `PUT /usuarios/senha` — qualquer usuário autenticado altera a própria senha (`senhaAtual`, `novaSenha`)
