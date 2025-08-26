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

## Arquitetura

O projeto segue uma arquitetura em camadas com as seguintes responsabilidades:

- **Controllers**: Endpoints REST da API
- **Services**: Lógica de negócio
- **Repository**: Acesso aos dados
- **Models**: Entidades JPA
- **DTOs**: Transferência de dados
- **Mappers**: Conversão entre objetos
- **Exception Handler**: Tratamento de erros

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

## Configuração e Instalação

### Pré-requisitos

- Java 21 ou superior
- Maven 3.6+
- MySQL 8.0+
- Docker (opcional)

### Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
DB_URL_LOCAL=jdbc:mysql://localhost:3306/sistema_despesas
DB_USER_LOCAL=seu_usuario
DB_PASSWORD_LOCAL=sua_senha
```

### Executando Localmente

1. **Clone o repositório:**
   ```bash
   git clone <url-do-repositorio>
   cd Sistema_de_Controle_de_Despesas
   ```

2. **Configure o banco de dados:**
   - Crie um banco MySQL chamado `sistema_despesas`
   - Configure as variáveis de ambiente no arquivo `.env`

3. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

### Executando com Docker

1. **Build da imagem:**
   ```bash
   docker build -t sistema-despesas .
   ```

2. **Execute com Docker Compose:**
   ```bash
   docker-compose up -d
   ```

## API Endpoints

### Usuários

- `POST /api/usuarios` - Criar usuário
- `GET /api/usuarios` - Listar usuários
- `GET /api/usuarios/{id}` - Buscar usuário por ID
- `PUT /api/usuarios/{id}` - Atualizar usuário
- `DELETE /api/usuarios/{id}` - Deletar usuário

### Categorias

- `POST /api/categorias` - Criar categoria
- `GET /api/categorias` - Listar categorias
- `GET /api/categorias/{id}` - Buscar categoria por ID
- `PUT /api/categorias/{id}` - Atualizar categoria
- `DELETE /api/categorias/{id}` - Deletar categoria

### Lançamentos

- `POST /api/lancamentos` - Criar lançamento
- `GET /api/lancamentos` - Listar lançamentos
- `GET /api/lancamentos/{id}` - Buscar lançamento por ID
- `PUT /api/lancamentos/{id}` - Atualizar lançamento
- `DELETE /api/lancamentos/{id}` - Deletar lançamento

## Documentação da API

A documentação interativa da API está disponível através do Swagger UI:

- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Testes (Em desenvolvimento)

### Executando Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn test jacoco:report
```

### Estrutura de Testes

- `src/test/java/` - Testes unitários


### Docker

```bash
# Build e execução
docker build -t sistema-despesas .
docker run -p 8080:8080 sistema-despesas
```

## Segurança

- Validação de dados de entrada
- Tratamento de exceções personalizado
- Validação de unicidade de email
- Validação de categorias duplicadas

## Funcionalidades

### Usuários
- ✅ Cadastro de usuários
- ✅ Validação de email único
- ✅ CRUD completo

### Categorias
- ✅ Criação de categorias por usuário
- ✅ Tipos: Receita e Despesa
- ✅ Validação de nomes únicos por usuário
- ✅ CRUD completo

### Lançamentos
- ✅ Registro de receitas e despesas
- ✅ Associação com categorias
- ✅ Validação de dados
- ✅ CRUD completo

### Recursos Adicionais
- ✅ HATEOAS para navegação
- ✅ Documentação OpenAPI
- ✅ Tratamento de exceções
- ✅ Mapeamento de objetos
- ✅ Validações de entrada

## Tratamento de Erros

O sistema possui tratamento personalizado para os seguintes erros:

- `NotFoundException` - Recurso não encontrado
- `EmailCannotBeDuplicatedException` - Email duplicado
- `CategoryCannotBeDuplicateException` - Categoria duplicada
- Validações de entrada com `@Valid`

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

