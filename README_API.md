# API Documentation for AppsMovilesBackend

This document provides information on how to access the API endpoints for the database tables in the AppsMovilesBackend project.

## Base URL

The server runs on port 8086. If you're running the server locally, the base URL will be:

```
http://localhost:8086
```

If you're accessing the server from another device on the same network, replace `localhost` with the IP address of the server:

```
http://<server-ip-address>:8086
```

## Available Endpoints

### Users (Usuarios)

- **GET /usuarios** - Retrieve all users
  ```
  http://localhost:8086/usuarios
  ```

- **POST /usuarios** - Create a new user
  ```
  http://localhost:8086/usuarios
  ```

  Body (JSON):
  ```json
  {
    "nombre": "User Name",
    "correo": "user@example.com",
    "tipo": "cliente",
    "userName": "username123",
    "contrasena": "password123",
    "fotoPerfil": "url_to_photo"
  }
  ```

### Locations (Locales)

- **GET /locales** - Retrieve all locations
  ```
  http://localhost:8086/locales
  ```

- **POST /locales** - Create a new location
  ```
  http://localhost:8086/locales
  ```

  Body (JSON):
  ```json
  {
    "nombre": "Location Name",
    "ubicacion": "Location Address",
    "horario": "Opening Hours",
    "imagenUrl": "url_to_image",
    "abierto": "true",
    "telefono": "123456789",
    "metodosPago": "Credit Card, Cash"
  }
  ```

### Promotions (Promociones)

- **GET /promociones** - Retrieve all promotions
  ```
  http://localhost:8086/promociones
  ```

- **POST /promociones** - Create a new promotion
  ```
  http://localhost:8086/promociones
  ```

  Body (JSON):
  ```json
  {
    "titulo": "Promotion Title",
    "descripcion": "Promotion Description",
    "precio": "10.99",
    "colorHex": "#FF5733",
    "localId": "1"
  }
  ```

### Reservations (Reservas)

- **GET /reservas** - Retrieve all reservations
  ```
  http://localhost:8086/reservas
  ```

- **POST /reservas** - Create a new reservation
  ```
  http://localhost:8086/reservas
  ```

  Body (JSON):
  ```json
  {
    "localId": "1",
    "usuarioId": "1",
    "fecha": "2023-06-15",
    "hora": "18:00",
    "estado": "pendiente",
    "mesa": "A1"
  }
  ```

## Static Resources

- **GET /images/{filename}** - Retrieve an image
  ```
  http://localhost:8086/images/foto1.png
  ```

## Testing the API

You can use tools like Postman, cURL, or any HTTP client to test these endpoints.

Example cURL command to get all users:
```
curl -X GET http://localhost:8086/usuarios
```

Example cURL command to create a new user:
```
curl -X POST http://localhost:8086/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"John Doe","correo":"john@example.com","tipo":"cliente","userName":"johndoe","contrasena":"password123"}'
```
