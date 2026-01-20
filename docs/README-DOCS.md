# 🧩 Documentação da API

Este documento descreve os principais endpoints da API e como realizar testes rápidos diretamente pelo terminal utilizando **cURL**.

A collection Postman completa está disponível em:  
📂 [`docs/collection.json`](./collection.json)

Os prints de sucesso e erro utilizando o Postman estão disponíveis em:  
📂 [`docs/atualiza`](./atualiza)
📂 [`docs/cadastrao`](./cadastro)
📂 [`docs/deleta`](./deleta)
📂 [`docs/login`](./login)
📂 [`docs/recupera`](./recupera)
📂 [`docs/senha`](./senha)

Os prints de sucesso e erro utilizando o Swagger estão disponíveis em:  
📂 [`docs/swagger`](./swagger)

---

## 🚀 Estrutura da Collection

A collection contém todas as rotas da aplicação, organizadas nas seguintes seções:

| Grupo | Descrição                       |
|--------|---------------------------------|
| 🧾 **Cadastro de usuário** | Criação de novos usuários       |
| ✏️ **Atualização de usuário** | Edição de dados do usuário      |
| 🔍 **Recupera usuários** | Busca por nome ou listagem geral |
| 🗑️ **Deleta usuário** | Remoção de usuário              |
| 🔐 **Atualização de senha** | Alteração de senha     |
| 🍽️ **Restaurantes** | Gerenciamento de restaurantes |
| 🍔 **Itens do Menu** | Gerenciamento de itens do cardápio |
| 🛡️ **Perfis (Roles)** | Gerenciamento de perfis de acesso |

---

## 🧾 Cadastro de Usuário — `POST /v1/users`

### ✅ Sucesso

```bash
curl -X POST http://localhost:8080/v1/users   -H "Content-Type: application/json"   -d '{
    "name": "Usuário Teste",
    "email": "usuario.teste@teste.com",
    "password": "senha123",
    "address": "Rua Teste 100",
    "login": "usuarioteste1",
    "roleId": 1
  }'
```

**Resposta (201):**
```json
{
  "id": 1,
  "name": "Usuário Teste",
  "email": "usuario.teste@teste.com",
  "address": "Rua Teste 100",
  "login": "usuarioteste1",
  "createdAt": "2025-10-28T21:37:04.4101015",
  "lastUpdatedAt": "2025-10-28T21:37:04.4101015",
  "lastLoginAt": null,
  "active": true,
  "roleName": "CLIENT"
}
```

### ❌ Erro — e-mail inválido

```bash
curl -X POST http://localhost:8080/v1/users   -H "Content-Type: application/json"   -d '{
    "name": "Teste Email",
    "email": "emailinvalido",
    "password": "teste123",
    "address": "Rua 1",
    "login": "testeemail",
    "roleId": 1
  }'
```

**Resposta (400):**
```json
{ "email": "Formato de e-mail inválido" }
```

---

## 🔐 Login — `POST /v1/login`

### ✅ Sucesso

```bash
curl -X POST http://localhost:8080/v1/login   -H "Content-Type: application/json"   -d '{
    "login": "usuarioteste1",
    "password": "senha123"
  }'
```

**Resposta (200):**
```json
{ "token": "f2cea676-d06f-43a4-a7b2-06806535a0c6" }
```

### ❌ Erro — senha incorreta

```bash
curl -X POST http://localhost:8080/v1/login   -H "Content-Type: application/json"   -d '{
    "login": "usuarioteste1",
    "password": "senha_errada"
  }'
```

**Resposta (404):**
```text
Login ou senha inválidos
```

---

## ✏️ Atualização de Usuário — `PATCH /v1/users/{id}`

### ✅ Sucesso

```bash
curl -X PATCH http://localhost:8080/v1/users/1   -H "Authorization: <token>"   -H "Content-Type: application/json"   -d '{
    "name": "Nome Atualizado",
    "email": "atualizado@teste.com",
    "address": "Rua Atualizada",
    "login": "usuarioteste1",
    "roleId": 2
  }'
```

**Resposta (200):**
```json
{
  "id": 1,
  "name": "Nome Atualizado",
  "email": "atualizado@teste.com",
  "address": "Rua Atualizada",
  "login": "usuarioteste1",
  "createdAt": "2025-10-28T21:37:04.410102",
  "lastUpdatedAt": "2025-10-28T21:37:04.410102",
  "lastLoginAt": null,
  "active": true,
  "roleName": "RESTAURANT_OWNER"
}
```

### ❌ Erro — token inválido

```bash
curl -X PATCH http://localhost:8080/v1/users/1   -H "Authorization: token_invalido"   -H "Content-Type: application/json"   -d '{ "name": "Novo Nome" }'
```

**Resposta (401):**
```text
Acesso negado: token ausente ou inválido. Verifique o cabeçalho 'Authorization' e tente novamente.
```

---

## 🔍 Recuperar Usuário — `GET /v1/users?name={name}`

### ✅ Sucesso

```bash
curl -X GET "http://localhost:8080/v1/users?name=Guilherme"   -H "Authorization: <token>"
```

**Resposta (200):**
```json
[
  {
    "id": 2,
    "name": "Guilherme",
    "email": "guilherme.gaspar@teste.com",
    "address": "Rua 5",
    "login": "loginprincipal",
    "createdAt": "2025-10-28T21:40:31.633351",
    "lastUpdatedAt": "2025-10-28T21:40:33.694727",
    "lastLoginAt": "2025-10-28T21:40:33.693077",
    "active": true,
    "roleName": "CLIENT"
  }
]
```

### ❌ Erro — token ausente

```bash
curl -X GET "http://localhost:8080/v1/users?name=Usuário Teste"
```

**Resposta (401):**
```text
Acesso negado: token ausente ou inválido. Verifique o cabeçalho 'Authorization' e tente novamente.
```

---

## 🗑️ Deletar Usuário — `DELETE /v1/users/{id}`

### ✅ Sucesso

```bash
curl -X DELETE http://localhost:8080/v1/users/1   -H "Authorization: <token>"
```

**Resposta (204):**
```
(no content)
```

### ❌ Erro — usuário inexistente

```bash
curl -X DELETE http://localhost:8080/v1/users/999   -H "Authorization: <token>"
```

**Resposta (400):**
```text
Usuário nao encontrado com o ID: 999
```

---

## 🔑 Atualizar Senha — `PATCH /v1/users/{id}/password`

### ✅ Sucesso

```bash
curl -X PATCH http://localhost:8080/v1/users/1/password   -H "Authorization: <token>"   -H "Content-Type: application/json"   -d '{ "newPassword": "novaSenha123" }'
```

**Resposta (204):**
```
(no content)
```

### ❌ Erro — senha vazia

```bash
curl -X PATCH http://localhost:8080/v1/users/1/password   -H "Authorization: <token>"   -H "Content-Type: application/json"   -d '{ "newPassword": "" }'
```

**Resposta (400):**
```json
{
  "newPassword": "A nova senha é obrigatória"
}
```

---

## 🍽️ Restaurantes

### ✅ Criar Restaurante — `POST /v1/restaurants`

```bash
curl -X POST http://localhost:8080/v1/restaurants \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante Exemplo",
    "address": "Rua das Flores, 123",
    "cuisineType": "Italiana",
    "openingHours": "18:00 - 23:00",
    "ownerId": 1
  }'
```

### ✅ Listar Restaurantes — `GET /v1/restaurants`

```bash
curl -X GET http://localhost:8080/v1/restaurants \
  -H "Authorization: <token>"
```

### ✅ Buscar Restaurante por ID — `GET /v1/restaurants/{id}`

```bash
curl -X GET http://localhost:8080/v1/restaurants/1 \
  -H "Authorization: <token>"
```

### ✅ Atualizar Restaurante — `PATCH /v1/restaurants/{id}`

```bash
curl -X PATCH http://localhost:8080/v1/restaurants/1 \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurante Atualizado",
    "address": "Nova Rua, 456",
    "cuisineType": "Brasileira",
    "openingHours": "11:00 - 22:00"
  }'
```

### ✅ Deletar Restaurante — `DELETE /v1/restaurants/{id}`

```bash
curl -X DELETE http://localhost:8080/v1/restaurants/1 \
  -H "Authorization: <token>"
```

---

## 🍔 Itens do Menu

### ✅ Criar Item do Menu — `POST /v1/menu-items`

```bash
curl -X POST http://localhost:8080/v1/menu-items \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Margherita",
    "description": "Molho de tomate, mussarela e manjericão",
    "price": 45.00,
    "restaurantId": 1,
    "availableOnlyInRestaurant": false
  }'
```

### ✅ Listar Itens por Restaurante — `GET /v1/menu-items/restaurant/{restaurantId}`

```bash
curl -X GET http://localhost:8080/v1/menu-items/restaurant/1 \
  -H "Authorization: <token>"
```

### ✅ Buscar Item por ID — `GET /v1/menu-items/{id}`

```bash
curl -X GET http://localhost:8080/v1/menu-items/1 \
  -H "Authorization: <token>"
```

### ✅ Atualizar Item do Menu — `PATCH /v1/menu-items/{id}`

```bash
curl -X PATCH http://localhost:8080/v1/menu-items/1 \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pizza Calabresa",
    "price": 50.00,
    "description": "Calabresa acebolada"
  }'
```

### ✅ Deletar Item do Menu — `DELETE /v1/menu-items/{id}`

```bash
curl -X DELETE http://localhost:8080/v1/menu-items/1 \
  -H "Authorization: <token>"
```

---

## 🛡️ Perfis (Roles)

### ✅ Criar Perfil — `POST /v1/roles`

```bash
curl -X POST http://localhost:8080/v1/roles \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "ADMIN"
  }'
```

### ✅ Listar Perfis — `GET /v1/roles`

```bash
curl -X GET http://localhost:8080/v1/roles \
  -H "Authorization: <token>"
```

### ✅ Buscar Perfil por ID — `GET /v1/roles/{id}`

```bash
curl -X GET http://localhost:8080/v1/roles/1 \
  -H "Authorization: <token>"
```

### ✅ Atualizar Perfil — `PATCH /v1/roles/{id}`

```bash
curl -X PATCH http://localhost:8080/v1/roles/1 \
  -H "Authorization: <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "SUPER_ADMIN"
  }'
```

### ✅ Deletar Perfil — `DELETE /v1/roles/{id}`

```bash
curl -X DELETE http://localhost:8080/v1/roles/1 \
  -H "Authorization: <token>"
```

---

## 📚 Referências

- 🌐 [Swagger UI - Disponível se rodar o projeto](http://localhost:8080/swagger-ui/index.html)
- 🏠 [Voltar ao README principal](../README.md)
