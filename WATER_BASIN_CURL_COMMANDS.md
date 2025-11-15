# Water Basin Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `WaterBasinController`.

**Base URL:** `http://localhost:8080/api/water-basins`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Cuenca Hidrográfica

**Endpoint:** `POST /api/water-basins`  
**Rol requerido:** `ADMIN`  
**Descripción:** Crea una nueva cuenca hidrográfica para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/water-basins" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true
  }'
```

**Campos del request:**
- `name`: Nombre de la cuenca hidrográfica (requerido, máximo 150 caracteres)
- `description`: Descripción opcional (máximo 1000 caracteres)
- `isActive`: Estado activo/inactivo (opcional, Boolean, default: true)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Cuenca del Río Magdalena",
  "description": "Cuenca hidrográfica principal del río Magdalena",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `400 Bad Request`: Datos inválidos, validación fallida, o nombre duplicado en la corporación
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** El nombre debe ser único dentro de la corporación (comparación case-insensitive).

---

## 2. Actualizar Cuenca Hidrográfica

**Endpoint:** `PUT /api/water-basins/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Actualiza una cuenca hidrográfica existente.

```bash
curl -X PUT "http://localhost:8080/api/water-basins/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Cuenca del Río Magdalena - Actualizada",
    "description": "Descripción actualizada de la cuenca hidrográfica",
    "isActive": true
  }'
```

**Parámetros de URL:**
- `id`: ID de la cuenca hidrográfica a actualizar (Long)

**Campos del request:**
- `name`: Nombre de la cuenca hidrográfica (requerido, máximo 150 caracteres)
- `description`: Descripción opcional (máximo 1000 caracteres)
- `isActive`: Estado activo/inactivo (opcional, Boolean)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Cuenca del Río Magdalena - Actualizada",
  "description": "Descripción actualizada de la cuenca hidrográfica",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:20:00"
}
```

**Errores posibles:**
- `404 Not Found`: La cuenca hidrográfica no existe
- `400 Bad Request`: Datos inválidos, validación fallida, o nombre duplicado en la corporación
- `403 Forbidden`: El usuario no tiene permisos o la cuenca no pertenece a su corporación

**Nota:** El nombre debe ser único dentro de la corporación, excluyendo la cuenca que se está actualizando.

---

## 3. Obtener Cuenca Hidrográfica por ID

**Endpoint:** `GET /api/water-basins/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene una cuenca hidrográfica específica por su ID.

```bash
curl -X GET "http://localhost:8080/api/water-basins/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la cuenca hidrográfica (Long)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Cuenca del Río Magdalena",
  "description": "Cuenca hidrográfica principal del río Magdalena",
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `404 Not Found`: La cuenca hidrográfica no existe o no pertenece a la corporación del usuario

---

## 4. Obtener Todas las Cuencas Hidrográficas (Paginado)

**Endpoint:** `GET /api/water-basins`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado con paginación.

### Ejemplo básico (paginación por defecto):
```bash
curl -X GET "http://localhost:8080/api/water-basins" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con parámetros de paginación:
```bash
curl -X GET "http://localhost:8080/api/water-basins?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/water-basins?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con múltiples criterios de ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/water-basins?page=0&size=10&sort=name,asc&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `page`: Número de página (opcional, default: 0, Integer)
- `size`: Tamaño de la página (opcional, default: 20, Integer)
- `sort`: Criterio de ordenamiento (opcional, formato: `campo,direccion`)
  - Campos disponibles: `id`, `name`, `isActive`, `createdAt`, `updatedAt`
  - Direcciones: `asc` (ascendente) o `desc` (descendente)
  - Se pueden especificar múltiples criterios de ordenamiento

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "name": "Cuenca del Río Cauca",
      "description": "Cuenca hidrográfica del río Cauca",
      "isActive": true,
      "createdAt": "2024-01-10T09:15:00",
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
- `content`: Array con las cuencas hidrográficas de la página actual
- `totalElements`: Total de cuencas hidrográficas
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

## 5. Obtener Todas las Cuencas Hidrográficas (Sin Paginación)

**Endpoint:** `GET /api/water-basins/all`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todas las cuencas hidrográficas de la corporación del usuario autenticado sin paginación.

```bash
curl -X GET "http://localhost:8080/api/water-basins/all" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  },
  {
    "id": 2,
    "name": "Cuenca del Río Cauca",
    "description": "Cuenca hidrográfica del río Cauca",
    "isActive": true,
    "createdAt": "2024-01-10T09:15:00",
    "updatedAt": null
  }
]
```

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** Este endpoint requiere rol `USER` o superior, a diferencia del endpoint paginado que requiere `ADMIN`.

---

## 6. Buscar Cuencas Hidrográficas por Nombre

**Endpoint:** `GET /api/water-basins/search?name={nombre}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Busca cuencas hidrográficas por nombre (búsqueda parcial, case-insensitive).

```bash
curl -X GET "http://localhost:8080/api/water-basins/search?name=Magdalena" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `name`: Nombre o parte del nombre a buscar (requerido, String)

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  }
]
```

**Ejemplo con búsqueda parcial:**
```bash
curl -X GET "http://localhost:8080/api/water-basins/search?name=rio" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Nota:** La búsqueda es case-insensitive (no distingue mayúsculas/minúsculas) y busca dentro del nombre de la cuenca. Solo devuelve cuencas de la corporación del usuario autenticado.

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

---

## 7. Eliminar Cuenca Hidrográfica

**Endpoint:** `DELETE /api/water-basins/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Elimina una cuenca hidrográfica.

```bash
curl -X DELETE "http://localhost:8080/api/water-basins/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la cuenca hidrográfica a eliminar (Long)

**Respuesta exitosa (200 OK):**
```json
true
```

**Errores posibles:**
- `404 Not Found`: La cuenca hidrográfica no existe
- `403 Forbidden`: El usuario no tiene permisos o la cuenca no pertenece a su corporación
- `400 Bad Request`: La cuenca tiene secciones asociadas y no puede ser eliminada

**Nota:** No se puede eliminar una cuenca hidrográfica que tenga secciones asociadas. Primero debe eliminar o reasignar las secciones.

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

### 2. Flujo completo: Crear, Listar, Buscar, Actualizar y Eliminar

```bash
# 1. Crear una cuenca hidrográfica
TOKEN="YOUR_JWT_TOKEN"

CREATE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/water-basins" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true
  }')

echo "Cuenca creada: $CREATE_RESPONSE"

# 2. Obtener todas las cuencas (primera página, ordenadas por nombre)
curl -X GET "http://localhost:8080/api/water-basins?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer $TOKEN"

# 3. Obtener todas las cuencas sin paginación (para usuarios con rol USER)
curl -X GET "http://localhost:8080/api/water-basins/all" \
  -H "Authorization: Bearer $TOKEN"

# 4. Buscar cuencas por nombre
curl -X GET "http://localhost:8080/api/water-basins/search?name=Magdalena" \
  -H "Authorization: Bearer $TOKEN"

# 5. Obtener una cuenca específica (asumiendo ID = 1)
curl -X GET "http://localhost:8080/api/water-basins/1" \
  -H "Authorization: Bearer $TOKEN"

# 6. Actualizar la cuenca
curl -X PUT "http://localhost:8080/api/water-basins/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Cuenca del Río Magdalena - Actualizada",
    "description": "Descripción actualizada de la cuenca",
    "isActive": true
  }'

# 7. Eliminar la cuenca (solo si no tiene secciones asociadas)
curl -X DELETE "http://localhost:8080/api/water-basins/1" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <token>`

2. **Roles**:
   - `ADMIN`: Requerido para crear, actualizar, eliminar, obtener por ID, listar con paginación y buscar
   - `USER` o superior: Requerido para obtener todas las cuencas sin paginación (`/all`)

3. **Validación de Nombre Único**: 
   - El nombre de la cuenca debe ser único dentro de la corporación (comparación case-insensitive)
   - Al crear: se valida que no exista otra cuenca con el mismo nombre en la corporación
   - Al actualizar: se valida que no exista otra cuenca con el mismo nombre, excluyendo la cuenca actual

4. **Paginación**: El endpoint de listado principal soporta paginación con parámetros estándar de Spring Data:
   - `page`: Número de página (0-indexed)
   - `size`: Tamaño de página
   - `sort`: Criterio de ordenamiento

5. **Corporación**: Las cuencas hidrográficas están asociadas a la corporación del usuario autenticado. Solo se pueden ver/modificar las cuencas de la propia corporación

6. **Formato de fechas**: Las fechas se devuelven en formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`

7. **Eliminación**: No se puede eliminar una cuenca que tenga secciones asociadas. Primero debe eliminar o reasignar las secciones

8. **Búsqueda**: La búsqueda por nombre es parcial y case-insensitive, solo busca dentro de la corporación del usuario

---

## Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos, validación fallida, nombre duplicado, o intento de eliminar cuenca con secciones
- `401 Unauthorized`: Token JWT inválido o ausente
- `403 Forbidden`: Usuario sin permisos suficientes o recurso no pertenece a su corporación
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor

---

## Validaciones de Campos

### Campo `name`:
- **Requerido**: Sí
- **Tipo**: String
- **Longitud máxima**: 150 caracteres
- **Unicidad**: Debe ser único dentro de la corporación (case-insensitive)

### Campo `description`:
- **Requerido**: No
- **Tipo**: String
- **Longitud máxima**: 1000 caracteres

### Campo `isActive`:
- **Requerido**: No
- **Tipo**: Boolean
- **Valor por defecto**: `true`

