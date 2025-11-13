# Discharge User Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `DischargeUserController`.

**Base URL:** `http://localhost:8080/api/discharge-users`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Usuario de Descarga

**Endpoint:** `POST /api/discharge-users`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Crea un nuevo usuario de descarga para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/discharge-users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez",
    "email": "contacto@empresa.com",
    "phone": "3001234567",
    "address": "Calle 123 #45-67",
    "fileNumber": "FILE001",
    "municipality": {
      "id": 1
    },
    "economicActivity": {
      "id": 1
    },
    "authorizationType": {
      "id": 1
    },
    "hasPtar": false,
    "isPublicServiceCompany": false,
    "isActive": true
  }'
```

**Campos del request:**
- `companyName`: Nombre de la empresa (requerido, máximo 200 caracteres)
- `code`: Código del usuario de descarga (requerido, máximo 8 caracteres, único dentro de la corporación)
- `documentNumber`: Número de documento (requerido, máximo 50 caracteres, único dentro de la corporación)
- `documentType`: Tipo de documento - `NIT`, `CC` (Cédula de Ciudadanía), `CE` (Cédula de Extranjería) (requerido)
- `contactPerson`: Nombre de la persona de contacto (requerido, máximo 150 caracteres)
- `email`: Email principal (requerido, formato válido, máximo 255 caracteres)
- `phone`: Teléfono principal (requerido, máximo 10 caracteres)
- `alternativeEmail`: Email alternativo (opcional, formato válido, máximo 255 caracteres)
- `alternativePhone`: Teléfono alternativo (opcional, máximo 10 caracteres)
- `address`: Dirección (requerido, máximo 200 caracteres)
- `fileNumber`: Número de expediente (requerido, máximo 20 caracteres)
- `municipality`: Objeto con `id` del municipio (requerido)
- `economicActivity`: Objeto con `id` de la actividad económica (requerido)
- `authorizationType`: Objeto con `id` del tipo de autorización (requerido)
- `hasPtar`: Indica si tiene PTAR (opcional, por defecto: false)
- `efficiencyPercentage`: Porcentaje de eficiencia (opcional, decimal)
- `isPublicServiceCompany`: Indica si es empresa de servicios públicos (opcional, por defecto: false)
- `isActive`: Estado activo (opcional, por defecto: true)

**Ejemplo con todos los campos opcionales:**
```bash
curl -X POST "http://localhost:8080/api/discharge-users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Empresa Completa S.A.",
    "code": "EMP002",
    "documentNumber": "800987654-3",
    "documentType": "NIT",
    "contactPerson": "María González",
    "email": "info@empresa.com",
    "phone": "3109876543",
    "alternativeEmail": "alternativo@empresa.com",
    "alternativePhone": "3201234567",
    "address": "Avenida Principal 456",
    "fileNumber": "FILE002",
    "municipality": {
      "id": 2
    },
    "economicActivity": {
      "id": 2
    },
    "authorizationType": {
      "id": 2
    },
    "hasPtar": true,
    "efficiencyPercentage": 85.50,
    "isPublicServiceCompany": true,
    "isActive": true
  }'
```

**Ejemplo con Cédula de Ciudadanía:**
```bash
curl -X POST "http://localhost:8080/api/discharge-users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Persona Natural",
    "code": "PN001",
    "documentNumber": "1234567890",
    "documentType": "CC",
    "contactPerson": "Carlos Rodríguez",
    "email": "carlos@example.com",
    "phone": "3151234567",
    "address": "Carrera 10 #20-30",
    "fileNumber": "FILE003",
    "municipality": {
      "id": 1
    },
    "economicActivity": {
      "id": 1
    },
    "authorizationType": {
      "id": 1
    }
  }'
```

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "companyName": "Empresa Ejemplo S.A.",
  "code": "EMP001",
  "documentNumber": "900123456-7",
  "documentType": "NIT",
  "contactPerson": "Juan Pérez",
  "email": "contacto@empresa.com",
  "phone": "3001234567",
  "alternativeEmail": null,
  "alternativePhone": null,
  "address": "Calle 123 #45-67",
  "fileNumber": "FILE001",
  "hasPtar": false,
  "efficiencyPercentage": null,
  "isPublicServiceCompany": false,
  "isActive": true,
  "municipality": {
    "id": 1,
    "name": "Bogotá",
    "code": "001"
  },
  "economicActivity": {
    "id": 1,
    "name": "Comercio",
    "code": "1234"
  },
  "authorizationType": {
    "id": 1,
    "name": "Permiso de Vertimiento"
  },
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Nota:** Los campos `corporation`, `createdBy`, `createdAt` se asignan automáticamente por el sistema y no deben enviarse en el request.

---

## 2. Actualizar Usuario de Descarga

**Endpoint:** `PUT /api/discharge-users/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Actualiza un usuario de descarga existente de la corporación del usuario autenticado.

```bash
curl -X PUT "http://localhost:8080/api/discharge-users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Empresa Ejemplo Actualizada S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez Actualizado",
    "email": "nuevoemail@empresa.com",
    "phone": "3001234567",
    "alternativeEmail": "alternativo@empresa.com",
    "alternativePhone": "3201234567",
    "address": "Nueva Dirección 789",
    "fileNumber": "FILE001",
    "municipality": {
      "id": 2
    },
    "economicActivity": {
      "id": 2
    },
    "authorizationType": {
      "id": 2
    },
    "hasPtar": true,
    "efficiencyPercentage": 90.00,
    "isPublicServiceCompany": false,
    "isActive": true
  }'
```

**Campos del request:**
- Todos los campos son los mismos que en la creación, excepto que `id` se toma del path parameter.

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "companyName": "Empresa Ejemplo Actualizada S.A.",
  "code": "EMP001",
  "documentNumber": "900123456-7",
  "documentType": "NIT",
  "contactPerson": "Juan Pérez Actualizado",
  "email": "nuevoemail@empresa.com",
  "phone": "3001234567",
  "alternativeEmail": "alternativo@empresa.com",
  "alternativePhone": "3201234567",
  "address": "Nueva Dirección 789",
  "fileNumber": "FILE001",
  "hasPtar": true,
  "efficiencyPercentage": 90.00,
  "isPublicServiceCompany": false,
  "isActive": true,
  "municipality": {
    "id": 2,
    "name": "Medellín",
    "code": "002"
  },
  "economicActivity": {
    "id": 2,
    "name": "Industria",
    "code": "5678"
  },
  "authorizationType": {
    "id": 2,
    "name": "Permiso de Captación"
  },
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T15:45:00"
}
```

**Nota:** El campo `updatedBy` se asigna automáticamente y `updatedAt` se actualiza automáticamente.

---

## 3. Obtener Usuario de Descarga por ID

**Endpoint:** `GET /api/discharge-users/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene un usuario de descarga específico por su ID (solo de la corporación del usuario autenticado).

```bash
curl -X GET "http://localhost:8080/api/discharge-users/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "companyName": "Empresa Ejemplo S.A.",
  "code": "EMP001",
  "documentNumber": "900123456-7",
  "documentType": "NIT",
  "contactPerson": "Juan Pérez",
  "email": "contacto@empresa.com",
  "phone": "3001234567",
  "alternativeEmail": null,
  "alternativePhone": null,
  "address": "Calle 123 #45-67",
  "fileNumber": "FILE001",
  "hasPtar": false,
  "efficiencyPercentage": null,
  "isPublicServiceCompany": false,
  "isActive": true,
  "municipality": {
    "id": 1,
    "name": "Bogotá",
    "code": "001"
  },
  "economicActivity": {
    "id": 1,
    "name": "Comercio",
    "code": "1234"
  },
  "authorizationType": {
    "id": 1,
    "name": "Permiso de Vertimiento"
  },
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Respuesta cuando no existe (404 Not Found):**
```json
```

---

## 4. Obtener Todos los Usuarios de Descarga (Paginado)

**Endpoint:** `GET /api/discharge-users`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todos los usuarios de descarga de la corporación del usuario autenticado con paginación.

```bash
# Primera página (20 elementos por defecto)
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Segunda página
curl -X GET "http://localhost:8080/api/discharge-users?page=1&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento por nombre de empresa
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20&sort=companyName,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento por código
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20&sort=code,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento por fecha de creación descendente
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Múltiples criterios de ordenamiento
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20&sort=isActive,desc&sort=companyName,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `page`: Número de página (default: 0, índice base 0)
- `size`: Tamaño de la página (default: 20)
- `sort`: Campo y dirección de ordenamiento (ej: `companyName,asc` o `createdAt,desc`). Se puede especificar múltiples veces para ordenamiento múltiple.

**Campos ordenables:**
- `companyName`: Nombre de la empresa
- `code`: Código
- `documentNumber`: Número de documento
- `contactPerson`: Persona de contacto
- `email`: Email
- `phone`: Teléfono
- `address`: Dirección
- `fileNumber`: Número de expediente
- `isActive`: Estado activo
- `hasPtar`: Tiene PTAR
- `isPublicServiceCompany`: Es empresa de servicios públicos
- `createdAt`: Fecha de creación
- `updatedAt`: Fecha de actualización

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "companyName": "Empresa Ejemplo S.A.",
      "code": "EMP001",
      "documentNumber": "900123456-7",
      "documentType": "NIT",
      "contactPerson": "Juan Pérez",
      "email": "contacto@empresa.com",
      "phone": "3001234567",
      "alternativeEmail": null,
      "alternativePhone": null,
      "address": "Calle 123 #45-67",
      "fileNumber": "FILE001",
      "hasPtar": false,
      "efficiencyPercentage": null,
      "isPublicServiceCompany": false,
      "isActive": true,
      "municipality": {
        "id": 1,
        "name": "Bogotá",
        "code": "001"
      },
      "economicActivity": {
        "id": 1,
        "name": "Comercio",
        "code": "1234"
      },
      "authorizationType": {
        "id": 1,
        "name": "Permiso de Vertimiento"
      },
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "companyName": "Otra Empresa S.A.",
      "code": "EMP002",
      "documentNumber": "800987654-3",
      "documentType": "NIT",
      "contactPerson": "María González",
      "email": "info@otraempresa.com",
      "phone": "3109876543",
      "alternativeEmail": null,
      "alternativePhone": null,
      "address": "Avenida Principal 456",
      "fileNumber": "FILE002",
      "hasPtar": true,
      "efficiencyPercentage": 85.50,
      "isPublicServiceCompany": false,
      "isActive": true,
      "municipality": {
        "id": 2,
        "name": "Medellín",
        "code": "002"
      },
      "economicActivity": {
        "id": 2,
        "name": "Industria",
        "code": "5678"
      },
      "authorizationType": {
        "id": 2,
        "name": "Permiso de Captación"
      },
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
  "numberOfElements": 2,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": false
  },
  "empty": false
}
```

---

## 5. Eliminar Usuario de Descarga

**Endpoint:** `DELETE /api/discharge-users/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Elimina un usuario de descarga de la corporación del usuario autenticado.

```bash
curl -X DELETE "http://localhost:8080/api/discharge-users/1" \
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
- **400 Bad Request**: Datos inválidos en el request (ej: campos requeridos faltantes, formato inválido, valores duplicados)
- **401 Unauthorized**: Token JWT inválido o ausente
- **403 Forbidden**: Usuario no tiene permisos suficientes o no pertenece a una corporación
- **404 Not Found**: Recurso no encontrado o no pertenece a la corporación del usuario

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer {token}`.

2. **Roles requeridos**:
   - `USER`: Para todas las operaciones CRUD

3. **Validaciones**:
   - `companyName`: Requerido, máximo 200 caracteres
   - `code`: Requerido, máximo 8 caracteres, único dentro de la corporación
   - `documentNumber`: Requerido, máximo 50 caracteres, único dentro de la corporación
   - `documentType`: Requerido, debe ser `NIT`, `CC` o `CE`
   - `contactPerson`: Requerido, máximo 150 caracteres
   - `email`: Requerido, formato válido, máximo 255 caracteres
   - `phone`: Requerido, máximo 10 caracteres
   - `address`: Requerido, máximo 200 caracteres
   - `fileNumber`: Requerido, máximo 20 caracteres
   - `municipality`: Requerido, debe existir en la base de datos
   - `economicActivity`: Requerido, debe existir en la base de datos
   - `authorizationType`: Requerido, debe existir en la base de datos
   - `alternativeEmail`: Opcional, si se proporciona debe ser formato válido
   - `alternativePhone`: Opcional, máximo 10 caracteres
   - `efficiencyPercentage`: Opcional, decimal

4. **Asignación automática**:
   - `corporation`: Se asigna automáticamente con la corporación del usuario autenticado
   - `createdBy`: Se asigna automáticamente con el usuario autenticado al crear
   - `updatedBy`: Se asigna automáticamente con el usuario autenticado al actualizar
   - `createdAt`: Se asigna automáticamente con la fecha/hora actual al crear
   - `updatedAt`: Se actualiza automáticamente con la fecha/hora actual al actualizar

5. **Paginación**: El endpoint `GET /api/discharge-users` soporta paginación estándar de Spring Data con parámetros `page`, `size` y `sort`.

6. **Seguridad**: Los usuarios solo pueden acceder a los usuarios de descarga de su propia corporación. Intentar acceder a usuarios de descarga de otras corporaciones resultará en un error 403 Forbidden.

7. **Unicidad**:
   - El `code` debe ser único dentro de la misma corporación (diferentes corporaciones pueden tener el mismo código)
   - El `documentNumber` debe ser único dentro de la misma corporación (diferentes corporaciones pueden tener el mismo número de documento)
   - La combinación de `documentType` y `documentNumber` debe ser única dentro de la misma corporación

8. **Relaciones**:
   - El `municipality` debe existir y estar activo
   - El `economicActivity` debe existir y estar activo
   - El `authorizationType` debe existir y estar activo

---

## Ejemplos de Uso Común

### Crear un usuario de descarga básico

```bash
curl -X POST "http://localhost:8080/api/discharge-users" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Nueva Empresa S.A.",
    "code": "NEW001",
    "documentNumber": "900111222-3",
    "documentType": "NIT",
    "contactPerson": "Ana López",
    "email": "ana@nuevaempresa.com",
    "phone": "3112223333",
    "address": "Calle Nueva 123",
    "fileNumber": "NEW001",
    "municipality": {"id": 1},
    "economicActivity": {"id": 1},
    "authorizationType": {"id": 1}
  }'
```

### Obtener usuarios de descarga ordenados por nombre

```bash
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=10&sort=companyName,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Obtener solo usuarios activos (filtrar en el frontend)

```bash
# Nota: El filtrado por isActive debe hacerse en el frontend
# o implementarse como un endpoint adicional en el backend
curl -X GET "http://localhost:8080/api/discharge-users?page=0&size=20&sort=isActive,desc&sort=companyName,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Actualizar solo el email y teléfono

```bash
# Primero obtener el usuario actual
curl -X GET "http://localhost:8080/api/discharge-users/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Luego actualizar con todos los campos (incluyendo los que no cambian)
curl -X PUT "http://localhost:8080/api/discharge-users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez",
    "email": "nuevoemail@empresa.com",
    "phone": "3009999999",
    "address": "Calle 123 #45-67",
    "fileNumber": "FILE001",
    "municipality": {"id": 1},
    "economicActivity": {"id": 1},
    "authorizationType": {"id": 1},
    "hasPtar": false,
    "isPublicServiceCompany": false,
    "isActive": true
  }'
```

### Desactivar un usuario de descarga

```bash
# Obtener el usuario actual
curl -X GET "http://localhost:8080/api/discharge-users/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Actualizar con isActive: false
curl -X PUT "http://localhost:8080/api/discharge-users/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez",
    "email": "contacto@empresa.com",
    "phone": "3001234567",
    "address": "Calle 123 #45-67",
    "fileNumber": "FILE001",
    "municipality": {"id": 1},
    "economicActivity": {"id": 1},
    "authorizationType": {"id": 1},
    "hasPtar": false,
    "isPublicServiceCompany": false,
    "isActive": false
  }'
```

---

## Errores Comunes

### Error 400 - Código duplicado en la corporación
```json
{
   "timestamp": "2024-01-15T10:30:00",
   "status": 400,
   "error": "Bad Request",
   "message": "Ya existe un usuario de descarga con el código 'EMP001' en esta corporación"
}
```

### Error 400 - Número de documento duplicado en la corporación
```json
{
   "timestamp": "2024-01-15T10:30:00",
   "status": 400,
   "error": "Bad Request",
   "message": "Ya existe un usuario de descarga con el tipo y número de documento especificados en esta corporación"
}
```

### Error 400 - Municipio no existe
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El municipio no existe"
}
```

### Error 403 - No pertenece a la corporación
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "No tiene permisos para actualizar este usuario de descarga"
}
```

### Error 403 - Usuario sin corporación
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "El usuario debe pertenecer a una corporación"
}
```

