
# Sistema de Controle de Despesas

## Descrição

Sistema de Controle de Despesas é uma aplicação Spring Boot desenvolvida para gerenciar despesas e receitas pessoais. O sistema permite aos usuários cadastrar categorias de gastos, registrar lançamentos financeiros e acompanhar seu fluxo de caixa de forma organizada.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **MySQL 9.1.0**
- **SpringDoc OpenAPI 2.8.9**
- **Docker**
- **Maven**

## Estrutura do Projeto

```
Sistema_de_Controle_de_Despesas/
├── src/
│   ├── main/
│   │   ├── java/com/glaudencio12/Sistema_de_Controle_de_Despesas/
│   │   │   ├── config/                    # Configurações da aplicação
│   │   │   ├── controllers/               # Controladores REST
│   │   │   │   └── docs/                  # Documentação dos controladores
│   │   │   ├── dto/                       # Data Transfer Objects
│   │   │   │   ├── request/               # DTOs de requisição
│   │   │   │   └── response/              # DTOs de resposta
│   │   │   ├── exception/                 # Tratamento de exceções
│   │   │   ├── mapper/                    # Mapeadores de objetos
│   │   │   ├── models/                    # Entidades JPA
│   │   │   │   └── enums/                 # Enumerações
│   │   │   ├── repository/                # Repositórios JPA
│   │   │   ├── services/                  # Camada de serviços
│   │   │   └── utils/                     # Utilitários
│   │   └── resources/
│   │       ├── application.yml            # Configurações da aplicação
│   │       └── db/migration/              # Migrações do banco
│   └── test/                              # Testes unitários
├── Dockerfile                             # Configuração Docker
├── compose.yaml                           # Docker Compose
├── pom.xml                               # Dependências Maven
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

## Funcionalidades

### ✅ Usuários
- Cadastro completo de usuários
- Validação de email único
- CRUD completo com validações
- Relacionamento com categorias e lançamentos

### ✅ Categorias
- Criação de categorias por usuário
- Tipos: Receita e Despesa
- Validação de nomes únicos por usuário
- CRUD completo com validações

### ✅ Lançamentos
- Registro de receitas e despesas
- Associação com categorias
- Validação de dados e relacionamentos
- CRUD completo com validações

### ✅ Recursos Avançados
- **HATEOAS** para navegação da API
- **Documentação OpenAPI** completa
- **Tratamento de exceções** robusto
- **Mapeamento de objetos** com ModelMapper
- **Validações de entrada** com Bean Validation
- **Suporte a múltiplos formatos** (JSON, XML, YAML)

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
   docker pull glaudencio123/despesas-pessoais:v1.1 .
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
    - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Usuários (`/api/usuarios`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|---------|
| `POST` | `/api/usuarios` | Criar novo usuário | ✅ |
| `GET` | `/api/usuarios` | Listar todos os usuários | ✅ |
| `GET` | `/api/usuarios/{id}` | Buscar usuário por ID | ✅ |
| `PUT` | `/api/usuarios/{id}` | Atualizar usuário | ✅ |
| `DELETE` | `/api/usuarios/{id}` | Deletar usuário | ✅ |

### Categorias (`/api/categorias`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|---------|
| `POST` | `/api/categorias` | Criar nova categoria | ✅ |
| `GET` | `/api/categorias` | Listar todas as categorias | ✅ |
| `GET` | `/api/categorias/{id}` | Buscar categoria por ID | ✅ |
| `PUT` | `/api/categorias/{id}` | Atualizar categoria | ✅ |
| `DELETE` | `/api/categorias/{id}` | Deletar categoria | ✅ |

### Lançamentos (`/api/lancamentos`)

| Método | Endpoint | Descrição | Status |
|--------|----------|-----------|---------|
| `POST` | `/api/lancamentos` | Criar novo lançamento | ✅ |
| `GET` | `/api/lancamentos` | Listar todos os lançamentos | ✅ |
| `GET` | `/api/lancamentos/{id}` | Buscar lançamento por ID | ✅ |
| `PUT` | `/api/lancamentos/{id}` | Atualizar lançamento | ✅ |
| `DELETE` | `/api/lancamentos/{id}` | Deletar lançamento | ✅ |

## Documentação da API

### Swagger UI
A documentação interativa da API está disponível através do Swagger UI:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8080/v3/api-docs.yaml`

### Exemplos de Uso

#### Criar Usuário
```bash
POST "http://localhost:8080/api/usuarios" \
  "Content-Type: application/json" \
  '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123"
  }'
```

#### Criar Categoria
```bash
POST "http://localhost:8080/api/categorias" \
  "Content-Type: application/json" \
  '{
    "nome": "Alimentação",
    "tipo": "DESPESA"
  }'
```

#### Criar Lançamento
```bash
POST "http://localhost:8080/api/lancamentos" \
  "Content-Type: application/json" \
  '{
    "descricao": "Almoço no restaurante",
    "valor": 25.50,
    "data": "2024-01-15",
    "tipo": "DESPESA",
    "categoriaId": 1
  }'
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

## Segurança e Validações

### Validações Implementadas

- ✅ **Validação de dados de entrada** com `@Valid`
- ✅ **Tratamento de exceções** personalizado e centralizado
- ✅ **Validação de unicidade** de email de usuário
- ✅ **Validação de categorias duplicadas** por usuário
- ✅ **Validação de relacionamentos** entre entidades
- ✅ **Sanitização de dados** de entrada

### Exceções Personalizadas

- `NotFoundException` - Recurso não encontrado
- `EmailCannotBeDuplicatedException` - Email duplicado
- `CategoryCannotBeDuplicateException` - Categoria duplicada
- `ValidationException` - Erros de validação

## Roadmap

### Funcionalidades Planejadas
- [ ] **Autenticação e Autorização** com JWT
- [ ] **Relatórios e Dashboards** financeiros
- [ ] **Notificações** de lançamentos
- [ ] **Importação/Exportação** de dados
- [ ] **Métricas e Monitoramento** com Actuator
- [ ] **Testes de integração** completos

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

## Suporte

Se você encontrar algum problema ou tiver dúvidas:

1. **Verifique** a documentação da API
3. **Crie uma nova issue** com detalhes do problema
4. **Entre em contato** através do GitHub
