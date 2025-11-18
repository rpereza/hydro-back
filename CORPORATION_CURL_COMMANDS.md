# Corporation Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `CorporationController`.

**Base URL:** `http://localhost:8080/api/corporations`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Corporación

**Endpoint:** `POST /api/corporations`  
**Rol requerido:** `ADMIN`  
**Descripción:** Crea una nueva corporación para el usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/corporations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Corporación Ejemplo S.A.",
    "code": "CORP001",
    "description": "Descripción de la corporación de ejemplo"
  }'
```

**Campos del request:**
- `name`: Nombre de la corporación (requerido, máximo 200 caracteres)
- `code`: Código único de la corporación (requerido, máximo 50 caracteres)
- `description`: Descripción opcional (máximo 500 caracteres)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Corporación Ejemplo S.A.",
  "code": "CORP001",
  "description": "Descripción de la corporación de ejemplo",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "owner": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "enabled": true,
    "roles": ["ADMIN"],
    "owner": true
  },
  "userCount": 1
}
```

---

## 2. Obtener Mi Corporación

**Endpoint:** `GET /api/corporations/my-corporation`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene la corporación del usuario autenticado.

```bash
curl -X GET "http://localhost:8080/api/corporations/my-corporation" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Corporación Ejemplo S.A.",
  "code": "CORP001",
  "description": "Descripción de la corporación de ejemplo",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "owner": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "enabled": true,
    "roles": ["ADMIN"],
    "owner": true
  },
  "userCount": 5
}
```

---

## 3. Obtener Corporación por ID

**Endpoint:** `GET /api/corporations/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene una corporación específica por su ID.

```bash
curl -X GET "http://localhost:8080/api/corporations/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Corporación Ejemplo S.A.",
  "code": "CORP001",
  "description": "Descripción de la corporación de ejemplo",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "owner": {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "enabled": true,
    "roles": ["ADMIN"],
    "owner": true
  },
  "userCount": 5
}
```

---

## 4. Obtener Todas las Corporaciones (Paginado)

**Endpoint:** `GET /api/corporations`  
**Rol requerido:** `SUPER_ADMIN`  
**Descripción:** Obtiene todas las corporaciones con paginación.

```bash
# Primera página (10 elementos por defecto)
curl -X GET "http://localhost:8080/api/corporations?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Segunda página
curl -X GET "http://localhost:8080/api/corporations?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento
curl -X GET "http://localhost:8080/api/corporations?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `page`: Número de página (default: 0)
- `size`: Tamaño de la página (default: 20)
- `sort`: Campo y dirección de ordenamiento (ej: `name,asc` o `createdAt,desc`)

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Corporación Ejemplo S.A.",
      "code": "CORP001",
      "description": "Descripción de la corporación de ejemplo",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null,
      "owner": {...},
      "userCount": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1
}
```

---

## 5. Actualizar Mi Corporación

**Endpoint:** `PUT /api/corporations/my-corporation`  
**Rol requerido:** `ADMIN`  
**Descripción:** Actualiza la información de la corporación del usuario autenticado.

```bash
curl -X PUT "http://localhost:8080/api/corporations/my-corporation" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Corporación Actualizada S.A.",
    "code": "CORP001",
    "description": "Nueva descripción actualizada"
  }'
```

**Campos del request:**
- `name`: Nombre de la corporación (requerido, máximo 200 caracteres)
- `code`: Código único de la corporación (requerido, máximo 50 caracteres)
- `description`: Descripción opcional (máximo 500 caracteres)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Corporación Actualizada S.A.",
  "code": "CORP001",
  "description": "Nueva descripción actualizada",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T15:45:00",
  "owner": {...},
  "userCount": 5
}
```

---

## 6. Invitar Usuario a la Corporación

**Endpoint:** `POST /api/corporations/users/invite`  
**Rol requerido:** `ADMIN`  
**Descripción:** Invita a un usuario a la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/corporations/users/invite" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "email": "nuevo.usuario@example.com",
    "username": "nuevo_usuario",
    "password": "password123",
    "firstName": "Nuevo",
    "lastName": "Usuario"
  }'
```

**Campos del request:**
- `email`: Email del usuario (requerido, formato válido, máximo 255 caracteres)
- `username`: Nombre de usuario (requerido, entre 3 y 50 caracteres)
- `password`: Contraseña (requerido, mínimo 6 caracteres)
- `firstName`: Nombre (opcional, máximo 100 caracteres)
- `lastName`: Apellido (opcional, máximo 100 caracteres)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 2,
  "username": "nuevo_usuario",
  "email": "nuevo.usuario@example.com",
  "firstName": "Nuevo",
  "lastName": "Usuario",
  "enabled": true,
  "roles": ["USER"],
  "owner": false,
  "createdAt": "2024-01-15T16:00:00"
}
```

---

## 7. Obtener Usuarios de Mi Corporación

**Endpoint:** `GET /api/corporations/users`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todos los usuarios de la corporación del usuario autenticado.

```bash
curl -X GET "http://localhost:8080/api/corporations/users" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "enabled": true,
    "roles": ["ADMIN"],
    "owner": true,
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "username": "nuevo_usuario",
    "email": "nuevo.usuario@example.com",
    "firstName": "Nuevo",
    "lastName": "Usuario",
    "enabled": true,
    "roles": ["USER"],
    "owner": false,
    "createdAt": "2024-01-15T16:00:00"
  }
]
```

---

## 8. Obtener Usuarios de una Corporación Específica

**Endpoint:** `GET /api/corporations/{corporationId}/users`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todos los usuarios de una corporación específica.

```bash
curl -X GET "http://localhost:8080/api/corporations/1/users" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@example.com",
    "firstName": "Admin",
    "lastName": "User",
    "enabled": true,
    "roles": ["ADMIN"],
    "owner": true,
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

---

## 9. Remover Usuario de Mi Corporación

**Endpoint:** `DELETE /api/corporations/users/{userId}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Remueve un usuario de la corporación del usuario autenticado.

```bash
curl -X DELETE "http://localhost:8080/api/corporations/users/2" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

---

## 10. Remover Usuario de una Corporación Específica

**Endpoint:** `DELETE /api/corporations/{corporationId}/users/{userId}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Remueve un usuario de una corporación específica.

```bash
curl -X DELETE "http://localhost:8080/api/corporations/1/users/2" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

---

## 11. Verificar si Puede Crear Corporación

**Endpoint:** `GET /api/corporations/can-create`  
**Rol requerido:** `ADMIN`  
**Descripción:** Verifica si el usuario autenticado puede crear una corporación.

```bash
curl -X GET "http://localhost:8080/api/corporations/can-create" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

o

```json
false
```

---

## 12. Verificar si Puede Invitar Usuarios

**Endpoint:** `GET /api/corporations/can-invite`  
**Rol requerido:** `ADMIN`  
**Descripción:** Verifica si el usuario autenticado puede invitar usuarios a su corporación.

```bash
curl -X GET "http://localhost:8080/api/corporations/can-invite" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

o

```json
false
```

---

## 13. Obtener Estadísticas de Mi Corporación

**Endpoint:** `GET /api/corporations/my-corporation/stats`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene estadísticas de la corporación del usuario autenticado.

```bash
curl -X GET "http://localhost:8080/api/corporations/my-corporation/stats" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "corporationId": 1,
  "corporationName": "Corporación Ejemplo S.A.",
  "totalUsers": 5,
  "activeUsers": 4,
  "totalDischarges": 0,
  "totalMonitorings": 0,
  "totalInvoices": 0,
  "createdAt": "2024-01-15T10:30:00"
}
```

---

## 14. Buscar Corporaciones por Nombre

**Endpoint:** `GET /api/corporations/search?name={name}`  
**Rol requerido:** `SUPER_ADMIN`  
**Descripción:** Busca corporaciones por nombre (búsqueda parcial, case-insensitive).

```bash
curl -X GET "http://localhost:8080/api/corporations/search?name=Ejemplo" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `name`: Nombre o parte del nombre a buscar (requerido)

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Corporación Ejemplo S.A.",
    "code": "CORP001",
    "description": "Descripción de la corporación de ejemplo",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null,
    "owner": {...},
    "userCount": 5
  }
]
```

---

## 15. Verificar si es Propietario de la Corporación

**Endpoint:** `GET /api/corporations/is-owner`  
**Rol requerido:** `ADMIN`  
**Descripción:** Verifica si el usuario autenticado es propietario de su corporación.

```bash
curl -X GET "http://localhost:8080/api/corporations/is-owner" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

o

```json
false
```

---

## Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Datos inválidos en el request
- **401 Unauthorized**: Token JWT inválido o ausente
- **403 Forbidden**: Usuario no tiene permisos suficientes
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (ej: código de corporación ya existe)

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer {token}`.

2. **Roles requeridos**:
   - `ADMIN`: Para la mayoría de operaciones relacionadas con la propia corporación
   - `SUPER_ADMIN`: Para operaciones que requieren acceso a todas las corporaciones

3. **Validaciones**:
   - El código de corporación debe ser único
   - El email y username deben ser únicos en el sistema
   - No se puede remover al propietario de una corporación

4. **Paginación**: El endpoint de obtener todas las corporaciones soporta paginación estándar de Spring Data.

5. **Búsqueda**: La búsqueda por nombre es case-insensitive y busca coincidencias parciales.

