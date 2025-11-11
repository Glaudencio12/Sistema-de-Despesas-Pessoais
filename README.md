
# Sistema de Controle de Despesas

## 📋 Descrição

Sistema de Controle de Despesas é uma aplicação Spring Boot moderna e robusta desenvolvida para gerenciar despesas e receitas pessoais. O sistema oferece uma API REST completa com autenticação JWT, monitoramento em tempo real, documentação interativa e containerização Docker para facilitar o deploy e manutenção.

## 🚀 Tecnologias Utilizadas

### **🔧 Framework Principal**
- **Java 21** - Linguagem de programação
- **Spring Boot 3.5.3** - Framework principal para desenvolvimento de aplicações Java
- **Maven 3.9.9** - Gerenciamento de dependências e build

### **🗄️ Banco de Dados e Persistência**
- **MySQL 9.0.1** - Sistema de gerenciamento de banco de dados relacional
- **Spring Data JPA** - Camada de abstração para acesso a dados
- **Flyway 11.7.2** - Ferramenta de migração de banco de dados

### **🔐 Segurança e Autenticação**
- **Spring Security** - Framework de segurança para aplicações Spring
- **JWT (JSON Web Token)** - Autenticação baseada em tokens

### **🌐 Desenvolvimento Web**
- **Spring HATEOAS** - Suporte para APIs RESTful com hypermedia
- **Spring Validation** - Validação de dados de entrada

### **📊 Monitoramento e Observabilidade**
- **Spring Boot Actuator** - Monitoramento e métricas da aplicação
- **Micrometer** - Coleta de métricas de aplicação
- **Prometheus** - Sistema de monitoramento e alertas

### **📚 Documentação de API**
- **SpringDoc OpenAPI 2.8.9** - Geração automática de documentação Swagger/OpenAPI

### **🔄 Utilitários e Bibliotecas**
- **ModelMapper 3.2.0** - Mapeamento entre objetos DTO e entidades
- **Jackson** - Serialização/deserialização JSON, XML e YAML
- **DotEnv Java 3.0.0** - Gerenciamento de variáveis de ambiente

### **🐳 Containerização e Orquestração**
- **Docker** - Containerização da aplicação
- **Docker Compose** - Orquestração de múltiplos containers

### **🧪 Testes**
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Framework de mocking para testes

## Estrutura do Projeto

```
Sistema_de_Controle_de_Despesas/
├── src/
│   ├── main/
│   │   ├── java/com/glaudencio12/Sistema_de_Controle_de_Despesas/
│   │   │   ├── config/                    # Configurações da aplicação (6 arquivos)
│   │   │   ├── controllers/               # Controladores REST (7 arquivos)
│   │   │   ├── dto/                       # Data Transfer Objects (8 arquivos)
│   │   │   ├── exception/                 # Tratamento de exceções (7 arquivos)
│   │   │   ├── mapper/                    # Mapeadores de objetos (2 arquivos)
│   │   │   ├── models/                    # Entidades JPA (4 arquivos)
│   │   │   ├── repository/                # Repositórios JPA (3 arquivos)
│   │   │   ├── security/                  # Configurações de segurança (2 arquivos)
│   │   │   ├── services/                  # Camada de serviços (4 arquivos)
│   │   │   ├── utils/                     # Utilitários (2 arquivos)
│   │   │   └── Startup.java               # Classe principal da aplicação
│   │   └── resources/
│   │       ├── application.yml            # Configurações da aplicação
│   │       ├── application-dev.yml        # Configuração ambiente de dev
│   │       ├── application-prod.yml       # Configuração ambiente de prod
│   │       ├── db/migration/              # Migrações do banco (10 arquivos)
│   │       └── META-INF/
│   │           └── spring.factories       # Configurações do Spring
│   └── test/
│       └── java/com/glaudencio12/Sistema_de_Controle_de_Despesas/
│           ├── services/                  # Testes de serviços (3 arquivos)
│           ├── stubs/                     # Stubs para testes (3 arquivos)
│           └── StartupTests.java          # Testes de inicialização
├── target/                                # Diretório de build (gerado automaticamente)
├── Dockerfile                             # Configuração Docker
├── compose.yaml                           # Docker Compose
├── pom.xml                               # Dependências Maven
├── prometheus.yml                        # Configuração do Prometheus
├── mvnw                                  # Maven Wrapper (Unix)
├── mvnw.cmd                              # Maven Wrapper (Windows)
└── README.md                             # Este arquivo
```

## Modelo de Dados

### Entidades Principais

#### Usuario
- `id`: Identificador único
- `nome`: Nome do usuário (máx. 100 caracteres)
- `email`: Email único (máx. 60 caracteres)
- `senha`: Senha do usuário (máx. 100 caracteres)
- `dataCadastro`: Data de cadastro
- `categorias`: Lista de categorias do usuário
- `lancamentos`: Lista de lançamentos do usuário

#### Categoria
- `id`: Identificador único
- `nome`: Nome da categoria (máx. 50 caracteres)
- `tipo`: Tipo de lançamento (RECEITA/DESPESA)
- `usuario`: Usuário proprietário
- `lancamentos`: Lista de lançamentos da categoria

#### Lancamento
- `id`: Identificador único
- `descricao`: Descrição do lançamento (máx. 150 caracteres)
- `valor`: Valor do lançamento (precisão 15,2)
- `data`: Data do lançamento
- `tipo`: Tipo de lançamento (RECEITA/DESPESA)
- `categoria`: Categoria do lançamento
- `usuario`: Usuário proprietário

## 🎯 Funcionalidades

### ✅ **Gestão de Usuários**
- Cadastro completo de usuários com validações
- Validação de email único
- CRUD completo com validações robustas
- Relacionamento com categorias e lançamentos
- Sistema de papéis e permissões

### ✅ **Gestão de Categorias**
- Criação de categorias por usuário
- Tipos: Receita e Despesa
- Validação de nomes únicos por usuário
- CRUD completo com validações
- Associação automática com lançamentos

### ✅ **Gestão de Lançamentos**
- Registro de receitas e despesas
- Associação com categorias
- Validação de dados e relacionamentos
- CRUD completo com validações
- Controle de data e valores

### ✅ **Segurança e Autenticação**
- **Autenticação JWT** com tokens de acesso e refresh
- **Spring Security** integrado
- **Criptografia de senhas** com BCrypt
- **Controle de acesso** baseado em papéis
- **Filtros de segurança** personalizados

### ✅ **Monitoramento e Observabilidade**
- **Métricas em tempo real** com Actuator
- **Integração com Prometheus** para coleta de métricas
- **Health checks** para verificação de saúde da aplicação
- **Logs estruturados** para debugging

### ✅ **Recursos Avançados**
- **HATEOAS** para navegação da API
- **Documentação OpenAPI** completa e interativa
- **Tratamento de exceções** robusto e centralizado
- **Mapeamento de objetos** com ModelMapper
- **Validações de entrada** com Bean Validation
- **Suporte a múltiplos formatos** (JSON, XML, YAML)
- **Migração de banco** com Flyway
- **Containerização** com Docker

## Configuração e Instalação

### Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **MySQL 8.0+** ou **MySQL 9.0+**
- **Docker** e **Docker Compose** (opcional, mas recomendado)

### Executando Localmente
**Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd Sistema_de_Controle_de_Despesas
   ```

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
# ========================================
# CONFIGURAÇÕES DO BANCO DE DADOS LOCAL
# ========================================
DB_URL_LOCAL=jdbc:mysql://localhost:3306/sistema_despesas?useTimezone=true&serverTimezone=UTC&createDatabaseIfNotExist=true
DB_USER_LOCAL=seu_usuario_local
DB_PASSWORD_LOCAL=sua_senha_local

# ========================================
# CONFIGURAÇÕES DO BANCO DE DADOS DOCKER
# ========================================
DB_URL_DOCKER=jdbc:mysql://db:3306/despesas_pessoais?useTimezone=true&serverTimezone=UTC
DB_USER_DOCKER=usuario_docker
DB_PASSWORD_DOCKER=senha_docker

# ========================================
# CONFIGURAÇÕES DO MYSQL DOCKER
# ========================================
MYSQL_USER=usuario_docker
MYSQL_PASSWORD=senha_docker
MYSQL_ROOT_PASSWORD=senha_root_segura

# ========================================
# CONFIGURAÇÕES DE SEGURANÇA JWT
# ========================================
JWT_SECRET_KEY=sua_chave_secreta_jwt_muito_segura_aqui
JWT_EXPIRE_LENGTH=3600000

# ========================================
# CONFIGURAÇÕES DE MONITORAMENTO
# ========================================
PROMETHEUS_ENABLED=true
ACTUATOR_ENABLED=true
```

1 - **Configure o banco de dados:**
- Instale e configure o MySQL
- Crie um banco chamado `sistema_despesas`
- Configure as variáveis de ambiente no arquivo `.env`

2 - **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3 - **Acesse a aplicação:**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Executando com Docker

1. **Pull da imagem da aplicação:**
   ```bash
   docker pull glaudencio12/despesas-pessoais:v1.2 .
   ```

2. **Execute com Docker Compose:**
   ```bash
   docker-compose up -d
   ```

3. **Verifique os serviços:**
   ```bash
   docker-compose ps
   ```

4. **Acesse a aplicação:**
    - API: `http://localhost:8080`
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - Health Check: `http://localhost:8080/actuator/health`
    - Métricas: `http://localhost:8080/actuator/metrics`
    - Prometheus: `http://localhost:9091`

## 🐳 Docker e Deployment

### Serviços Incluídos

O `docker-compose.yml` inclui os seguintes serviços:

- **app**: Aplicação Spring Boot (porta 8080)
- **db**: MySQL 9.0.1 (porta 3307)
- **prometheus**: Sistema de monitoramento (porta 9091)

### Comandos Docker Úteis

```bash
# Iniciar todos os serviços
docker-compose up -d

# Parar todos os serviços
docker-compose down

# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do banco de dados
docker-compose logs -f db

# Ver logs do Prometheus
docker-compose logs -f prometheus

# Rebuild da aplicação
docker-compose up --build app

# Limpar volumes (CUIDADO: remove dados do banco)
docker-compose down -v
```

### Variáveis de Ambiente Docker

Certifique-se de que o arquivo `.env` contém todas as variáveis necessárias:

```env
# Banco de dados
DB_URL_DOCKER=jdbc:mysql://db:3306/despesas_pessoais?useTimezone=true&serverTimezone=UTC
DB_USER_DOCKER=usuario_docker
DB_PASSWORD_DOCKER=senha_docker

# MySQL
MYSQL_USER=usuario_docker
MYSQL_PASSWORD=senha_docker
MYSQL_ROOT_PASSWORD=senha_root_segura

# JWT
JWT_SECRET_KEY=sua_chave_secreta_jwt_muito_segura_aqui
```

## 🔗 API Endpoints

### 🔐 **Autenticação (`/api/auth`)**

| Método | Endpoint | Descrição | Status | Autenticação |
|--------|----------|-----------|---------|--------------|
| `POST` | `/api/auth/login` | Fazer login e obter token JWT | ✅ | ❌ |
| `POST` | `/api/auth/refresh` | Renovar token de acesso | ✅ | ❌ |
| `POST` | `/api/auth/logout` | Fazer logout | ❌ | ❌ |

### 👥 **Usuários (`/api/usuarios`)**

| Método | Endpoint                   | Descrição | Status | Autenticação |
|--------|----------------------------|-----------|---------|--------------|
| `POST` | `/api/usuarios/createUser` | Criar novo usuário | ✅ | ❌ |
| `GET` | `/api/usuarios`            | Listar todos os usuários | ✅ | ✅ |
| `GET` | `/api/usuarios/{id}`       | Buscar usuário por ID | ✅ | ✅ |
| `PUT` | `/api/usuarios/{id}`       | Atualizar usuário | ✅ | ✅ |
| `PATCH` | `/api/usuarios/{id}`       | Atualizar dados parcialmente | ✅ | ✅ |
| `DELETE` | `/api/usuarios/{id}`       | Deletar usuário | ✅ | ✅ |

### 📂 **Categorias (`/api/categorias`)**

| Método | Endpoint | Descrição | Status | Autenticação |
|--------|----------|-----------|---------|--------------|
| `POST` | `/api/categorias` | Criar nova categoria | ✅ | ✅ |
| `GET` | `/api/categorias` | Listar todas as categorias | ✅ | ✅ |
| `GET` | `/api/categorias/{id}` | Buscar categoria por ID | ✅ | ✅ |
| `PUT` | `/api/categorias/{id}` | Atualizar categoria | ✅ | ✅ |
| `DELETE` | `/api/categorias/{id}` | Deletar categoria | ✅ | ✅ |

### 💰 **Lançamentos (`/api/lancamentos`)**

| Método | Endpoint | Descrição | Status | Autenticação |
|--------|----------|-----------|---------|--------------|
| `POST` | `/api/lancamentos` | Criar novo lançamento | ✅ | ✅ |
| `GET` | `/api/lancamentos` | Listar todos os lançamentos | ✅ | ✅ |
| `GET` | `/api/lancamentos/{id}` | Buscar lançamento por ID | ✅ | ✅ |
| `PUT` | `/api/lancamentos/{id}` | Atualizar lançamento | ✅ | ✅ |
| `DELETE` | `/api/lancamentos/{id}` | Deletar lançamento | ✅ | ✅ |

### 📊 **Monitoramento (`/actuator`)**

| Método | Endpoint | Descrição | Status | Autenticação |
|--------|----------|-----------|---------|--------------|
| `GET` | `/actuator/health` | Status de saúde da aplicação | ✅ | ❌ |
| `GET` | `/actuator/metrics` | Métricas da aplicação | ✅ | ❌ |
| `GET` | `/actuator/prometheus` | Métricas para Prometheus | ✅ | ❌ |

## Documentação da API

### Swagger UI
A documentação interativa da API está disponível através do Swagger UI:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

### Exemplos de Uso

#### 🔐 **Autenticação**

**Fazer Login:**
```bash
POST "http://localhost:8080/api/auth/login" \
  "Content-Type: application/json" \
  '{
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

**Resposta:**
```json
{
  "email": "joao@email.com",
  "authenticated": true,
  "created": "2024-01-15T10:30:00Z",
  "expiration": "2024-01-15T11:30:00Z",
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 👥 **Usuários**

**Criar Usuário:**
```bash
POST "http://localhost:8080/api/usuarios" \
  "Content-Type: application/json" \
  '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123",
    "papel": "USER"
  }'
```

#### 📂 **Categorias**

**Criar Categoria (com autenticação):**
```bash
POST "http://localhost:8080/api/categorias" \
  "Content-Type: application/json" \
  "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  '{
    "nome": "Alimentação",
    "tipo": "DESPESA"
  }'
```

#### 💰 **Lançamentos**

**Criar Lançamento (com autenticação):**
```bash
POST "http://localhost:8080/api/lancamentos" \
  "Content-Type: application/json" \
  "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  '{
    "descricao": "Almoço no restaurante",
    "valor": 25.50,
    "data": "2024-01-15",
    "tipo": "DESPESA",
    "categoriaId": 1
  }'
```

#### 📊 **Monitoramento**

**Verificar Saúde da Aplicação:**
```bash
GET "http://localhost:8080/actuator/health"
```

**Obter Métricas:**
```bash
GET "http://localhost:8080/actuator/metrics"
```

## Testes

### Executando Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura (quando implementado)
mvn test jacoco:report

# Executar testes específicos
mvn test -Dtest=UsuarioServiceTest

```

## 🔒 Segurança e Validações

### Validações Implementadas

- ✅ **Validação de dados de entrada** com `@Valid`
- ✅ **Tratamento de exceções** personalizado e centralizado
- ✅ **Validação de unicidade** de email de usuário
- ✅ **Validação de categorias duplicadas** por usuário
- ✅ **Validação de relacionamentos** entre entidades
- ✅ **Sanitização de dados** de entrada
- ✅ **Autenticação JWT** com tokens seguros
- ✅ **Criptografia de senhas** com BCrypt
- ✅ **Controle de acesso** baseado em papéis

### Exceções Personalizadas

- `NotFoundException` - Recurso não encontrado
- `EmailCannotBeDuplicatedException` - Email duplicado
- `CategoryCannotBeDuplicateException` - Categoria duplicada
- `ValidationException` - Erros de validação
- `InvalidJwtAuthenticationException` - Token JWT inválido
- `InvalidTokenException` - Autenticação sem token

## 📊 Histórico de movimentações

### Spring Boot Actuator

### Logs Estruturados

- **Logs de aplicação** com níveis configuráveis
- **Logs de segurança** para auditoria
- **Logs de performance** para otimização
- **Logs de erro** para debugging

## 🗺️ Roadmap

### ✅ **Funcionalidades Implementadas**
- [✅] **Autenticação e Autorização** com JWT
- [✅] **Métricas e Monitoramento** com Actuator e Prometheus
- [✅] **Containerização** com Docker e Docker Compose
- [✅] **Documentação** completa com OpenAPI/Swagger
- [✅] **Migração de banco** com Flyway
- [✅] **Validações robustas** e tratamento de exceções
- [✅] **Testes unitários** básicos

### 🚀 **Funcionalidades Planejadas**
- [ ] **Relatórios e Dashboards** financeiros
- [ ] **Notificações** de lançamentos
- [ ] **Importação/Exportação** de dados (CSV, Excel)
- [ ] **Testes de integração** completos
- [ ] **Cache** com Redis para performance
- [ ] **Rate Limiting** para proteção da API
- [✅] **Auditoria** de operações (logs de auditoria)
- [ ] **Backup automático** do banco de dados
- [ ] **Interface web** para usuários finais
- [ ] **API de relatórios** com filtros avançados

## Contribuição

1. **Fork** o projeto
2. **Clone** o repositório forkado
3. **Crie uma branch** para sua feature (`git checkout -b feature/AmazingFeature`)
4. **Commit** suas mudanças (`git commit -m 'Add some AmazingFeature'`)
5. **Push** para a branch (`git push origin feature/AmazingFeature`)
6. **Abra um Pull Request**

## Autor

**Glaudencio12**
- GitHub: [@glaudencio12](https://github.com/glaudencio12)
