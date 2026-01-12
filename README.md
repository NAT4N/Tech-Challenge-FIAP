# 🧩 TechChallenge API - FIAP  

API RESTful desenvolvida como parte do projeto **TechChallenge da FIAP**, responsável pelo gerenciamento de usuários e autenticação (login, criação, atualização e exclusão de usuários).

## 🚀 Tecnologias Utilizadas

Este projeto foi desenvolvido com foco em **Java moderno**, **boas práticas de arquitetura Spring** e **containerização com Docker**.

### 🧠 Linguagem e Plataforma
- **Java 21**
- **Maven** – Gerenciador de dependências e build

### ⚙️ Frameworks e Bibliotecas Principais
- **Spring Boot 3.5.6** – Framework principal da aplicação
- **Spring Web** – Criação de APIs RESTful
- **Spring Data JPA** – Integração com o banco de dados
- **Spring Validation** – Validações automáticas com anotações

### 🗃️ Banco de Dados
- **PostgreSQL** – Banco de dados relacional utilizado
- **JPA** – Camada de persistência padronizada

### 🧩 Utilitários e Ferramentas de Suporte
- **MapStruct** – Mapeamento entre entidades e DTOs
- **Lombok** – Redução de boilerplate no código
- **Spring DevTools** – Hot reload durante o desenvolvimento

### 🧪 Testes e Qualidade
- **Spring Boot Starter Test** – Suporte a testes unitários e de integração
- **JaCoCo** – Geração de relatórios de cobertura de código

### 🐳 Infraestrutura e Deploy
- **Docker** e **Docker Compose** – Containerização e orquestração da aplicação
- **OpenAPI 3.1 (Swagger)** – Documentação interativa dos endpoints REST

---

## 📦 Como Executar o Projeto

### 🔧 Pré-requisitos
- [Docker](https://www.docker.com/)

### 🐳 Rodando com Docker Compose

O projeto já possui um `Dockerfile` e um `docker-compose.yml` configurados para facilitar o build e a execução da aplicação junto ao banco de dados PostgreSQL.

### ▶️ Subindo os containers

```bash
docker compose up -d
```

---
## 📘 Endpoints Principais

| Método     | Endpoint                  | Descrição                                    | Autenticação |
|------------|---------------------------|----------------------------------------------|--------------|
| **POST**   | `/v1/login`               | Realiza login e retorna o token              | Não          |
| **POST**   | `/v1/users`               | Cria um novo usuário                         | Não          |
| **GET**    | `/v1/users`               | Lista usuários ou busca por nome             | Sim          |
| **PATCH**  | `/v1/users/{id}`          | Atualiza parcialmente os dados de um usuário | Sim          |
| **PATCH**  | `/v1/users/{id}/password` | Altera a senha de um usuário                 | Sim          |
| **DELETE** | `/v1/users/{id}`          | Remove um usuário pelo ID                    | Sim          |

> 💡 **Nota:**  
> Para acessar os endpoints que requerem autenticação (`Sim` na coluna “Autenticação”), siga os passos abaixo:
>
> 1. **Cadastro do usuário:** antes de realizar login, é necessário criar uma conta usando o endpoint `/v1/users`.  
>    Exemplo de body para registro:
>    ```json
>    {
>      "name": "Guilherme Gaspar",
>      "email": "guilherme@gmail.com",
>      "password": "passwordforte123",
>      "address": "Rua Exemplo, 123",
>      "login": "guilhermeg",
>      "roleId": 2
>    }
>    ```
>    - `roleId = 1` → Cliente
>    - `roleId = 2` → Dono de restaurante
>
> 
> 2. **Login:** utilize o endpoint `/v1/login` com o login e senha cadastrados.  
>    Esse endpoint possui um **post-script** configurado no Postman que salva automaticamente o token retornado na variável de ambiente `authToken`.
>
> 
> 3. **Autenticação automática:** os endpoints que exigem autenticação já utilizam o header:
>    ```http
>    Authorization: {{authToken}}
>    ```
>    Assim, ao realizar o login com sucesso, o token é salvo e aplicado automaticamente nos próximos requests — **não é necessário colar manualmente o token**.
>
> 
> 4. **Ambiente do Postman:** certifique-se de **selecionar um ambiente ativo** antes de enviar as requisições.  
>    Se a variável `authToken` ainda não existir, o script do endpoint de login **a criará automaticamente na primeira execução**.
>
> 
> 5. **Caso o token não seja aplicado automaticamente:**  
>    copie o token retornado pelo login e adicione manualmente no header das requisições:
>    ```http
>    Authorization: <seu_token>
>    ```
>
> ⚙️ **Importante:** esse fluxo automatizado de autenticação **só funciona se você utilizar a collection Postman** disponível no repositório.  
> A collection pode ser importada diretamente a partir do arquivo [`collection.json`](./docs/collection.json) localizado no diretório [`docs`](./docs).
>
> Dessa forma, o fluxo de autenticação no Postman é totalmente automatizado:  
> **cadastre-se → faça login → os endpoints autenticados funcionarão automaticamente.**

---

## 📬 Collection Postman — Testes de API

Dentro do diretório [`docs`](./docs), você encontrará a [`collection`](./docs/collection.json) completa utilizada para testar a API do projeto e também os prints relacionadas as testes com a collection e testes com o swagger

Essa collection contém **requisições válidas e inválidas**. Nesse diretório de docs, inclui também **prints e exemplos visuais** de cada cenário de teste junto com os testes com swagger e postman.

A estrutura do diretório é organizada nas seguintes pastas:

- 🧾 **cadastro/** — Requisições de criação de usuário
- 🔄 **atualiza/** — Requisições de atualização de dados de usuário
- 🗑️ **deleta/** — Requisições de exclusão de usuários
- 🔍 **recupera/** — Requisições de busca e listagem de usuários
- 🔐 **senha/** — Requisições específicas de atualização de senha
- 🧾 **swagger/** — Exemplos de requisições utilizando o Swagger

Cada pasta contém:
- Imagens com **prints das respostas** de sucesso e erro;

👉 Para mais detalhes e exemplos visuais, consulte a documentação completa em [`docs/README-DOCS.md`](./docs/README-DOCS.md).
