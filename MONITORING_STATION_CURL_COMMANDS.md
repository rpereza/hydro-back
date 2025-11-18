# MonitoringStationController - cURL Examples

Este documento contiene ejemplos de cURL para todos los endpoints del `MonitoringStationController`.

**Base URL**: `http://localhost:8080/api/monitoring-stations`

**Autenticación**: Todos los endpoints requieren un token JWT en el header `Authorization: Bearer <token>`

**Roles requeridos**: 
- La mayoría de los endpoints requieren el rol `ADMIN`
- El endpoint de búsqueda por nombre y sección de cuenca (`GET /api/monitoring-stations/find`) requiere el rol `USER`

---

## 1. Crear Estación de Monitoreo

**Endpoint**: `POST /api/monitoring-stations`

**Descripción**: Crea una nueva estación de monitoreo para la corporación del usuario autenticado.

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <JWT_TOKEN>`

**Body** (JSON):
```json
{
  "name": "Estación Río Grande",
  "basinSection": {
    "id": 1
  },
  "description": "Estación de monitoreo ubicada en el río Grande",
  "latitude": 14.634915,
  "longitude": -90.506882,
  "isActive": true
}
```

**cURL Command**:
```bash
curl -X POST "http://localhost:8080/api/monitoring-stations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Estación Río Grande",
    "basinSection": {
      "id": 1
    },
    "description": "Estación de monitoreo ubicada en el río Grande",
    "latitude": 14.634915,
    "longitude": -90.506882,
    "isActive": true
  }'
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "name": "Estación Río Grande",
  "basinSection": {
    "id": 1,
    "name": "Sección Norte",
    "description": "Sección norte de la cuenca"
  },
  "description": "Estación de monitoreo ubicada en el río Grande",
  "latitude": 14.634915,
  "longitude": -90.506882,
  "isActive": true,
  "corporation": {
    "id": 1,
    "name": "Corporación Ejemplo"
  },
  "createdBy": {
    "id": 1,
    "username": "admin"
  },
  "createdAt": "2024-01-15T10:30:00"
}
```

**Errores posibles**:
- `400 Bad Request`: Datos inválidos o nombre duplicado en la corporación
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación
- `409 Conflict`: Conflicto (ej: nombre de estación de monitoreo ya existe)

---

## 2. Actualizar Estación de Monitoreo

**Endpoint**: `PUT /api/monitoring-stations/{id}`

**Descripción**: Actualiza una estación de monitoreo existente de la corporación del usuario.

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID de la estación de monitoreo a actualizar

**Body** (JSON):
```json
{
  "name": "Estación Río Grande - Actualizada",
  "basinSection": {
    "id": 1
  },
  "description": "Descripción actualizada de la estación",
  "latitude": 14.635000,
  "longitude": -90.507000,
  "isActive": true
}
```

**cURL Command**:
```bash
curl -X PUT "http://localhost:8080/api/monitoring-stations/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Estación Río Grande - Actualizada",
    "basinSection": {
      "id": 1
    },
    "description": "Descripción actualizada de la estación",
    "latitude": 14.635000,
    "longitude": -90.507000,
    "isActive": true
  }'
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Estación Río Grande - Actualizada",
  "basinSection": {
    "id": 1,
    "name": "Sección Norte"
  },
  "description": "Descripción actualizada de la estación",
  "latitude": 14.635000,
  "longitude": -90.507000,
  "isActive": true,
  "updatedBy": {
    "id": 1,
    "username": "admin"
  },
  "updatedAt": "2024-01-15T11:00:00"
}
```

**Errores posibles**:
- `404 Not Found`: Estación no encontrada o no pertenece a la corporación
- `400 Bad Request`: Datos inválidos o nombre duplicado
- `403 Forbidden`: Usuario no autenticado o sin permisos
- `409 Conflict`: Conflicto (ej: nombre de estación de monitoreo ya existe)

---

## 3. Obtener Estación de Monitoreo por ID

**Endpoint**: `GET /api/monitoring-stations/{id}`

**Descripción**: Obtiene una estación de monitoreo específica por su ID.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID de la estación de monitoreo

**cURL Command**:
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Estación Río Grande",
  "basinSection": {
    "id": 1,
    "name": "Sección Norte",
    "description": "Sección norte de la cuenca",
    "waterBasin": {
      "id": 1,
      "name": "Cuenca del Río Grande"
    }
  },
  "description": "Estación de monitoreo ubicada en el río Grande",
  "latitude": 14.634915,
  "longitude": -90.506882,
  "isActive": true,
  "corporation": {
    "id": 1,
    "name": "Corporación Ejemplo"
  },
  "createdBy": {
    "id": 1,
    "username": "admin"
  },
  "createdAt": "2024-01-15T10:30:00"
}
```

**Errores posibles**:
- `404 Not Found`: Estación no encontrada o no pertenece a la corporación
- `403 Forbidden`: Usuario no autenticado

---

## 4. Obtener Todas las Estaciones de Monitoreo (con Paginación)

**Endpoint**: `GET /api/monitoring-stations`

**Descripción**: Obtiene todas las estaciones de monitoreo de la corporación del usuario con paginación.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Query Parameters**:
- `page`: Número de página (0-indexed, default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo(s) para ordenar (formato: `campo,direccion` o `campo1,direccion1&sort=campo2,direccion2`)

**Ejemplos de cURL**:

### 4.1. Obtener primera página (default)
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.2. Obtener página específica con tamaño personalizado
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.3. Obtener con ordenamiento por nombre (ascendente)
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.4. Obtener con ordenamiento por fecha de creación (descendente)
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.5. Obtener con múltiples criterios de ordenamiento
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations?page=0&size=10&sort=isActive,desc&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "name": "Estación Río Grande",
      "basinSection": {
        "id": 1,
        "name": "Sección Norte"
      },
      "description": "Estación de monitoreo ubicada en el río Grande",
      "latitude": 14.634915,
      "longitude": -90.506882,
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00"
    },
    {
      "id": 2,
      "name": "Estación Río Chico",
      "basinSection": {
        "id": 2,
        "name": "Sección Sur"
      },
      "description": "Estación de monitoreo ubicada en el río Chico",
      "latitude": 14.640000,
      "longitude": -90.510000,
      "isActive": true,
      "createdAt": "2024-01-15T11:00:00"
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
  "empty": false
}
```

**Errores posibles**:
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

---

## 5. Buscar Estaciones de Monitoreo por Nombre

**Endpoint**: `GET /api/monitoring-stations/search`

**Descripción**: Busca estaciones de monitoreo por nombre (búsqueda parcial, case-insensitive).

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Query Parameters**:
- `name`: Nombre o parte del nombre a buscar (requerido)

**cURL Command**:
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/search?name=Río" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo con búsqueda parcial**:
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/search?name=Grande" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Estación Río Grande",
    "basinSection": {
      "id": 1,
      "name": "Sección Norte"
    },
    "description": "Estación de monitoreo ubicada en el río Grande",
    "latitude": 14.634915,
    "longitude": -90.506882,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 3,
    "name": "Estación Río Grande Sur",
    "basinSection": {
      "id": 1,
      "name": "Sección Norte"
    },
    "description": "Estación adicional en el río Grande",
    "latitude": 14.635000,
    "longitude": -90.507000,
    "isActive": true,
    "createdAt": "2024-01-15T12:00:00"
  }
]
```

**Errores posibles**:
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

---

## 6. Eliminar Estación de Monitoreo

**Endpoint**: `DELETE /api/monitoring-stations/{id}`

**Descripción**: Elimina una estación de monitoreo. No se puede eliminar si tiene monitoreos asociados.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID de la estación de monitoreo a eliminar

**cURL Command**:
```bash
curl -X DELETE "http://localhost:8080/api/monitoring-stations/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
true
```

**Errores posibles**:
- `404 Not Found`: Estación no encontrada o no pertenece a la corporación
- `409 Conflict`: La estación tiene monitoreos asociados (ResourceInUseException)
- `403 Forbidden`: Usuario no autenticado o sin permisos

**Ejemplo de error cuando la estación está en uso**:
```json
{
  "error": "Cannot delete MonitoringStation with id '1' because it is being used by 5 Monitoring(s)"
}
```

---

## 7. Buscar Estaciones de Monitoreo por Nombre y Sección de Cuenca (con Último Monitoreo)

**Endpoint**: `GET /api/monitoring-stations/find`

**Descripción**: Busca estaciones de monitoreo por nombre (búsqueda parcial) y sección de cuenca, retornando todas las estaciones que cumplan las validaciones con su último monitoreo (fecha más reciente). Requiere rol USER.

**Validaciones**:
- La sección de cuenca debe pertenecer a la corporación del usuario
- La estación debe estar activa (`isActive == true`)
- La estación debe tener al menos un monitoreo

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Query Parameters**:
- `name`: Nombre o parte del nombre de la estación de monitoreo (requerido, búsqueda parcial case-insensitive)
- `basinSectionId`: ID de la sección de cuenca (requerido)

**cURL Command**:
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/find?name=Río Grande&basinSectionId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Nota**: La búsqueda por nombre es parcial, por lo que buscar "Río Grande" encontrará estaciones como "Estación Río Grande", "Estación Río Grande Norte", etc.

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Estación Río Grande",
    "description": "Estación de monitoreo ubicada en el río Grande",
    "latitude": 14.634915,
    "longitude": -90.506882,
    "lastMonitoring": {
      "id": 15,
      "od": 5.250,
      "sst": 12.500,
      "dqo": 8.750,
      "ce": 450.000,
      "ph": 7.200,
      "n": 2.100,
      "p": 0.850,
      "caudalVolumen": 1250.50
    }
  },
  {
    "id": 3,
    "name": "Estación Río Grande Norte",
    "description": "Estación adicional en el río Grande",
    "latitude": 14.635000,
    "longitude": -90.507000,
    "lastMonitoring": {
      "id": 22,
      "od": 4.800,
      "sst": 11.200,
      "dqo": 7.950,
      "ce": 420.000,
      "ph": 7.150,
      "n": 1.950,
      "p": 0.780,
      "caudalVolumen": 1180.25
    }
  }
]
```

**Nota**: La respuesta es un array que puede contener cero o más estaciones. Si no se encuentran estaciones que cumplan todas las validaciones, se retornará un array vacío `[]` o un error según corresponda.

**Errores posibles**:
- `400 Bad Request`: 
  - No se encontraron estaciones activas que cumplan los criterios
  - Parámetros inválidos
- `404 Not Found`: 
  - No se encontraron estaciones que contengan el nombre especificado en la sección de cuenca
  - Sección de cuenca no encontrada o no pertenece a la corporación
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

**Ejemplo de error cuando no hay estaciones activas**:
```json
{
  "error": "No active monitoring stations found"
}
```

**Ejemplo de error cuando ninguna estación pertenece a la corporación**:
```json
{
  "error": "No monitoring stations found that belong to your corporation"
}
```

**Nota**: Si se encuentran estaciones pero ninguna tiene monitoreos registrados, la respuesta será un array vacío `[]` en lugar de un error, ya que las estaciones sin monitoreos se filtran silenciosamente.

---

## Notas Adicionales

### Campos Requeridos para Crear/Actualizar:
- `name`: Requerido, máximo 200 caracteres
- `basinSection.id`: Requerido, debe existir y pertenecer a la corporación del usuario

### Campos Opcionales:
- `description`: Máximo 500 caracteres
- `latitude`: Decimal con precisión 10, escala 8
- `longitude`: Decimal con precisión 11, escala 8
- `isActive`: Boolean, default: true

### Validaciones:
- El nombre debe ser único dentro de la corporación (case-insensitive)
- La sección de cuenca (`basinSection`) debe existir y pertenecer a la corporación del usuario
- No se puede eliminar una estación que tenga monitoreos asociados

### Ordenamiento Disponible:
Los siguientes campos pueden usarse para ordenar en la paginación:
- `name`: Nombre de la estación
- `createdAt`: Fecha de creación
- `isActive`: Estado activo/inactivo
- `latitude`: Latitud
- `longitude`: Longitud

Direcciones de ordenamiento: `asc` (ascendente) o `desc` (descendente)

---

## Ejemplo Completo de Flujo

### 1. Crear una estación
```bash
curl -X POST "http://localhost:8080/api/monitoring-stations" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Estación Nueva",
    "basinSection": {"id": 1},
    "description": "Nueva estación de monitoreo",
    "latitude": 14.634915,
    "longitude": -90.506882,
    "isActive": true
  }'
```

### 2. Obtener todas las estaciones (paginado)
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 3. Buscar por nombre
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/search?name=Nueva" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Obtener por ID
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 5. Actualizar
```bash
curl -X PUT "http://localhost:8080/api/monitoring-stations/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Estación Actualizada",
    "basinSection": {"id": 1},
    "description": "Descripción actualizada",
    "latitude": 14.635000,
    "longitude": -90.507000,
    "isActive": true
  }'
```

### 6. Eliminar (solo si no tiene monitoreos asociados)
```bash
curl -X DELETE "http://localhost:8080/api/monitoring-stations/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 7. Buscar estaciones con último monitoreo (requiere rol USER)
```bash
curl -X GET "http://localhost:8080/api/monitoring-stations/find?name=Río Grande&basinSectionId=1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Nota**: Este endpoint retorna una lista de todas las estaciones que coincidan con el nombre (búsqueda parcial) y cumplan las validaciones.

