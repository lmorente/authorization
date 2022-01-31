# Práctica 5: Autenticación y autorización - Node y MongoDB

## Ejecución

```sh
$ docker run --name mongo-db  -p 27017:27017 -d mongo:latest
$ npm install
$ node src/server.js
```
## API URL

```sh
https://localhost:3000/api/auth/signin
https://localhost:3000/api/auth/signup
https://localhost:3000/books/<resource>
```

## Uso de la API
Se proporciona una colección Postman para interactuar con la API.
La baseURL tiene que configurarse para localhost:3000
La base de datos se inicializa con datos de ejemplo