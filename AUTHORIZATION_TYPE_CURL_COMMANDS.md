# Authorization Type Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `AuthorizationTypeController`.

**Base URL:** `http://localhost:8080/api/authorization-types`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Tipo de Autorización

**Endpoint:** `POST /api/authorization-types`  
**Rol requerido:** `ADMIN`  
**Descripción:** Crea un nuevo tipo de autorización para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/authorization-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Permiso de Vertimiento"
  }'
```

**Campos del request:**
- `name`: Nombre del tipo de autorización (requerido, máximo 100 caracteres)
- `isActive`: Estado activo (opcional, por defecto: true)

**Ejemplo con isActive:**
```bash
curl -X POST "http://localhost:8080/api/authorization-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Permiso de Captación",
    "isActive": true
  }'
```

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Permiso de Vertimiento",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Nota:** Los campos `corporation`, `createdBy`, `createdAt` se asignan automáticamente por el sistema y no deben enviarse en el request.

---

## 2. Actualizar Tipo de Autorización

**Endpoint:** `PUT /api/authorization-types/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Actualiza un tipo de autorización existente de la corporación del usuario autenticado.

```bash
curl -X PUT "http://localhost:8080/api/authorization-types/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Permiso de Vertimiento Actualizado",
    "isActive": false
  }'
```

**Campos del request:**
- `name`: Nombre del tipo de autorización (requerido, máximo 100 caracteres)
- `isActive`: Estado activo (opcional, boolean)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Permiso de Vertimiento Actualizado",
  "isActive": false,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T15:45:00"
}
```

**Nota:** El campo `updatedBy` se asigna automáticamente y `updatedAt` se actualiza automáticamente.

---

## 3. Obtener Tipo de Autorización por ID

**Endpoint:** `GET /api/authorization-types/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene un tipo de autorización específico por su ID (solo de la corporación del usuario autenticado).

```bash
curl -X GET "http://localhost:8080/api/authorization-types/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Permiso de Vertimiento",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Respuesta cuando no existe (404 Not Found):**
```json
```

---

## 4. Obtener Todos los Tipos de Autorización (Paginado)

**Endpoint:** `GET /api/authorization-types`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todos los tipos de autorización de la corporación del usuario autenticado con paginación.

```bash
# Primera página (20 elementos por defecto)
curl -X GET "http://localhost:8080/api/authorization-types?page=0&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Segunda página
curl -X GET "http://localhost:8080/api/authorization-types?page=1&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento por nombre
curl -X GET "http://localhost:8080/api/authorization-types?page=0&size=20&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento por fecha de creación descendente
curl -X GET "http://localhost:8080/api/authorization-types?page=0&size=20&sort=createdAt,desc" \
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
      "name": "Permiso de Vertimiento",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "name": "Permiso de Captación",
      "isActive": true,
      "createdAt": "2024-01-15T11:00:00",
      "updatedAt": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 2
}
```

---

## 5. Obtener Todos los Tipos de Autorización (Sin Paginación)

**Endpoint:** `GET /api/authorization-types/all`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todos los tipos de autorización de la corporación del usuario autenticado sin paginación.

```bash
curl -X GET "http://localhost:8080/api/authorization-types/all" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Permiso de Vertimiento",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  },
  {
    "id": 2,
    "name": "Permiso de Captación",
    "isActive": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": null
  },
  {
    "id": 3,
    "name": "Permiso de Reuso",
    "isActive": false,
    "createdAt": "2024-01-15T12:00:00",
    "updatedAt": "2024-01-15T13:00:00"
  }
]
```

---

## 6. Eliminar Tipo de Autorización

**Endpoint:** `DELETE /api/authorization-types/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Elimina un tipo de autorización de la corporación del usuario autenticado.

```bash
curl -X DELETE "http://localhost:8080/api/authorization-types/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

**Respuesta cuando no existe (404 Not Found):**
```json
```

---

## Códigos de Estado HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Datos inválidos en el request (ej: nombre vacío o muy largo)
- **401 Unauthorized**: Token JWT inválido o ausente
- **403 Forbidden**: Usuario no tiene permisos suficientes o no pertenece a una corporación
- **404 Not Found**: Recurso no encontrado o no pertenece a la corporación del usuario

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer {token}`.

2. **Roles requeridos**:
   - `ADMIN`: Para crear, actualizar, eliminar y obtener tipos de autorización
   - `USER` o `ADMIN`: Para obtener todos los tipos de autorización sin paginación

3. **Validaciones**:
   - El nombre del tipo de autorización es requerido y no puede exceder 100 caracteres
   - El nombre debe ser único dentro de la corporación
   - Solo se pueden modificar/eliminar tipos de autorización de la propia corporación

4. **Asignación automática**:
   - `corporation`: Se asigna automáticamente con la corporación del usuario autenticado
   - `createdBy`: Se asigna automáticamente con el usuario autenticado al crear
   - `updatedBy`: Se asigna automáticamente con el usuario autenticado al actualizar
   - `createdAt`: Se asigna automáticamente con la fecha/hora actual al crear
   - `updatedAt`: Se actualiza automáticamente con la fecha/hora actual al actualizar

5. **Paginación**: El endpoint `GET /api/authorization-types` soporta paginación estándar de Spring Data con parámetros `page`, `size` y `sort`.

6. **Seguridad**: Los usuarios solo pueden acceder a los tipos de autorización de su propia corporación. Intentar acceder a tipos de autorización de otras corporaciones resultará en un error 403 Forbidden.

---

## Ejemplos de Uso Común

### Crear múltiples tipos de autorización

```bash
# Tipo 1
curl -X POST "http://localhost:8080/api/authorization-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name": "Permiso de Vertimiento"}'

# Tipo 2
curl -X POST "http://localhost:8080/api/authorization-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name": "Permiso de Captación"}'

# Tipo 3
curl -X POST "http://localhost:8080/api/authorization-types" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"name": "Permiso de Reuso"}'
```

### Obtener y actualizar un tipo de autorización

```bash
# 1. Obtener el tipo de autorización
curl -X GET "http://localhost:8080/api/authorization-types/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# 2. Actualizar el nombre
curl -X PUT "http://localhost:8080/api/authorization-types/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Nuevo Nombre",
    "isActive": true
  }'
```

### Desactivar un tipo de autorización

```bash
curl -X PUT "http://localhost:8080/api/authorization-types/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Permiso de Vertimiento",
    "isActive": false
  }'
```

