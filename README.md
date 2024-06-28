# 📊 FinanceApp

FinanceApp é uma aplicação de gerenciamento financeiro pessoal desenvolvida em Java, utilizando Spring Boot. A aplicação oferece uma API RESTful para gerenciar contas de usuários, transações (movimentações) e categorias. Utiliza PostgreSQL como banco de dados e JWT para autenticação e autorização.


## ✨ Features

- 📝 Registro e autenticação de usuários
- 💼 Gerenciamento de contas
- 💸 Gerenciamento de transações (movimentações)
- 🗂️ Gerenciamento de categorias
- 🔒 Controle de acesso baseado em roles
- 🔑 Autenticação JWT
- 📜 Documentação Swagger

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.3.1**
- **Maven**
- **PostgreSQL**
- **JWT**
- **Docker**
- **Swagger**

## 🛠 Pré-requisitos

Antes de começar, você precisará ter as seguintes ferramentas instaladas:

- [Java 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Docker](https://docs.docker.com/get-docker/)


## 📝 Instalação

Siga os passos abaixo para configurar o projeto localmente:

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/Jaoow/finance-app.git
   ```

2. **Navegue até o diretório do projeto:**

   ```bash
   cd finance-app
   ```

3. **Construa o projeto usando Maven:**

   ```bash
   mvn clean install
   ```

4. **Inicie o banco de dados PostgreSQL usando Docker:**

   ```bash
   docker-compose up -d
   ```

5. **Execute a aplicação:**

   ```bash
   mvn spring-boot:run
   ```

6. **A aplicação estará acessível em** http://localhost:8080/

## 📖 Documentação da API

A documentação da API está disponível em:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 🌐 Diagrama de Funcionamento

[![](https://mermaid.ink/img/pako:eNqtlFFv2jAQx7_KyVKlTiriPQ-VYFmnTkOlC9te8uI5V7BK7Mx2qBDiu--c2CEJsO2hPGA797vz3__Ed2BCF8gSZvF3jUpgKvna8DJXQD8n3RbhQSpOkVlVwQSyvXVYwtMOzU7iW65a8uYGZsJpY4GrAj7qstIKlbNttOLGSSErrhx8t2iA22Y8j86Wjz7Y25KeXMBqt_FcM2ZeisALlBC6ptGDYXqVXegdltjC3fwqnc49t9TWrQ1mz18h5Y7_4hZ7fjQH9QKpkhTcSR1iPjC5v6eDJbB8ylYw5UQ1fwFFuBUGC7_iW_uhTSPeZxGVwA--lYXnTi76wCSW_YauNgq-_FzBSr-iOlUgwud4xNI7sghvkjzskd0BFlzxNUbr7Ln4z5-89hCG21job3opfEGu371BBplt4QQe0IlNsHOgJayITecJPNdo9iMinU_6lYItkWne2rBUVDTYrc9dt3BEj22MH9W_fJwewuyxOE7LmPTu7kY50d54QUYy43Lg8YhpTT4VDC53VM--rlzUdrbt_3k9xtkdK9GUXBbUyg4-PWd0m0rMWULTgpvXnOXqSBxdM53tlWCJMzXeMaPr9YYlL3TRaFVX3sjQBLunWEjqbYu2UzYN8_gHAoC3Cg?type=png)](https://mermaid.live/edit#pako:eNqtlFFv2jAQx7_KyVKlTiriPQ-VYFmnTkOlC9te8uI5V7BK7Mx2qBDiu--c2CEJsO2hPGA797vz3__Ed2BCF8gSZvF3jUpgKvna8DJXQD8n3RbhQSpOkVlVwQSyvXVYwtMOzU7iW65a8uYGZsJpY4GrAj7qstIKlbNttOLGSSErrhx8t2iA22Y8j86Wjz7Y25KeXMBqt_FcM2ZeisALlBC6ptGDYXqVXegdltjC3fwqnc49t9TWrQ1mz18h5Y7_4hZ7fjQH9QKpkhTcSR1iPjC5v6eDJbB8ylYw5UQ1fwFFuBUGC7_iW_uhTSPeZxGVwA--lYXnTi76wCSW_YauNgq-_FzBSr-iOlUgwud4xNI7sghvkjzskd0BFlzxNUbr7Ln4z5-89hCG21job3opfEGu371BBplt4QQe0IlNsHOgJayITecJPNdo9iMinU_6lYItkWne2rBUVDTYrc9dt3BEj22MH9W_fJwewuyxOE7LmPTu7kY50d54QUYy43Lg8YhpTT4VDC53VM--rlzUdrbt_3k9xtkdK9GUXBbUyg4-PWd0m0rMWULTgpvXnOXqSBxdM53tlWCJMzXeMaPr9YYlL3TRaFVX3sjQBLunWEjqbYu2UzYN8_gHAoC3Cg)

## 🤝 Contribuição

Se você deseja contribuir para este projeto, siga as etapas abaixo:

1. **Fork o projeto**
2. **Crie uma branch para sua feature** (`git checkout -b feature/sua-feature`)
3. **Commit suas mudanças** (`git commit -m 'Adiciona sua feature'`)
4. **Push para a branch** (`git push origin feature/sua-feature`)
5. **Abra um Pull Request**

## 📜 Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

Se você tiver alguma dúvida ou problema, sinta-se à vontade para [abrir uma issue](https://github.com/Jaoow/finance-app/issues).
