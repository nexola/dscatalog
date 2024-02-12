# DS Catalog

## Sobre o projeto

O projeto foi desenvolvido durante diversos módulos do curso com o objetivo de apresentar e praticar os conceitos e as ferramentas relacionadas com o desenvolvimento de API Rest com a construção de um catálogo de produtos. Disponibiliza uma área administrativa completa para adicionar, alterar e remover os recursos, Contém um controle de acesso dos recursos pelo tipo de perfil do usuário cadastrado no sistema.

Projeto desenvolvido através do curso Java Spring Expert da [DevSuperior](https://devsuperior.club "Site da DevSuperior")

## Modelo conceitual
<img align="center" alt="Modelo Conceitual DS Catalog" width="100%" src="https://raw.githubusercontent.com/DavidMoraes-DEV/assetsReadme/main/dscatalog/modelo-conceitual.png" />

## Tecnologias utilizadas
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- OAuth2 / JWT
- JUnit
- EmailSender

## Executando o projeto
```bash
# Clonando repositório
git clone git@github.com:nexola/dscatalog.git
```
## Coleção e environment do postman
[postman_collection.json](https://github.com/nexola/dscatalog/files/14200857/DSCatalog.postman_collection.json) 

[postman_environment.json](https://github.com/nexola/dscatalog/files/14200888/dscatalog-local.postman_environment.json)
)

# Requisições
## AUTH
### POST /oauth2/token
> Requisição de login

### POST /auth/recover-token
> Solicitação de token para recuperação de senha

### PUT /auth/new-password
> Atualização de senha

## CATEGORY
### GET /categories
> Retorna todas as categorias

### GET /categories/{id}
> Retorna a categoria pelo id

### POST /categories
> Adiciona uma nova categoria (usuário autenticado)

Formato body (POST/PUT):
```json
{
    "name": "Showers"
}
```
### PUT /categories/{id} (usuário autenticado)
> Atualiza a categoria

### DELETE /categories/{id} (usuário autenticado)
> Remove a categoria 

## PRODUCT 
### GET /products
> Retorna todos os produtos paginados, possui ordenação e filtro

### GET /products/{id}
> Retorna o produto pelo id

### POST /products
> Adiciona um novo produto (usuário autenticado)

Formato body (POST/PUT):
```json
{
    "name": "PS5",
    "description": "Lorem Ipsum dolor sit amet consect epitetur",
    "price": 300.0,
    "imgUrl": "imgurl.com.br",
    "date": "2020-07-14T10:00:00Z",
    "categories": [
        {
            "id": 1
        },
        {
            "id": 3
        }
    ]
}
```
### PUT /products/{id} (usuário autenticado)
> Atualiza o produto

### DELETE /product/{id} (usuário autenticado)
> Remove o produto

## USER
### GET /users/me (usuário autenticado)
> Recupera o usuário logado

### GET /users (Somente ADM)
> Recupera todos os usuários

### GET /users/{id} (Somente ADM)
> Recupera o usuário pelo id

### POST /users
> Insere um novo usuário

Formato body:
```json
{
    "firstName": "Vitor",
    "lastName": "Vianna",
    "email": "vitor@gmail.com",
    "password": "123456"
}
```
### PUT /users/{id} (Somente ADM)
> Atualiza o usuário

Formato body:
```json
{
    "firstName": "João",
    "lastName": "Soares",
    "email": "joao@gmail.com",
    "password": "joaojjj2",
    "roles": [
        {
            "id": 1
        }
    ]
}
```
### DELETE /users/{id} (Somente ADM)
> Remove um usuário

# Autor

Vitor Vianna

https://www.linkedin.com/in/vitor-vianna-a53075215/

