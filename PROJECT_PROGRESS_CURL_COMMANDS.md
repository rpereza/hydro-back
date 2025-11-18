# Project Progress Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `ProjectProgressController`.

**Base URL:** `http://localhost:8080/api/project-progress`

**Nota:** Todos los endpoints requieren autenticación JWT. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Progreso de Proyecto

**Endpoint:** `POST /api/project-progress`  
**Rol requerido:** `USER`  
**Descripción:** Crea un nuevo progreso de proyecto para la corporación del usuario autenticado.

```bash
curl -X POST "http://localhost:8080/api/project-progress" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "year": 2024,
    "cciPercentage": 75.50,
    "cevPercentage": 80.25,
    "cdsPercentage": 70.00,
    "ccsPercentage": 85.75
  }'
```

**Campos del request:**
- `dischargeUser`: Objeto con el ID del usuario de descarga (requerido)
  - `id`: ID del usuario de descarga (Long, requerido)
- `year`: Año del progreso (requerido, Integer)
- `cciPercentage`: Porcentaje CCI (requerido, BigDecimal, precisión 7, escala 2)
- `cevPercentage`: Porcentaje CEV (requerido, BigDecimal, precisión 7, escala 2)
- `cdsPercentage`: Porcentaje CDS (requerido, BigDecimal, precisión 7, escala 2)
- `ccsPercentage`: Porcentaje CCS (requerido, BigDecimal, precisión 7, escala 2)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "dischargeUser": {
    "id": 1,
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez",
    "email": "contacto@empresa.com",
    "phone": "3001234567",
    "isActive": true
  },
  "year": 2024,
  "cciPercentage": 75.50,
  "cevPercentage": 80.25,
  "cdsPercentage": 70.00,
  "ccsPercentage": 85.75,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `400 Bad Request`: Datos inválidos, validación fallida, o ya existe un progreso para el mismo usuario de descarga y año en la corporación
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

**Nota:** No puede existir más de un progreso de proyecto para el mismo usuario de descarga y año dentro de la misma corporación.

---

## 2. Actualizar Progreso de Proyecto

**Endpoint:** `PUT /api/project-progress/{id}`  
**Rol requerido:** `USER`  
**Descripción:** Actualiza un progreso de proyecto existente.

```bash
curl -X PUT "http://localhost:8080/api/project-progress/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "year": 2024,
    "cciPercentage": 78.00,
    "cevPercentage": 82.50,
    "cdsPercentage": 72.25,
    "ccsPercentage": 87.00
  }'
```

**Campos del request:**
- `dischargeUser`: Objeto con el ID del usuario de descarga (requerido)
  - `id`: ID del usuario de descarga (Long, requerido)
- `year`: Año del progreso (requerido, Integer)
- `cciPercentage`: Porcentaje CCI (requerido, BigDecimal)
- `cevPercentage`: Porcentaje CEV (requerido, BigDecimal)
- `cdsPercentage`: Porcentaje CDS (requerido, BigDecimal)
- `ccsPercentage`: Porcentaje CCS (requerido, BigDecimal)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "dischargeUser": {
    "id": 1,
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001"
  },
  "year": 2024,
  "cciPercentage": 78.00,
  "cevPercentage": 82.50,
  "cdsPercentage": 72.25,
  "ccsPercentage": 87.00,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-16T14:20:00"
}
```

**Errores posibles:**
- `400 Bad Request`: Datos inválidos, validación fallida, o ya existe otro progreso para el mismo usuario de descarga y año en la corporación
- `403 Forbidden`: El progreso no pertenece a la corporación del usuario o el usuario no tiene permisos
- `404 Not Found`: El progreso de proyecto no existe

---

## 3. Obtener Progreso de Proyecto por ID

**Endpoint:** `GET /api/project-progress/{id}`  
**Rol requerido:** `USER`  
**Descripción:** Obtiene un progreso de proyecto específico por su ID.

```bash
curl -X GET "http://localhost:8080/api/project-progress/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros:**
- `id`: ID del progreso de proyecto (Long, requerido en la URL)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "dischargeUser": {
    "id": 1,
    "companyName": "Empresa Ejemplo S.A.",
    "code": "EMP001",
    "documentNumber": "900123456-7",
    "documentType": "NIT",
    "contactPerson": "Juan Pérez",
    "email": "contacto@empresa.com",
    "phone": "3001234567",
    "isActive": true
  },
  "year": 2024,
  "cciPercentage": 75.50,
  "cevPercentage": 80.25,
  "cdsPercentage": 70.00,
  "ccsPercentage": 85.75,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `403 Forbidden`: El progreso no pertenece a la corporación del usuario o el usuario no tiene permisos
- `404 Not Found`: El progreso de proyecto no existe

---

## 4. Obtener Todos los Progresos de Proyecto (Paginado)

**Endpoint:** `GET /api/project-progress`  
**Rol requerido:** `USER`  
**Descripción:** Obtiene todos los progresos de proyecto de la corporación del usuario autenticado con paginación.

### 4.1. Obtener primera página (10 elementos por defecto)

```bash
curl -X GET "http://localhost:8080/api/project-progress?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.2. Obtener segunda página con 20 elementos

```bash
curl -X GET "http://localhost:8080/api/project-progress?page=1&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.3. Obtener con ordenamiento por año descendente

```bash
curl -X GET "http://localhost:8080/api/project-progress?page=0&size=10&sort=year,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4.4. Obtener con ordenamiento por año ascendente y luego por CCI descendente

```bash
curl -X GET "http://localhost:8080/api/project-progress?page=0&size=10&sort=year,asc&sort=cciPercentage,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de consulta:**
- `page`: Número de página (0-indexed, opcional, default: 0)
- `size`: Tamaño de la página (opcional, default: 20)
- `sort`: Campo(s) para ordenar (opcional, formato: `campo,direccion`)
  - Direcciones válidas: `asc` (ascendente) o `desc` (descendente)
  - Puedes especificar múltiples campos: `&sort=campo1,asc&sort=campo2,desc`

**Campos ordenables:**
- `id`
- `year`
- `cciPercentage`
- `cevPercentage`
- `cdsPercentage`
- `ccsPercentage`
- `createdAt`
- `updatedAt`

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "dischargeUser": {
        "id": 1,
        "companyName": "Empresa Ejemplo S.A.",
        "code": "EMP001"
      },
      "year": 2024,
      "cciPercentage": 75.50,
      "cevPercentage": 80.25,
      "cdsPercentage": 70.00,
      "ccsPercentage": 85.75,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "dischargeUser": {
        "id": 2,
        "companyName": "Otra Empresa S.A.",
        "code": "EMP002"
      },
      "year": 2024,
      "cciPercentage": 82.30,
      "cevPercentage": 78.50,
      "cdsPercentage": 75.00,
      "ccsPercentage": 80.25,
      "createdAt": "2024-01-16T09:15:00",
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
  "empty": false
}
```

**Errores posibles:**
- `403 Forbidden`: El usuario no tiene permisos o no pertenece a una corporación

---

## 5. Eliminar Progreso de Proyecto

**Endpoint:** `DELETE /api/project-progress/{id}`  
**Rol requerido:** `USER`  
**Descripción:** Elimina un progreso de proyecto.

```bash
curl -X DELETE "http://localhost:8080/api/project-progress/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros:**
- `id`: ID del progreso de proyecto a eliminar (Long, requerido en la URL)

**Respuesta exitosa (200 OK):**
```json
true
```

**Errores posibles:**
- `403 Forbidden`: El progreso no pertenece a la corporación del usuario o el usuario no tiene permisos
- `404 Not Found`: El progreso de proyecto no existe

---

## Ejemplo Completo de Flujo

### 1. Autenticarse y obtener token

```bash
# Login
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "usuario@ejemplo.com",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "usuario",
  "email": "usuario@ejemplo.com",
  "roles": ["USER"]
}
```

### 2. Crear un progreso de proyecto

```bash
curl -X POST "http://localhost:8080/api/project-progress" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "year": 2024,
    "cciPercentage": 75.50,
    "cevPercentage": 80.25,
    "cdsPercentage": 70.00,
    "ccsPercentage": 85.75
  }'
```

### 3. Obtener todos los progresos con paginación

```bash
curl -X GET "http://localhost:8080/api/project-progress?page=0&size=10&sort=year,desc" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 4. Obtener un progreso específico

```bash
curl -X GET "http://localhost:8080/api/project-progress/1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 5. Actualizar el progreso

```bash
curl -X PUT "http://localhost:8080/api/project-progress/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "year": 2024,
    "cciPercentage": 78.00,
    "cevPercentage": 82.50,
    "cdsPercentage": 72.25,
    "ccsPercentage": 87.00
  }'
```

### 6. Eliminar el progreso

```bash
curl -X DELETE "http://localhost:8080/api/project-progress/1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <token>`.

2. **Roles requeridos**: Todos los endpoints requieren el rol `USER`.

3. **Corporación**: Los progresos de proyecto están asociados a la corporación del usuario autenticado. Solo puedes ver/modificar/eliminar progresos de tu propia corporación.

4. **Validaciones**:
   - No puede existir más de un progreso para el mismo usuario de descarga y año dentro de la misma corporación.
   - Todos los porcentajes (CCI, CEV, CDS, CCS) son requeridos y deben tener precisión de 7 dígitos y escala de 2 decimales.

5. **Paginación**: El endpoint de listado soporta paginación estándar de Spring Data con parámetros `page`, `size` y `sort`.

6. **Formato de fechas**: Las fechas se devuelven en formato ISO 8601 (`yyyy-MM-ddTHH:mm:ss`).

