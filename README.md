# FinTrack API

FinTrack é uma API REST desenvolvida em **Spring Boot** para controle financeiro pessoal ou colaborativo.  
O sistema permite gerenciar **receitas, despesas, categorias, metas financeiras e métodos de pagamento**, além de trabalhar com **múltiplos workspaces isolados**, que podem ser compartilhados com outros usuários por convite.

O projeto foi pensado com foco em **boas práticas de backend**, segurança com **JWT**, testes automatizados e suporte a **Docker**.

---

## ✨ Funcionalidades

- Autenticação e autorização com **JWT** e **roles**
- Criação e gerenciamento de **workspaces**
- Compartilhamento de workspaces via **convites**
- CRUD de:
  - Categorias
  - Métodos de pagamento
  - Metas financeiras
  - Receitas
  - Despesas
- Isolamento total de dados por workspace
- Testes automatizados com banco em memória (**H2**)
- Ambiente pronto para execução com **Docker**

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Security
- Spring Data JPA
- JWT (io.jsonwebtoken)
- PostgreSQL (produção/desenvolvimento)
- H2 (testes)
- MapStruct
- Lombok
- Maven
- Docker / Docker Compose
- JUnit + Mockito
- JaCoCo (cobertura de testes)

---

## 📁 Arquitetura

O projeto segue uma arquitetura em camadas, separando bem as responsabilidades:

- **Controller**: endpoints REST
- **Service**: regras de negócio
- **Repository**: acesso a dados (JPA)
- **DTOs / Mappers**: conversão de dados com MapStruct
- **Security**: autenticação, autorização e filtros JWT
- **Config**: configurações globais da aplicação

---

## 🔐 Segurança

- Autenticação baseada em **JWT**
- Tokens de acesso e refresh token
- Sessão stateless
- Filtro JWT aplicado a todas as rotas protegidas
- Apenas endpoints de autenticação são públicos

---

## 🧪 Testes
- Testes automatizados com Spring Boot Test
- Banco de dados H2 em memória
- Mockito para mocks
- Cobertura de testes gerada com JaCoCo

Para rodar os testes:
```bash
mvn test
```
Para gerar o relatório de cobertura:
```bash
mvn verify
```
O relatório será gerado em:
```bash
target/site/jacoco/index.html
```

---

## 🐘 Banco de dados
### Produção / Desenvolvimento
- PostgreSQL

### Testes
- H2 (in-memory)

As configurações de banco são controladas por profiles:
- default → PostgreSQL
- test → H2
- production → PostgreSQL com logs reduzidos

---

## 🐳 Docker

O projeto possui suporte a Docker para facilitar o ambiente de desenvolvimento.

Subir o banco de dados PostgreSQL

```bash
docker-compose up -d
```

Isso irá subir um container PostgreSQL com as seguintes configurações:
- Database: `fintrack`
- User: `root`
- Password: `root`
- Porta: `5432`

---

## ▶️ Como executar o projeto

### Pré-requisitos
- Java 21
- Maven
- Docker (opcional, mas recomendado)

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/VictorFerreiraFranco/project-java-spring-boot-fintrack.git
```

2. Acesse o diretório do projeto:
```bash
docker-compose up -d
```

2. Acesse o diretório do projeto:
```bash
cd project-java-spring-boot-fintrack
```

3. (Opcional) Suba o PostgreSQL com Docker:
```bash
docker-compose up -d
```

4. Execute a aplicação:
```bash
mvn spring-boot:run
```

A API ficará disponível em: `http://localhost:8080`

--- 

## 📌 Observações
- Este projeto é backend only (não possui frontend).
- Ideal para integração com aplicações web ou mobile.
- Estruturado para facilitar manutenção, testes e evolução futura.


---

## 👤 Autor
### Victor Ferreira Franco
Projeto desenvolvido com foco em aprendizado avançado de Spring Boot, segurança, testes e arquitetura de APIs REST.
