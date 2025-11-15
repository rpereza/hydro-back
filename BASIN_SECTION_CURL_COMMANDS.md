# Basin Section Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `BasinSectionController`.

**Base URL:** `http://localhost:8080/api/basin-sections`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Sección de Cuenca

**Endpoint:** `POST /api/basin-sections`  
**Rol requerido:** `ADMIN`  
**Descripción:** Crea una nueva sección de cuenca para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/basin-sections" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Sección Alta del Río Magdalena",
    "waterBasin": {
      "id": 1
    },
    "description": "Sección correspondiente a la parte alta del río Magdalena",
    "isActive": true,
    "startPoint": "Punto de inicio: Coordenadas X, Y",
    "endPoint": "Punto de fin: Coordenadas X, Y"
  }'
```

**Campos del request:**
- `name`: Nombre de la sección de cuenca (requerido, máximo 100 caracteres)
- `waterBasin`: Objeto con el ID de la cuenca hidrográfica (requerido)
  - `id`: ID de la cuenca hidrográfica (Long)
- `description`: Descripción opcional (máximo 1000 caracteres)
- `isActive`: Estado activo/inactivo (opcional, Boolean, default: true)
- `startPoint`: Punto de inicio opcional (String)
- `endPoint`: Punto de fin opcional (String)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Sección Alta del Río Magdalena",
  "waterBasin": {
    "id": 1,
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true
  },
  "description": "Sección correspondiente a la parte alta del río Magdalena",
  "isActive": true,
  "startPoint": "Punto de inicio: Coordenadas X, Y",
  "endPoint": "Punto de fin: Coordenadas X, Y",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `400 Bad Request`: Datos inválidos, validación fallida, nombre duplicado en la corporación, o cuenca hidrográfica no pertenece a la corporación
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** El nombre debe ser único dentro de la corporación (comparación case-insensitive). La cuenca hidrográfica debe pertenecer a la corporación del usuario.

---

## 2. Actualizar Sección de Cuenca

**Endpoint:** `PUT /api/basin-sections/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Actualiza una sección de cuenca existente.

```bash
curl -X PUT "http://localhost:8080/api/basin-sections/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Sección Alta del Río Magdalena - Actualizada",
    "waterBasin": {
      "id": 1
    },
    "description": "Descripción actualizada de la sección",
    "isActive": true,
    "startPoint": "Nuevo punto de inicio",
    "endPoint": "Nuevo punto de fin"
  }'
```

**Parámetros de URL:**
- `id`: ID de la sección de cuenca a actualizar (Long)

**Campos del request:**
- `name`: Nombre de la sección de cuenca (requerido, máximo 100 caracteres)
- `waterBasin`: Objeto con el ID de la cuenca hidrográfica (requerido)
  - `id`: ID de la cuenca hidrográfica (Long)
- `description`: Descripción opcional (máximo 1000 caracteres)
- `isActive`: Estado activo/inactivo (opcional, Boolean)
- `startPoint`: Punto de inicio opcional (String)
- `endPoint`: Punto de fin opcional (String)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Sección Alta del Río Magdalena - Actualizada",
  "waterBasin": {
    "id": 1,
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true
  },
  "description": "Descripción actualizada de la sección",
  "isActive": true,
  "startPoint": "Nuevo punto de inicio",
  "endPoint": "Nuevo punto de fin",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T14:20:00"
}
```

**Errores posibles:**
- `404 Not Found`: La sección de cuenca no existe
- `400 Bad Request`: Datos inválidos, validación fallida, nombre duplicado en la corporación, o cuenca hidrográfica no pertenece a la corporación
- `403 Forbidden`: El usuario no tiene permisos o la sección no pertenece a su corporación

**Nota:** El nombre debe ser único dentro de la corporación, excluyendo la sección que se está actualizando.

---

## 3. Obtener Sección de Cuenca por ID

**Endpoint:** `GET /api/basin-sections/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene una sección de cuenca específica por su ID.

```bash
curl -X GET "http://localhost:8080/api/basin-sections/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la sección de cuenca (Long)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Sección Alta del Río Magdalena",
  "waterBasin": {
    "id": 1,
    "name": "Cuenca del Río Magdalena",
    "description": "Cuenca hidrográfica principal del río Magdalena",
    "isActive": true
  },
  "description": "Sección correspondiente a la parte alta del río Magdalena",
  "isActive": true,
  "startPoint": "Punto de inicio: Coordenadas X, Y",
  "endPoint": "Punto de fin: Coordenadas X, Y",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `404 Not Found`: La sección de cuenca no existe o no pertenece a la corporación del usuario

---

## 4. Obtener Todas las Secciones de Cuenca (Paginado)

**Endpoint:** `GET /api/basin-sections`  
**Rol requerido:** `ADMIN`  
**Descripción:** Obtiene todas las secciones de cuenca de la corporación del usuario autenticado con paginación.

### Ejemplo básico (paginación por defecto):
```bash
curl -X GET "http://localhost:8080/api/basin-sections" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con parámetros de paginación:
```bash
curl -X GET "http://localhost:8080/api/basin-sections?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/basin-sections?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Ejemplo con múltiples criterios de ordenamiento:
```bash
curl -X GET "http://localhost:8080/api/basin-sections?page=0&size=10&sort=name,asc&sort=createdAt,desc" \
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
      "name": "Sección Alta del Río Magdalena",
      "waterBasin": {
        "id": 1,
        "name": "Cuenca del Río Magdalena",
        "description": "Cuenca hidrográfica principal del río Magdalena",
        "isActive": true
      },
      "description": "Sección correspondiente a la parte alta del río Magdalena",
      "isActive": true,
      "startPoint": "Punto de inicio: Coordenadas X, Y",
      "endPoint": "Punto de fin: Coordenadas X, Y",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "name": "Sección Media del Río Magdalena",
      "waterBasin": {
        "id": 1,
        "name": "Cuenca del Río Magdalena",
        "description": "Cuenca hidrográfica principal del río Magdalena",
        "isActive": true
      },
      "description": "Sección correspondiente a la parte media del río Magdalena",
      "isActive": true,
      "startPoint": null,
      "endPoint": null,
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
- `content`: Array con las secciones de cuenca de la página actual
- `totalElements`: Total de secciones de cuenca
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

## 5. Obtener Todas las Secciones de Cuenca (Sin Paginación)

**Endpoint:** `GET /api/basin-sections/all`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todas las secciones de cuenca de la corporación del usuario autenticado sin paginación.

```bash
curl -X GET "http://localhost:8080/api/basin-sections/all" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Sección Alta del Río Magdalena",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true
    },
    "description": "Sección correspondiente a la parte alta del río Magdalena",
    "isActive": true,
    "startPoint": "Punto de inicio: Coordenadas X, Y",
    "endPoint": "Punto de fin: Coordenadas X, Y",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  },
  {
    "id": 2,
    "name": "Sección Media del Río Magdalena",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true
    },
    "description": "Sección correspondiente a la parte media del río Magdalena",
    "isActive": true,
    "startPoint": null,
    "endPoint": null,
    "createdAt": "2024-01-10T09:15:00",
    "updatedAt": null
  }
]
```

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** Este endpoint requiere rol `USER` o superior, a diferencia del endpoint paginado que requiere `ADMIN`.

---

## 6. Obtener Secciones de Cuenca por Cuenca Hidrográfica

**Endpoint:** `GET /api/basin-sections/by-water-basin/{waterBasinId}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todas las secciones de cuenca asociadas a una cuenca hidrográfica específica.

```bash
curl -X GET "http://localhost:8080/api/basin-sections/by-water-basin/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `waterBasinId`: ID de la cuenca hidrográfica (Long)

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Sección Alta del Río Magdalena",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true
    },
    "description": "Sección correspondiente a la parte alta del río Magdalena",
    "isActive": true,
    "startPoint": "Punto de inicio: Coordenadas X, Y",
    "endPoint": "Punto de fin: Coordenadas X, Y",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  },
  {
    "id": 2,
    "name": "Sección Media del Río Magdalena",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true
    },
    "description": "Sección correspondiente a la parte media del río Magdalena",
    "isActive": true,
    "startPoint": null,
    "endPoint": null,
    "createdAt": "2024-01-10T09:15:00",
    "updatedAt": null
  }
]
```

**Errores posibles:**
- `400 Bad Request`: La cuenca hidrográfica no existe o no pertenece a la corporación del usuario
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** Solo devuelve secciones de cuencas hidrográficas que pertenecen a la corporación del usuario autenticado.

---

## 7. Buscar Secciones de Cuenca por Nombre

**Endpoint:** `GET /api/basin-sections/search?name={nombre}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Busca secciones de cuenca por nombre (búsqueda parcial, case-insensitive).

```bash
curl -X GET "http://localhost:8080/api/basin-sections/search?name=Alta" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**
- `name`: Nombre o parte del nombre a buscar (requerido, String)

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Sección Alta del Río Magdalena",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Magdalena",
      "description": "Cuenca hidrográfica principal del río Magdalena",
      "isActive": true
    },
    "description": "Sección correspondiente a la parte alta del río Magdalena",
    "isActive": true,
    "startPoint": "Punto de inicio: Coordenadas X, Y",
    "endPoint": "Punto de fin: Coordenadas X, Y",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null
  }
]
```

**Ejemplo con búsqueda parcial:**
```bash
curl -X GET "http://localhost:8080/api/basin-sections/search?name=seccion" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Nota:** La búsqueda es case-insensitive (no distingue mayúsculas/minúsculas) y busca dentro del nombre de la sección. Solo devuelve secciones de la corporación del usuario autenticado.

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

---

## 8. Eliminar Sección de Cuenca

**Endpoint:** `DELETE /api/basin-sections/{id}`  
**Rol requerido:** `ADMIN`  
**Descripción:** Elimina una sección de cuenca.

```bash
curl -X DELETE "http://localhost:8080/api/basin-sections/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la sección de cuenca a eliminar (Long)

**Respuesta exitosa (200 OK):**
```json
true
```

**Errores posibles:**
- `404 Not Found`: La sección de cuenca no existe
- `403 Forbidden`: El usuario no tiene permisos o la sección no pertenece a su corporación

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
# 1. Crear una sección de cuenca
TOKEN="YOUR_JWT_TOKEN"

CREATE_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/basin-sections" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Sección Alta del Río Magdalena",
    "waterBasin": {
      "id": 1
    },
    "description": "Sección correspondiente a la parte alta del río Magdalena",
    "isActive": true,
    "startPoint": "Punto de inicio: Coordenadas X, Y",
    "endPoint": "Punto de fin: Coordenadas X, Y"
  }')

echo "Sección creada: $CREATE_RESPONSE"

# 2. Obtener todas las secciones (primera página, ordenadas por nombre)
curl -X GET "http://localhost:8080/api/basin-sections?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer $TOKEN"

# 3. Obtener todas las secciones sin paginación (para usuarios con rol USER)
curl -X GET "http://localhost:8080/api/basin-sections/all" \
  -H "Authorization: Bearer $TOKEN"

# 4. Obtener secciones por cuenca hidrográfica
curl -X GET "http://localhost:8080/api/basin-sections/by-water-basin/1" \
  -H "Authorization: Bearer $TOKEN"

# 5. Buscar secciones por nombre
curl -X GET "http://localhost:8080/api/basin-sections/search?name=Alta" \
  -H "Authorization: Bearer $TOKEN"

# 6. Obtener una sección específica (asumiendo ID = 1)
curl -X GET "http://localhost:8080/api/basin-sections/1" \
  -H "Authorization: Bearer $TOKEN"

# 7. Actualizar la sección
curl -X PUT "http://localhost:8080/api/basin-sections/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Sección Alta del Río Magdalena - Actualizada",
    "waterBasin": {
      "id": 1
    },
    "description": "Descripción actualizada de la sección",
    "isActive": true,
    "startPoint": "Nuevo punto de inicio",
    "endPoint": "Nuevo punto de fin"
  }'

# 8. Eliminar la sección
curl -X DELETE "http://localhost:8080/api/basin-sections/1" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <token>`

2. **Roles**:
   - `ADMIN`: Requerido para crear, actualizar, eliminar, obtener por ID y listar con paginación
   - `USER` o superior: Requerido para obtener todas las secciones sin paginación (`/all`), obtener por cuenca hidrográfica y buscar

3. **Validación de Nombre Único**: 
   - El nombre de la sección debe ser único dentro de la corporación (comparación case-insensitive)
   - Al crear: se valida que no exista otra sección con el mismo nombre en la corporación
   - Al actualizar: se valida que no exista otra sección con el mismo nombre, excluyendo la sección actual

4. **Validación de Cuenca Hidrográfica**: 
   - La cuenca hidrográfica especificada debe pertenecer a la corporación del usuario autenticado
   - Solo se pueden asociar secciones a cuencas de la propia corporación

5. **Paginación**: El endpoint de listado principal soporta paginación con parámetros estándar de Spring Data:
   - `page`: Número de página (0-indexed)
   - `size`: Tamaño de página
   - `sort`: Criterio de ordenamiento

6. **Corporación**: Las secciones de cuenca están asociadas a la corporación del usuario autenticado. Solo se pueden ver/modificar las secciones de la propia corporación

7. **Formato de fechas**: Las fechas se devuelven en formato ISO 8601: `YYYY-MM-DDTHH:mm:ss`

8. **Búsqueda**: La búsqueda por nombre es parcial y case-insensitive, solo busca dentro de la corporación del usuario

9. **Relaciones**: Las secciones de cuenca están relacionadas con:
   - Una cuenca hidrográfica (`waterBasin`) - requerida
   - Pueden tener múltiples estaciones de monitoreo (`monitoringStations`)
   - Pueden tener múltiples descargas (`discharges`)

---

## Códigos de Estado HTTP

- `200 OK`: Operación exitosa
- `201 Created`: Recurso creado exitosamente
- `400 Bad Request`: Datos inválidos, validación fallida, nombre duplicado en la corporación, o cuenca hidrográfica no pertenece a la corporación
- `401 Unauthorized`: Token JWT inválido o ausente
- `403 Forbidden`: Usuario sin permisos suficientes o recurso no pertenece a su corporación
- `404 Not Found`: Recurso no encontrado
- `500 Internal Server Error`: Error interno del servidor

---

## Validaciones de Campos

### Campo `name`:
- **Requerido**: Sí
- **Tipo**: String
- **Longitud máxima**: 100 caracteres
- **Unicidad**: Debe ser único dentro de la corporación (case-insensitive)

### Campo `waterBasin`:
- **Requerido**: Sí
- **Tipo**: Objeto con `id` (Long)
- **Validación**: La cuenca hidrográfica debe existir y pertenecer a la corporación del usuario

### Campo `description`:
- **Requerido**: No
- **Tipo**: String
- **Longitud máxima**: 1000 caracteres

### Campo `isActive`:
- **Requerido**: No
- **Tipo**: Boolean
- **Valor por defecto**: `true`

### Campo `startPoint`:
- **Requerido**: No
- **Tipo**: String
- **Uso**: Punto de inicio geográfico de la sección

### Campo `endPoint`:
- **Requerido**: No
- **Tipo**: String
- **Uso**: Punto de fin geográfico de la sección

---

## Ejemplo de Request Body Completo

```json
{
  "name": "Sección Alta del Río Magdalena",
  "waterBasin": {
    "id": 1
  },
  "description": "Sección correspondiente a la parte alta del río Magdalena, desde su nacimiento hasta el punto medio del curso",
  "isActive": true,
  "startPoint": "Latitud: 1.2345, Longitud: -75.6789",
  "endPoint": "Latitud: 2.3456, Longitud: -74.5678"
}
```

**Nota:** Solo es necesario proporcionar el `id` de la cuenca hidrográfica en el objeto `waterBasin`. El sistema completará automáticamente la información de la cuenca en la respuesta.

