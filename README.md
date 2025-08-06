# 🚗 Sistema de Gestão de Veículos e Pneus

## 📋 Sobre o Projeto

Este é um sistema completo de gestão de veículos e pneus desenvolvido em Java 17 com Spring Boot. O projeto implementa todas as 6 APIs solicitadas no desafio, com autenticação JWT e testes automatizados.

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Por que escolhi |
|------------|--------|-----------------|
| Java | 17 | Versão LTS moderna e estável |
| Spring Boot | 3.2.0 | Framework robusto para APIs |
| Spring Security + JWT | - | Autenticação segura |
| PostgreSQL | - | Banco confiável e robusto |
| Flyway | - | Controle de versão do banco |
| Docker | - | Facilita deploy e testes |
| JUnit 5 | - | Testes modernos e eficientes |

## �� Como Executar

### Opção 1: Docker (Recomendado)

```bash
# Clone o repositório
git clone <url-do-repositorio>
cd desafioJava

# Execute com Docker
mvn clean package
docker-compose up --build
```

A API estará disponível em: `http://localhost:8080`

### Opção 2: Localmente

**Pré-requisitos:**
- Java 17 instalado
- PostgreSQL instalado
- Banco `desafio_veiculos` criado

```bash
# Execute a aplicação
mvn spring-boot:run
```

## �� Autenticação

### 1. Registrar usuário

```bash
POST /api/auth/registrar
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "123456"
}
```

### 2. Fazer login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "123456"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer"
}
```

## 📡 APIs do Sistema

### 1. Listar todos os veículos

```bash
GET /api/veiculos
```

**Resposta:**
```json
[
  {
    "id": 1,
    "placa": "ABC1234",
    "marca": "Ford",
    "quilometragem": 50000,
    "status": "ATIVO"
  }
]
```

### 2. Buscar veículo específico (com pneus)

```bash
GET /api/veiculos/1
```

**Resposta:**
```json
{
  "id": 1,
  "placa": "ABC1234",
  "marca": "Ford",
  "quilometragem": 50000,
  "status": "ATIVO",
  "pneusAplicados": [
    {
      "id": 1,
      "numeroFogo": "P001-2024-001",
      "marca": "Michelin",
      "posicao": "DIANTEIRA_ESQUERDA",
      "pressaoAtual": 32.5
    }
  ]
}
```

### 3. Criar veículo

```bash
POST /api/veiculos
Content-Type: application/json

{
  "placa": "ABC1234",
  "marca": "Ford",
  "quilometragem": 50000,
  "status": "ATIVO"
}
```

### 4. Criar pneu

```bash
POST /api/pneus
Content-Type: application/json

{
  "numeroFogo": "P001-2024-001",
  "marca": "Michel
