# MonitoringController - cURL Examples

Este documento contiene ejemplos de cURL para todos los endpoints del `MonitoringController`.

**Base URL**: `http://localhost:8080/api/monitoring`

**Autenticación**: Todos los endpoints requieren un token JWT en el header `Authorization: Bearer <token>`

**Roles requeridos**: Todos los endpoints requieren el rol `USER`

---

## 1. Crear Monitoreo

**Endpoint**: `POST /api/monitoring`

**Descripción**: Crea un nuevo monitoreo para la corporación del usuario autenticado.

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <JWT_TOKEN>`

**Body** (JSON):
```json
{
  "monitoringStation": {
    "id": 1
  },
  "monitoringDate": "2024-01-15",
  "weatherConditions": "Soleado",
  "waterTemperature": 25.5,
  "airTemperature": 28.0,
  "ph": 7.2,
  "od": 6.5,
  "sst": 50.0,
  "dqo": 120.0,
  "ce": 800.0,
  "caudalVolumen": 1500.50,
  "performedBy": "Juan Pérez",
  "notes": "Monitoreo realizado en condiciones normales"
}
```

**cURL Command**:
```bash
curl -X POST "http://localhost:8080/api/monitoring" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "monitoringStation": {
      "id": 1
    },
    "monitoringDate": "2024-01-15",
    "weatherConditions": "Soleado",
    "waterTemperature": 25.5,
    "airTemperature": 28.0,
    "ph": 7.2,
    "od": 6.5,
    "sst": 50.0,
    "dqo": 120.0,
    "ce": 800.0,
    "caudalVolumen": 1500.50,
    "performedBy": "Juan Pérez",
    "notes": "Monitoreo realizado en condiciones normales"
  }'
```

**Response (201 Created)**:
```json
{
  "id": 1,
  "monitoringStation": {
    "id": 1,
    "name": "Estación Río Grande"
  },
  "monitoringDate": "2024-01-15",
  "weatherConditions": "Soleado",
  "waterTemperature": 25.5,
  "airTemperature": 28.0,
  "ph": 7.2,
  "od": 6.5,
  "sst": 50.0,
  "dqo": 120.0,
  "ce": 800.0,
  "caudalVolumen": 1500.50,
  "performedBy": "Juan Pérez",
  "notes": "Monitoreo realizado en condiciones normales",
  "corporation": {
    "id": 1,
    "name": "Corporación Ejemplo"
  },
  "createdBy": {
    "id": 1,
    "username": "user"
  },
  "createdAt": "2024-01-15T10:30:00"
}
```

**Errores posibles**:
- `400 Bad Request`: Datos inválidos o monitoreo duplicado para la misma estación y fecha
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación
- `409 Conflict`: Conflicto (ej: ya existe un monitoreo para la misma estación y fecha)

---

## 2. Actualizar Monitoreo

**Endpoint**: `PUT /api/monitoring/{id}`

**Descripción**: Actualiza un monitoreo existente de la corporación del usuario.

**Headers**:
- `Content-Type: application/json`
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID del monitoreo a actualizar

**Body** (JSON):
```json
{
  "monitoringStation": {
    "id": 1
  },
  "monitoringDate": "2024-01-15",
  "weatherConditions": "Nublado",
  "waterTemperature": 24.0,
  "airTemperature": 27.5,
  "ph": 7.3,
  "od": 6.8,
  "sst": 55.0,
  "dqo": 125.0,
  "ce": 850.0,
  "caudalVolumen": 1600.75,
  "performedBy": "María González",
  "notes": "Monitoreo actualizado - condiciones mejoradas"
}
```

**cURL Command**:
```bash
curl -X PUT "http://localhost:8080/api/monitoring/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "monitoringStation": {
      "id": 1
    },
    "monitoringDate": "2024-01-15",
    "weatherConditions": "Nublado",
    "waterTemperature": 24.0,
    "airTemperature": 27.5,
    "ph": 7.3,
    "od": 6.8,
    "sst": 55.0,
    "dqo": 125.0,
    "ce": 850.0,
    "ice": 0.85,
    "iph": 0.75,
    "irnp": 0.65,
    "iod": 0.55,
    "isst": 0.45,
    "idqo": 0.35,
    "caudalVolumen": 1600.75,
    "performedBy": "María González",
    "notes": "Monitoreo actualizado - condiciones mejoradas"
  }'
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "monitoringStation": {
    "id": 1,
    "name": "Estación Río Grande"
  },
  "monitoringDate": "2024-01-15",
  "weatherConditions": "Nublado",
  "waterTemperature": 24.0,
  "airTemperature": 27.5,
  "ph": 7.3,
  "od": 6.8,
  "sst": 55.0,
  "dqo": 125.0,
  "ce": 850.0,
  "caudalVolumen": 1600.75,
  "performedBy": "María González",
  "notes": "Monitoreo actualizado - condiciones mejoradas",
  "updatedBy": {
    "id": 1,
    "username": "user"
  },
  "updatedAt": "2024-01-15T11:00:00"
}
```

**Errores posibles**:
- `400 Bad Request`: Datos inválidos
- `404 Not Found`: Monitoreo no encontrado o no pertenece a la corporación del usuario
- `403 Forbidden`: Usuario no autenticado o no tiene permisos
- `409 Conflict`: Conflicto (ej: ya existe otro monitoreo para la misma estación y fecha)

---

## 3. Obtener Monitoreo por ID

**Endpoint**: `GET /api/monitoring/{id}`

**Descripción**: Obtiene un monitoreo específico por su ID de la corporación del usuario.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID del monitoreo a obtener

**cURL Command**:
```bash
curl -X GET "http://localhost:8080/api/monitoring/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "monitoringStation": {
    "id": 1,
    "name": "Estación Río Grande",
    "basinSection": {
      "id": 1,
      "name": "Sección Norte"
    }
  },
  "monitoringDate": "2024-01-15",
  "weatherConditions": "Soleado",
  "waterTemperature": 25.5,
  "airTemperature": 28.0,
  "ph": 7.2,
  "od": 6.5,
  "sst": 50.0,
  "dqo": 120.0,
  "ce": 800.0,
  "caudalVolumen": 1500.50,
  "performedBy": "Juan Pérez",
  "notes": "Monitoreo realizado en condiciones normales",
  "corporation": {
    "id": 1,
    "name": "Corporación Ejemplo"
  },
  "createdBy": {
    "id": 1,
    "username": "user"
  },
  "createdAt": "2024-01-15T10:30:00",
  "updatedBy": {
    "id": 1,
    "username": "user"
  },
  "updatedAt": "2024-01-15T11:00:00"
}
```

**Errores posibles**:
- `404 Not Found`: Monitoreo no encontrado o no pertenece a la corporación del usuario
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

---

## 4. Obtener Todos los Monitoreos de la Corporación (Paginado)

**Endpoint**: `GET /api/monitoring`

**Descripción**: Obtiene todos los monitoreos de la corporación del usuario autenticado con paginación.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Query Parameters**:
- `page`: Número de página (0-indexed, default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo(s) para ordenar (ej: `monitoringDate,desc` o `createdAt,asc`)

**Ejemplo 1: Primera página con tamaño por defecto**
```bash
curl -X GET "http://localhost:8080/api/monitoring?page=0" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 2: Página específica con tamaño personalizado**
```bash
curl -X GET "http://localhost:8080/api/monitoring?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 3: Con ordenamiento por fecha de monitoreo descendente**
```bash
curl -X GET "http://localhost:8080/api/monitoring?page=0&size=20&sort=monitoringDate,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 4: Con ordenamiento múltiple**
```bash
curl -X GET "http://localhost:8080/api/monitoring?page=0&size=20&sort=monitoringDate,desc&sort=createdAt,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "monitoringStation": {
        "id": 1,
        "name": "Estación Río Grande"
      },
      "monitoringDate": "2024-01-15",
      "weatherConditions": "Soleado",
      "waterTemperature": 25.5,
      "airTemperature": 28.0,
      "ph": 7.2,
      "od": 6.5,
      "sst": 50.0,
      "dqo": 120.0,
      "ce": 800.0,
      "ice": 0.8,
      "iph": 0.7,
      "irnp": 0.6,
      "iod": 0.5,
      "isst": 0.4,
      "idqo": 0.3,
      "caudalVolumen": 1500.50,
      "performedBy": "Juan Pérez",
      "notes": "Monitoreo realizado en condiciones normales"
    },
    {
      "id": 2,
      "monitoringStation": {
        "id": 1,
        "name": "Estación Río Grande"
      },
      "monitoringDate": "2024-01-14",
      "weatherConditions": "Lluvioso",
      "waterTemperature": 23.0,
      "airTemperature": 26.5,
      "ph": 7.1,
      "od": 6.2,
      "sst": 48.0,
      "dqo": 115.0,
      "ce": 780.0,
      "caudalVolumen": 1450.25,
      "performedBy": "María González",
      "notes": "Monitoreo con lluvia"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 45,
  "totalPages": 3,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 20,
  "empty": false
}
```

**Errores posibles**:
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

---

## 5. Obtener Monitoreos por Estación (Paginado)

**Endpoint**: `GET /api/monitoring/by-station/{monitoringStationId}`

**Descripción**: Obtiene todos los monitoreos de una estación específica de la corporación del usuario con paginación.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `monitoringStationId`: ID de la estación de monitoreo

**Query Parameters**:
- `page`: Número de página (0-indexed, default: 0)
- `size`: Tamaño de página (default: 20)
- `sort`: Campo(s) para ordenar (ej: `monitoringDate,desc` o `createdAt,asc`)

**Ejemplo 1: Primera página con tamaño por defecto**
```bash
curl -X GET "http://localhost:8080/api/monitoring/by-station/1?page=0" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 2: Página específica con tamaño personalizado**
```bash
curl -X GET "http://localhost:8080/api/monitoring/by-station/1?page=1&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 3: Con ordenamiento por fecha de monitoreo descendente**
```bash
curl -X GET "http://localhost:8080/api/monitoring/by-station/1?page=0&size=20&sort=monitoringDate,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Ejemplo 4: Con ordenamiento múltiple**
```bash
curl -X GET "http://localhost:8080/api/monitoring/by-station/1?page=0&size=20&sort=monitoringDate,desc&sort=createdAt,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
{
  "content": [
    {
      "id": 1,
      "monitoringStation": {
        "id": 1,
        "name": "Estación Río Grande"
      },
      "monitoringDate": "2024-01-15",
      "weatherConditions": "Soleado",
      "waterTemperature": 25.5,
      "airTemperature": 28.0,
      "ph": 7.2,
      "od": 6.5,
      "sst": 50.0,
      "dqo": 120.0,
      "ce": 800.0,
      "ice": 0.8,
      "iph": 0.7,
      "irnp": 0.6,
      "iod": 0.5,
      "isst": 0.4,
      "idqo": 0.3,
      "caudalVolumen": 1500.50,
      "performedBy": "Juan Pérez",
      "notes": "Monitoreo realizado en condiciones normales"
    },
    {
      "id": 2,
      "monitoringStation": {
        "id": 1,
        "name": "Estación Río Grande"
      },
      "monitoringDate": "2024-01-14",
      "weatherConditions": "Lluvioso",
      "waterTemperature": 23.0,
      "airTemperature": 26.5,
      "ph": 7.1,
      "od": 6.2,
      "sst": 48.0,
      "dqo": 115.0,
      "ce": 780.0,
      "caudalVolumen": 1450.25,
      "performedBy": "María González",
      "notes": "Monitoreo con lluvia"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 12,
  "totalPages": 1,
  "last": true,
  "first": true,
  "size": 20,
  "number": 0,
  "numberOfElements": 12,
  "empty": false
}
```

**Errores posibles**:
- `400 Bad Request`: Estación de monitoreo no pertenece a la corporación del usuario
- `403 Forbidden`: Usuario no autenticado o no pertenece a una corporación

---

## 6. Eliminar Monitoreo

**Endpoint**: `DELETE /api/monitoring/{id}`

**Descripción**: Elimina un monitoreo de la corporación del usuario.

**Headers**:
- `Authorization: Bearer <JWT_TOKEN>`

**Path Parameters**:
- `id`: ID del monitoreo a eliminar

**cURL Command**:
```bash
curl -X DELETE "http://localhost:8080/api/monitoring/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response (200 OK)**:
```json
true
```

**Errores posibles**:
- `404 Not Found`: Monitoreo no encontrado o no pertenece a la corporación del usuario
- `403 Forbidden`: Usuario no autenticado o no tiene permisos

---

## Notas Adicionales

### Campos Requeridos en POST/PUT

Los siguientes campos son **obligatorios** al crear o actualizar un monitoreo:
- `monitoringStation.id`: ID de la estación de monitoreo
- `monitoringDate`: Fecha del monitoreo (formato: YYYY-MM-DD)
- `ph`: Valor de pH (BigDecimal)
- `od`: Valor de OD (BigDecimal)
- `sst`: Valor de SST (BigDecimal)
- `dqo`: Valor de DQO (BigDecimal)
- `ce`: Valor de CE (BigDecimal)
- `caudalVolumen`: Volumen de caudal (BigDecimal)

### Campos Opcionales

Los siguientes campos son **opcionales**:
- `weatherConditions`: Condiciones climáticas (String)
- `waterTemperature`: Temperatura del agua (Double)
- `airTemperature`: Temperatura del aire (Double)
- `n`: Valor de N (BigDecimal)
- `p`: Valor de P (BigDecimal)
- `rnp`: Valor de RNP (BigDecimal)
- `performedBy`: Persona que realizó el monitoreo (String)
- `notes`: Notas adicionales (String)

### Campos Calculados (No enviar ni esperar en respuestas)

Los siguientes campos son **calculados automáticamente** en la capa de servicio y **NO deben** ser incluidos en las solicitudes POST/PUT ni aparecerán en las respuestas JSON:
- `iod`: Índice de OD (calculado)
- `isst`: Índice de SST (calculado)
- `idqo`: Índice de DQO (calculado)
- `ice`: Índice de CE (calculado)
- `iph`: Índice de pH (calculado)
- `irnp`: Índice de RNP (calculado)

Estos campos se calcularán y almacenarán automáticamente en la base de datos cuando se cree o actualice un monitoreo.

### Validaciones

1. **Unicidad**: No puede existir más de un monitoreo para la misma estación y fecha.
2. **Corporación**: La estación de monitoreo debe pertenecer a la corporación del usuario autenticado.
3. **Permisos**: Solo se pueden crear, actualizar o eliminar monitoreos de la propia corporación.

### Ordenamiento Disponible

Los siguientes campos pueden ser usados para ordenar en los parámetros `sort`:
- `monitoringDate`: Fecha del monitoreo
- `createdAt`: Fecha de creación
- `updatedAt`: Fecha de actualización
- `waterTemperature`: Temperatura del agua
- `airTemperature`: Temperatura del aire
- `ph`: Valor de pH

Ejemplo de ordenamiento múltiple:
```
?sort=monitoringDate,desc&sort=createdAt,asc
```

