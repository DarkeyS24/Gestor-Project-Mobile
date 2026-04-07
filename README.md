# Gestor App — Mobile

Aplicação mobile desenvolvida com TotalCross para gerenciamento offline de clientes pessoas físicas e jurídicas, com validação de CPF/CNPJ, exclusão lógica e sincronização com o back-end via API REST.

## História de usuário

**HIS-02 – Desenvolver um aplicativo mobile com TotalCross**

> Como Vendedor, no meu smartphone, eu preciso cadastrar clientes de forma off-line para ter mais liberdade e poder fazer o meu trabalho sem depender de um notebook ou conexão com a internet.

### Critérios de aceite

- Armazenar os dados de clientes em um banco local (SQLite)
- Permitir o cadastro, alteração, exclusão e listagem dos clientes
- Não permitir cadastrar mais de um cliente com o mesmo CPF/CNPJ
- Verificar se o CPF/CNPJ do cliente é válido
- Todos os campos do cadastro são obrigatórios, exceto o campo de e-mail
- Permitir a alteração apenas dos dados de contato do cliente (telefone e e-mail)
- Permitir a sincronização dos dados do cliente com o back-end via API REST (novos cadastros, alterações e exclusões)
- A aplicação deve estar coberta com testes unitários

## Tecnologias

- **Java**
- **TotalCross** — framework mobile para Java
- **SQLite** — banco de dados local embarcado
- **JUnit 4** — testes unitários
- **Mockito** — mocking em testes

## Projeto back-end

O back-end que provê a API de sincronização está disponível em:
[github.com/DarkeyS24/Gestor-Project](https://github.com/DarkeyS24/Gestor-Project)

- **Java 25** + **Spring Boot 4.0.5**
- Banco: H2 in-memory
- Build: Maven

### Endpoints disponíveis para sincronização

Base URL: `http://localhost:8080`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/findById/{id}` | Busca cliente por ID |
| POST | `/clientes` | Cadastra novo cliente |
| PUT | `/clientes/{id}` | Atualiza telefone ou e-mail |
| DELETE | `/clientes/{id}` | Remove cliente por ID |

## Estrutura do projeto

```
src/
└── br/com/drky/gestor_app/
    ├── dao/        # Acesso ao banco de dados (ClienteDAO)
    ├── enums/      # TipoCliente (FISICO, JURIDICO)
    └── model/      # Entidade Cliente
```

## Banco de dados

Tabela `tbcliente`:

```sql
CREATE TABLE IF NOT EXISTS tbcliente (
    codigo       INTEGER PRIMARY KEY AUTOINCREMENT,
    nome         TEXT,
    cpfCnpj      TEXT,
    telefone     TEXT,
    email        TEXT NULL,
    tipo         TEXT,
    sincronizado INTEGER CHECK (sincronizado BETWEEN 0 AND 1),
    excluido     INTEGER CHECK (excluido BETWEEN 0 AND 1)
);
```

## Métodos do ClienteDAO

| Método | Descrição |
|---|---|
| `inserirCliente(Cliente)` | Insere um novo cliente |
| `buscaTodosOsClientes()` | Lista clientes ativos (não excluídos) |
| `buscaClientePorId(Integer)` | Busca cliente pelo código |
| `atualizarCliente(Cliente)` | Atualiza dados e marca como não sincronizado |
| `excluirClienteLogico(Integer)` | Marca cliente como excluído (`excluido = 1`) |
| `excluirCliente(Integer)` | Remove o registro fisicamente do banco |
| `atualizarStatusSincronizacaoCliente(Integer)` | Marca cliente como sincronizado |
| `buscaTodosOsClientesNaoSincronizados()` | Lista clientes com `sincronizado = 0` |
| `buscaTodosOsClientesExcluidos()` | Lista clientes com `excluido = 1` |

## Exemplos de uso

### Cadastrar cliente (pessoa física)

```java
Cliente c = new Cliente();
c.setNome("João Silva");
c.setTipo(TipoCliente.FISICO);
c.setCpfCnpj("529.982.247-25");
c.setTelefone("34999990000");
c.setEmail("joao@email.com");
c.setSincronizado(false);
c.setExcluido(false);

String resultado = dao.inserirCliente(c);
// "Cliente inserido com sucesso!!"
```

### Cadastrar cliente (pessoa jurídica)

```java
Cliente c = new Cliente();
c.setNome("Empresa LTDA");
c.setTipo(TipoCliente.JURIDICO);
c.setCpfCnpj("11.222.333/0001-81");
c.setTelefone("34977770000");
c.setEmail("contato@empresa.com");
c.setSincronizado(false);
c.setExcluido(false);

String resultado = dao.inserirCliente(c);
// "Cliente inserido com sucesso!!"
```

### Atualizar cliente

```java
cliente.setTelefone("34988880000");
cliente.setEmail("novo@email.com");

String resultado = dao.atualizarCliente(cliente);
// "Cliente atualizado com sucesso!!"
// sincronizado é redefinido para false automaticamente
```

### Exclusão lógica

```java
String resultado = dao.excluirClienteLogico(id);
// "Cliente Excluido com sucesso!!"
// cliente não aparece em buscaTodosOsClientes()
// cliente aparece em buscaTodosOsClientesExcluidos()
```

### Sincronizar com o back-end

```java
// 1. Busca os registros pendentes de sincronização
List<Cliente> pendentes = dao.buscaTodosOsClientesNaoSincronizados();

// 2. Envia cada um para a API REST do back-end
// POST /clientes, PUT /clientes/{id} ou DELETE /clientes/{id}

// 3. Após envio bem-sucedido, marca como sincronizado
dao.atualizarStatusSincronizacaoCliente(cliente.getCodigo());
```

## Regras de negócio

- O campo `tipo` aceita apenas `FISICO` ou `JURIDICO`.
- O CPF/CNPJ é validado matematicamente antes do cadastro.
- Não é permitido cadastrar dois clientes com o mesmo CPF ou CNPJ.
- O campo `email` é opcional e pode ser `null`.
- Na atualização, apenas `telefone` e `email` podem ser alterados.
- Ao atualizar, o campo `sincronizado` é automaticamente redefinido para `false`.
- A exclusão lógica mantém o registro no banco com `excluido = 1`; a exclusão física o remove permanentemente.
- `buscaTodosOsClientes()` nunca retorna registros com `excluido = 1`.

## Retornos dos métodos

| Operação | Retorno em caso de sucesso |
|---|---|
| `inserirCliente` | `"Cliente inserido com sucesso!!"` |
| `atualizarCliente` | `"Cliente atualizado com sucesso!!"` |
| `excluirClienteLogico` | `"Cliente Excluido com sucesso!!"` |
| `buscaClientePorId` (inexistente) | `null` |
| `buscaTodosOsClientes` (sem registros) | lista vazia |

## Testes

Os testes utilizam **Mockito** para simular o `ClienteDAO` sem banco de dados real, garantindo execução rápida e isolamento entre casos.

### Cobertura atual

| Grupo | Casos |
|---|---|
| `inserirCliente` | com e-mail, sem e-mail, pessoa jurídica |
| `buscaTodosOsClientes` | sem registros, com registros, excluídos ocultos |
| `buscaClientePorId` | ID existente, ID inexistente |
| `atualizarCliente` | telefone e e-mail, e-mail nulo, status de sincronização |
| `excluirClienteLogico` | marcação como excluído, ocultação na listagem ativa |
| `atualizarStatusSincronizacaoCliente` | marcação como sincronizado |
| `buscaTodosOsClientesNaoSincronizados` | filtragem correta por status |
| `excluirCliente` | remoção física confirmada via busca posterior |

### Cobertura dos critérios de aceite

| Critério de aceite | Coberto |
|---|---|
| Armazenar dados em banco local (SQLite) | Sim — `inserirCliente` |
| Cadastro, alteração, exclusão e listagem | Sim — todos os grupos de teste |
| Não permitir CPF/CNPJ duplicado | A validar — regra de negócio no DAO |
| Validar CPF/CNPJ matematicamente | A validar — regra de negócio no service/DAO |
| E-mail opcional | Sim — `inserirCliente_comEmailNulo` |
| Alterar apenas telefone e e-mail | Sim — `atualizarCliente_deveAlterarTelefoneEEmail` |
| Sincronização com back-end | Sim — `buscaTodosOsClientesNaoSincronizados`, `atualizarStatusSincronizacaoCliente` |
| Cobertura com testes unitários | Sim — 14 casos com Mockito |

## Como executar os testes

**Via IDE (Eclipse / IntelliJ):**

Clique com o botão direito em `ClienteDAOTest` → **Run As → JUnit Test**

**Via Maven:**

```bash
mvn test
```

**Via Gradle:**

```bash
./gradlew test
```

## Dependências de teste

**Maven (`pom.xml`):**

```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>4.11.0</version>
    <scope>test</scope>
</dependency>
```

**Gradle (`build.gradle`):**

```groovy
testImplementation 'junit:junit:4.13.2'
testImplementation 'org.mockito:mockito-core:4.11.0'
```
