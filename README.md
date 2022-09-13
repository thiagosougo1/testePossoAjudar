# My Music 2022

>API responsável por gerenciar as musicas favoritas do usuário.

---
## 🏆 Meta

Desenvolvido por OS SIMPSONS (GRUPO 4)


---
# 🖥️ Índice

- [Escopo](#-Escopo)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Como rodar na sua máquina](#-como-baixar-o-projeto)

---

## Escopo

● Permitir o usuário buscar novas músicas:
1. O serviço deve validar se o usuário informou ao menos 2 caracteres, retornando um HTTP 400
   caso a consulta tenha menos de 2 caracteres.
2. A busca deve ser realizada através do nome de artista e nome da música.
3. A busca por música não deve ser case sensitive.
4. A busca deve retornar valores contendo o filtro, não necessitando de ser informado o nome
   completo de música ou artista.
5. O retorno deve estar ordenado pelo nome do artista e depois pelo nome da música.

● Permitir adicionar as músicas favoritas do usuário na playlist:
1. Deve receber um request contendo o identificador da música e o identificador da playlist.
2. Deve validar se o identificador da música e o identificador da playlist existem.

● Permitir o usuário remover músicas de sua playlist:
3. Deve receber um request contendo o identificador da música e o identificador da playlist.
4. Deve validar se o identificador da música e o identificador da playlist existem.

Todos os endpoints devem possuir uma camada de segurança para proteger o dominio de dados. Para implementar
essa segurança os endpoints criados devem exigir que as requisições recebidas possuam o header "authorization",
contendo um token válido para responder a requisição. Para realizar a criação e geração do token, utilizar o serviço
disponbilizado junto com estrutura do projeto: token-provider-0.0.1-SNAPSHOT.jar.




## 🚀 Tecnologias utilizadas

O projeto foi desenvolvido utilizando:

- JDK 11
- Maven
- Spring Boot
- JPA & Hibernate
- Swagger
- Intellij Idea
- Postman
- CleanArch
- JaCoCo
- Microservice
- PITest
- Heroku
- Circle CI

---
Acesse a arquitetura do projeto por este [LINK](https://excalidraw.com/#json=EwD3zImwi9fLVGrjz8Unw,CvPYR7BfIL-miljjW-bzkA
)

---

## 🧑‍💻 Como rodar na sua máquina

- Clone o projeto que consta nesse repositório No terminal rode o seguinte comando:
```sh
mvn clean install
```

- Rode a classe main do projeto SummitBootcampApplication


- Para testar os endpoints utilize a documentação como base.
  Acesse [Swagger](http://localhost:8081/api/swagger-ui.html)


### token-provider

Baixar o .jar a partir do link a seguir para compilar a aplicação de token

https://drive.google.com/file/d/17weBxIxAE0P1yimKTqBOfocIFi1VMkZQ/view?usp=sharing

Para criação de token válidos utilizar o endpoint a seguir:

```
ENDPOINT: /api/v1/token
METODO: POST
BODY: 
{ 
    "data": {
        "name": "joao"
    }
}
RETORNO: 201 Created
{
    "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="
}
```

Para validação de token utilizar o endpoint a seguir:


```
ENDPOINT: /api/v1/token/authorizer
METODO: POST
BODY: 
{ 
    "data": {
        "name": "joao",
        "token": "ZIIKXbvDLcs30v/7nzGxxwRHW6AHBEp94vEtSCFGZqK8ojfKYv39J92PI5Tw9EIHZLhtGJUaY2KZHwysFlfWvA=="
    }
}
RETORNO: 201 Created
{
    "ok"
}
```

# Banco de dados

Para testar o banco de dados utilize os seguintes passos:

Para auxiliar o desenvolvimento do API, a estrutura inicial conta com uma base de dados pré-definida e populada

Modelagem:
<div align="center"><img src="https://i.imgur.com/yfMGrur.png" title="source:modelagem imgur" /></div>


Atenção:
Os campos Id que utilizam GUID mapear como string devido à complexidade na compatibilidade com o UUID nativo do Java.

Dica:
Não é necessário, porém é possível utilizar uma ferramenta para abrir e visualizar o arquivo MyMusic.db de maneira mais fácil, como:

https://sqlitestudio.pl

# Testes

Para testar as as classes criadas foram realizados os seguintes testes:

<div align="center"><img src="images/Testes.png" title="Testes realizados" alt="Figura com os testes"/></div>

# Jacoco

Para fazer a cobertura de código de foi de 70%, foi utilizado o Java code coverage tools (JaCoCo)

<div align="center"><img src="images/Jacoco.png" title="Cobertura realizada pelo JaCoCo" alt="Figura com a cobertura"/></div>

# PITest

Rode no console o comando para executar o PITest
```
mvn org.pitest:pitest-maven:mutationCoverage
```

<div align="center"><img src="images/Mutation.png" title="Teste de mutação realizado" alt="Figura com o resultado do PITest"/></div>



