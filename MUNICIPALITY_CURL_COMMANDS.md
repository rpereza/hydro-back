# Comandos cURL para MunicipalityController

Base URL: `http://localhost:8080/api/municipalities`

**Nota:** La mayoría de los endpoints requieren autenticación con rol `SUPER_ADMIN`. El endpoint para obtener municipios por departamento requiere rol `USER`. Reemplaza `<JWT_TOKEN>` con tu token JWT válido.

---

## 1. Crear un nuevo municipio

```bash
curl -X POST http://localhost:8080/api/municipalities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1
    },
    "category": {
      "id": 1
    },
    "nbi": 12.50,
    "isActive": true
  }'
```

**Campos requeridos:**
- `name`: Nombre del municipio (máximo 50 caracteres, requerido)
- `code`: Código del municipio (máximo 3 caracteres, único, requerido)
- `department`: Objeto con `id` del departamento (requerido)
- `category`: Objeto con `id` de la categoría (requerido)

**Campos opcionales:**
- `nbi`: Necesidades Básicas Insatisfechas (BigDecimal, opcional)
- `isActive`: Estado activo del municipio (boolean, por defecto: true)

**Ejemplo de respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Medellín",
  "code": "001",
  "department": {
    "id": 1,
    "name": "Antioquia",
    "code": "AN"
  },
  "category": {
    "id": 1,
    "name": "Categoría A",
    "code": "CAT-A"
  },
  "nbi": 12.50,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Códigos de respuesta:**
- `201 Created`: Municipio creado exitosamente
- `400 Bad Request`: Datos inválidos o código duplicado
- `403 Forbidden`: Usuario no tiene permisos o no pertenece a una corporación

---

## 2. Actualizar un municipio existente

```bash
curl -X PUT http://localhost:8080/api/municipalities/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1
    },
    "category": {
      "id": 1
    },
    "nbi": 15.75,
    "isActive": true
  }'
```

**Parámetros:**
- `{id}`: ID del municipio a actualizar (en la URL)

**Campos requeridos:**
- `name`: Nombre del municipio (máximo 50 caracteres)
- `code`: Código del municipio (máximo 3 caracteres, único)
- `department`: Objeto con `id` del departamento
- `category`: Objeto con `id` de la categoría

**Campos opcionales:**
- `nbi`: Necesidades Básicas Insatisfechas (BigDecimal)
- `isActive`: Estado activo del municipio (boolean)

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Medellín",
  "code": "001",
  "department": {
    "id": 1,
    "name": "Antioquia",
    "code": "AN"
  },
  "category": {
    "id": 1,
    "name": "Categoría A",
    "code": "CAT-A"
  },
  "nbi": 15.75,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00"
}
```

**Códigos de respuesta:**
- `200 OK`: Municipio actualizado exitosamente
- `400 Bad Request`: Datos inválidos o código duplicado
- `404 Not Found`: Municipio no encontrado
- `403 Forbidden`: Usuario no tiene permisos

---

## 3. Obtener un municipio por ID

```bash
curl -X GET http://localhost:8080/api/municipalities/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros:**
- `{id}`: ID del municipio a obtener (en la URL)

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Medellín",
  "code": "001",
  "department": {
    "id": 1,
    "name": "Antioquia",
    "code": "AN"
  },
  "category": {
    "id": 1,
    "name": "Categoría A",
    "code": "CAT-A"
  },
  "nbi": 12.50,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Códigos de respuesta:**
- `200 OK`: Municipio encontrado
- `404 Not Found`: Municipio no encontrado
- `403 Forbidden`: Usuario no tiene permisos

---

## 4. Obtener todos los municipios con paginación

```bash
curl -X GET "http://localhost:8080/api/municipalities?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros de consulta (query parameters):**
- `page`: Número de página (por defecto: 0)
- `size`: Tamaño de la página (por defecto: 20)
- `sort`: Campo(s) para ordenar (formato: `campo,direccion` o `campo1,direccion1&sort=campo2,direccion2`)

**Ejemplos de parámetros de ordenamiento:**
- `sort=name,asc` - Ordenar por nombre ascendente
- `sort=name,desc` - Ordenar por nombre descendente
- `sort=createdAt,desc` - Ordenar por fecha de creación descendente
- `sort=name,asc&sort=code,asc` - Ordenar por nombre y luego por código

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Medellín",
      "code": "001",
      "department": {
        "id": 1,
        "name": "Antioquia",
        "code": "AN"
      },
      "category": {
        "id": 1,
        "name": "Categoría A",
        "code": "CAT-A"
      },
      "nbi": 12.50,
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
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
  "totalElements": 1,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 1,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "empty": false
}
```

**Códigos de respuesta:**
- `200 OK`: Lista de municipios obtenida exitosamente
- `403 Forbidden`: Usuario no tiene permisos

---

## 5. Obtener todos los municipios (sin paginación)

```bash
curl -X GET http://localhost:8080/api/municipalities/all \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1,
      "name": "Antioquia",
      "code": "AN"
    },
    "category": {
      "id": 1,
      "name": "Categoría A",
      "code": "CAT-A"
    },
    "nbi": 12.50,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "name": "Bogotá",
    "code": "002",
    "department": {
      "id": 2,
      "name": "Cundinamarca",
      "code": "CU"
    },
    "category": {
      "id": 1,
      "name": "Categoría A",
      "code": "CAT-A"
    },
    "nbi": 10.25,
    "isActive": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

**Códigos de respuesta:**
- `200 OK`: Lista de municipios obtenida exitosamente
- `403 Forbidden`: Usuario no tiene permisos

---

## 6. Buscar un municipio por nombre exacto

```bash
curl -X GET "http://localhost:8080/api/municipalities/by-name?name=Medellín" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros de consulta:**
- `name`: Nombre exacto del municipio a buscar (requerido)

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Medellín",
  "code": "001",
  "department": {
    "id": 1,
    "name": "Antioquia",
    "code": "AN"
  },
  "category": {
    "id": 1,
    "name": "Categoría A",
    "code": "CAT-A"
  },
  "nbi": 12.50,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Códigos de respuesta:**
- `200 OK`: Municipio encontrado
- `404 Not Found`: Municipio no encontrado
- `403 Forbidden`: Usuario no tiene permisos

---

## 7. Buscar municipios por nombre (búsqueda parcial)

```bash
curl -X GET "http://localhost:8080/api/municipalities/search?name=Med" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros de consulta:**
- `name`: Nombre o parte del nombre del municipio a buscar (requerido, búsqueda case-insensitive)

**Ejemplo de respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1,
      "name": "Antioquia",
      "code": "AN"
      },
    "category": {
      "id": 1,
      "name": "Categoría A",
      "code": "CAT-A"
    },
    "nbi": 12.50,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

**Nota:** Esta búsqueda es parcial y case-insensitive. Por ejemplo, buscar "med" encontrará "Medellín", "Medina", etc.

**Códigos de respuesta:**
- `200 OK`: Lista de municipios encontrados (puede estar vacía)
- `403 Forbidden`: Usuario no tiene permisos

---

## 8. Obtener municipios por departamento

```bash
curl -X GET http://localhost:8080/api/municipalities/by-department/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros:**
- `{departmentId}`: ID del departamento (en la URL)

**Rol requerido:** `USER` o `ADMIN` (no requiere `SUPER_ADMIN`)

**Descripción:** Obtiene todos los municipios activos que pertenecen al departamento especificado.

**Ejemplo de respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1,
      "name": "Antioquia",
      "code": "AN"
    },
    "category": {
      "id": 1,
      "name": "Categoría A",
      "code": "CAT-A"
    },
    "nbi": 12.50,
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "name": "Envigado",
    "code": "002",
    "department": {
      "id": 1,
      "name": "Antioquia",
      "code": "AN"
    },
    "category": {
      "id": 1,
      "name": "Categoría A",
      "code": "CAT-A"
    },
    "nbi": 8.30,
    "isActive": true,
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  }
]
```

**Nota:** Este endpoint solo retorna municipios activos (`isActive = true`). Los municipios inactivos no se incluyen en la respuesta.

**Códigos de respuesta:**
- `200 OK`: Lista de municipios activos del departamento (puede estar vacía si no hay municipios activos)
- `403 Forbidden`: Usuario no tiene permisos

---

## Ejemplos de uso completo

### Ejemplo 1: Crear y luego buscar un municipio

```bash
# 1. Crear un municipio
curl -X POST http://localhost:8080/api/municipalities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Envigado",
    "code": "002",
    "department": {
      "id": 1
    },
    "category": {
      "id": 1
    },
    "nbi": 8.30,
    "isActive": true
  }'

# 2. Buscar el municipio creado por nombre exacto
curl -X GET "http://localhost:8080/api/municipalities/by-name?name=Envigado" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# 3. Buscar municipios que contengan "env" en el nombre
curl -X GET "http://localhost:8080/api/municipalities/search?name=env" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Ejemplo 2: Obtener municipios con diferentes opciones de paginación

```bash
# Primera página con 5 elementos, ordenados por nombre
curl -X GET "http://localhost:8080/api/municipalities?page=0&size=5&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Segunda página con 10 elementos, ordenados por fecha de creación descendente
curl -X GET "http://localhost:8080/api/municipalities?page=1&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Todos los municipios sin paginación
curl -X GET http://localhost:8080/api/municipalities/all \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Obtener municipios activos de un departamento específico
curl -X GET http://localhost:8080/api/municipalities/by-department/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Ejemplo 3: Actualizar un municipio

```bash
# Obtener el municipio primero
curl -X GET http://localhost:8080/api/municipalities/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Actualizar el municipio
curl -X PUT http://localhost:8080/api/municipalities/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Medellín",
    "code": "001",
    "department": {
      "id": 1
    },
    "category": {
      "id": 2
    },
    "nbi": 14.25,
    "isActive": true
  }'
```

---

## Notas importantes

1. **Autenticación:** 
   - La mayoría de los endpoints requieren un token JWT válido con el rol `SUPER_ADMIN`.
   - El endpoint `/by-department/{departmentId}` requiere rol `USER` o `ADMIN` (no requiere `SUPER_ADMIN`).

2. **Código único:** El campo `code` debe ser único en todo el sistema. Si intentas crear un municipio con un código que ya existe, recibirás un error 400.

3. **Relaciones:** Los campos `department` y `category` deben hacer referencia a entidades existentes que pertenezcan a la misma corporación del usuario autenticado.

4. **Validaciones:**
   - `name`: Máximo 50 caracteres, requerido
   - `code`: Máximo 3 caracteres, único, requerido
   - `department`: Requerido, debe existir y pertenecer a la corporación
   - `category`: Requerido, debe existir y pertenecer a la corporación
   - `nbi`: Opcional, tipo BigDecimal
   - `isActive`: Opcional, por defecto true

5. **Búsqueda parcial:** El endpoint `/search` realiza una búsqueda case-insensitive que encuentra municipios cuyo nombre contiene la cadena proporcionada.

6. **Paginación:** El endpoint principal (`GET /api/municipalities`) soporta paginación estándar de Spring Data con parámetros `page`, `size` y `sort`.

7. **Municipios por departamento:** El endpoint `/by-department/{departmentId}` retorna solo municipios activos del departamento especificado. Este endpoint es útil para llenar listas desplegables en formularios donde se necesita filtrar municipios por departamento.

---

## Errores comunes y soluciones

### Error 400 Bad Request
- **Causa:** Datos inválidos, código duplicado, o departamento/categoría no pertenece a la corporación
- **Solución:** Verifica que todos los campos requeridos estén presentes y que el código sea único

### Error 403 Forbidden
- **Causa:** Usuario no autenticado, token inválido, o usuario no tiene rol SUPER_ADMIN
- **Solución:** Verifica que el token JWT sea válido y que el usuario tenga el rol correcto

### Error 404 Not Found
- **Causa:** Municipio no encontrado
- **Solución:** Verifica que el ID del municipio sea correcto

---

## Obtener token JWT

Para obtener un token JWT, primero debes autenticarte:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "tu_usuario",
    "password": "tu_contraseña"
  }'
```

La respuesta incluirá un token JWT que puedes usar en el header `Authorization: Bearer <JWT_TOKEN>`.

