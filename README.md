# Documentación Técnica: Microservicio de Login (ms-login)

## Descripción General
El microservicio `ms-login` (BackMac) gestiona la autenticación y la información base de los usuarios del sistema.

## Arquitectura y Tecnologías
- **Lenguaje:** Java 21
- **Framework:** Spring Boot
- **Persistencia:** Spring Data JPA, PostgreSQL
- **Seguridad:** Spring Security con OAuth2 (Azure AD)
- **Contenerización:** Docker

## Construcción y Despliegue
Para correr este servicio contenerizado:
```bash
docker build -t img-ms-login .
docker run -d -p 8080:8080 --name ms-login img-ms-login
```

## API
Endpoint principal:
- `POST /api/v1/auth/login`: Sincroniza al usuario utilizando el token JWT de Azure AD enviado en el header de autorización.
