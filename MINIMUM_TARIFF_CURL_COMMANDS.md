# Minimum Tariff Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `MinimumTariffController`.

**Base URL:** `http://localhost:8080/api/minimum-tariffs`

**Nota:** Todos los endpoints requieren autenticación JWT y rol `ADMIN`. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Tarifa Mínima

**Endpoint:** `POST /api/minimum-tariffs`  
**Rol requerido:** `ADMIN`  
**Descripción:** Crea una nueva tarifa mínima para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/minimum-tariffs" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "year": 2024,
    "dboValue": 250.50,
    "sstValue": 180.75,
    "ipcValue": 320.00,
    "isActive": true
  }'
```

**Campos del request:**
- `year`: Año de la tarifa mínima (requerido, Integer)
- `dboValue`: Valor de DBO (requerido, BigDecimal, precision 5, scale 2)
- `sstValue`: Valor de SST (requerido, BigDecimal, precision 5, scale 2)
- `ipcValue`: Valor de IPC (requerido, BigDecimal, precision 5, scale 2)
- `isActive`: Estado activo/inactivo (opcional, Boolean, default: true)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "year": 2024,
  "dboValue": 250.50,
  "sstValue": 180.75,
  "ipcValue": 320.00,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `400 Bad Request`: Datos inválidos o validación fallida
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

---

## 2. Actualizar Tarifa Mínima

**Endpoint:** `PUT /api/minimum-tariffs/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Actualiza una tarifa mínima existente.

```bash
curl -X PUT "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "year": 2024,
    "dboValue": 255.75,
    "sstValue": 185.00,
    "ipcValue": 325.50,
    "isActive": true
  }'
```

**Parámetros de URL:**
- `id`: ID de la tarifa mínima a actualizar (Long)

**Campos del request:**
- `year`: Año de la tarifa mínima (requerido, Integer)
- `dboValue`: Valor de DBO (requerido, BigDecimal)
- `sstValue`: Valor de SST (requerido, BigDecimal)
- `ipcValue`: Valor de IPC (requerido, BigDecimal)
- `isActive`: Estado activo/inactivo (opcional, Boolean)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "year": 2024,
  "dboValue": 255.75,
  "sstValue": 185.00,
  "ipcValue": 325.50,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:20:00"
}
```

**Errores posibles:**
- `404 Not Found`: La tarifa mínima no existe
- `400 Bad Request`: Datos inválidos o validación fallida
- `403 Forbidden`: El usuario no tiene permisos o la tarifa no pertenece a su corporación

---

## 3. Obtener Tarifa Mínima por ID

**Endpoint:** `GET /api/minimum-tariffs/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene una tarifa mínima específica por su ID.

```bash
curl -X GET "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la tarifa mínima (Long)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "year": 2024,
  "dboValue": 250.50,
  "sstValue": 180.75,
  "ipcValue": 320.00,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `404 Not Found`: La tarifa mínima no existe

---

## 4. Obtener Todas las Tarifas Mínimas (Paginado)

**Endpoint:** `GET /api/minimum-tariffs`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todas las tarifas mínimas de la corporación del usuario autenticado con paginación.

### Ejemplo básico (paginación por defecto):
```bash
curl -X GET "http://localhost:8080/api/minimum-tariffs" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con parámetros de paginación:
```bash
curl -X GET "http://localhost:8080/api/minimum-tariffs?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/minimum-tariffs?page=0&size=10&sort=year,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con múltiples criterios de ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/minimum-tariffs?page=0&size=10&sort=year,desc&sort=createdAt,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `page`: Número de página (opcional, default: 0, Integer)
- `size`: Tamaño de la página (opcional, default: 20, Integer)
- `sort`: Criterio de ordenamiento (opcional, formato: `campo,direccion`)
  - Campos disponibles: `id`, `year`, `dboValue`, `sstValue`, `ipcValue`, `isActive`, `createdAt`, `updatedAt`
  - Direcciones: `asc` (ascendente) o `desc` (descendente)
  - Se pueden especificar múltiples criterios de ordenamiento

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "year": 2024,
      "dboValue": 250.50,
      "sstValue": 180.75,
      "ipcValue": 320.00,
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "year": 2023,
      "dboValue": 245.00,
      "sstValue": 175.50,
      "ipcValue": 315.25,
      "isActive": true,
      "createdAt": "2023-01-10T09:15:00",
      "updatedAt": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 25,
  "totalPages": 3,
  "last": false,
  "first": true,
  "size": 10,
  "number": 0,
  "numberOfElements": 10,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "empty": false
}
```

**Campos de la respuesta paginada:**
- `content`: Array con las tarifas mínimas de la página actual
- `totalElements`: Total de tarifas mínimas
- `totalPages`: Total de páginas
- `size`: Tamaño de la página solicitada
- `number`: Número de página actual (0-indexed)
- `first`: Indica si es la primera página
- `last`: Indica si es la última página
- `numberOfElements`: Número de elementos en la página actual
- `empty`: Indica si la página está vacía

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

---

## 5. Eliminar Tarifa Mínima

**Endpoint:** `DELETE /api/minimum-tariffs/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Elimina una tarifa mínima.

```bash
curl -X DELETE "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la tarifa mínima a eliminar (Long)

**Respuesta exitosa (200 OK):**
```json
true
```

**Errores posibles:**
- `404 Not Found`: La tarifa mínima no existe
- `403 Forbidden`: El usuario no tiene permisos o la tarifa no pertenece a su corporación

---

## Ejemplos de Uso Completo

### 1. Obtener token de autenticación

Primero, necesitas autenticarte para obtener el token JWT:

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "admin123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwNTMyMDAwMCwiZXhwIjoxNzA1NDA2NDAwfQ...",
  "type": "Bearer",
  "username": "admin",
  "email": "admin@example.com",
  "roles": ["ADMIN"]
}
```

### 2. Flujo completo: Crear, Listar, Actualizar y Eliminar

```bash
# 1. Crear una tarifa mínima
TOKEN="YOUR_JWT_TOKEN"

CREATE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/minimum-tariffs" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "year": 2024,
    "dboValue": 250.50,
    "sstValue": 180.75,
    "ipcValue": 320.00,
    "isActive": true
  }')

echo "Tarifa creada: $CREATE_RESPONSE"

# 2. Obtener todas las tarifas (primera página)
curl -X GET "http://localhost:8080/api/minimum-tariffs?page=0&size=10&sort=year,desc" \
  -H "Authorization: Bearer $TOKEN"

# 3. Obtener una tarifa específica (asumiendo ID = 1)
curl -X GET "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Authorization: Bearer $TOKEN"

# 4. Actualizar la tarifa
curl -X PUT "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "year": 2024,
    "dboValue": 255.75,
    "sstValue": 185.00,
    "ipcValue": 325.50,
    "isActive": true
  }'

# 5. Eliminar la tarifa
curl -X DELETE "http://localhost:8080/api/minimum-tariffs/1" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <token>`

2. **Roles**: Todos los endpoints requieren el rol `ADMIN`

3. **Validación**: Los campos `year`, `dboValue`, `sstValue` e `ipcValue` son obligatorios y deben cumplir las validaciones definidas en la entidad

4. **Paginación**: El endpoint de listado soporta paginación con parámetros estándar de Spring Data:
   - `page`: Número de página (0-indexed)
   - `size`: Tamaño de página
   - `sort`: Criterio de ordenamiento

5. **Corporación**: Las tarifas mínimas están asociadas a la corporación del usuario autenticado. Solo se pueden ver/modificar las tarifas de la propia corporación

6. **Formato de fechas**: Las fechas se devuelven en formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`

7. **Valores decimales**: Los valores de DBO, SST e IPC se manejan como `BigDecimal` con precisión de 5 dígitos y 2 decimales

---

## Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos o validación fallida
- `401 Unauthorized`: Token JWT inválido o ausente
- `403 Forbidden`: Usuario sin permisos suficientes
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor

