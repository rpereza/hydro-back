# Comandos cURL para DepartmentController

Base URL: `http://localhost:8080/api/departments`

**Nota:** Todos los endpoints requieren autenticación con rol `SUPER_ADMIN`. Reemplaza `<JWT_TOKEN>` con tu token JWT válido.

---

## 1. Crear un nuevo departamento

```bash
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Antioquia",
    "code": "AN"
  }'
```

**Campos requeridos:**
- `name`: Nombre del departamento (máximo 100 caracteres, único)
- `code`: Código del departamento (máximo 2 caracteres, único)

**Ejemplo de respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "Antioquia",
  "code": "AN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "municipalities": []
}
```

---

## 2. Actualizar un departamento existente

```bash
curl -X PUT http://localhost:8080/api/departments/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Antioquia",
    "code": "AN"
  }'
```

**Nota:** El `id` en la URL debe coincidir con el ID del departamento a actualizar. El campo `id` en el body será ignorado (se usa el de la URL).

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Antioquia",
  "code": "AN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00",
  "municipalities": []
}
```

---

## 3. Obtener un departamento por ID

```bash
curl -X GET http://localhost:8080/api/departments/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Antioquia",
  "code": "AN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "municipalities": []
}
```

**Respuesta si no existe (404 Not Found):**
```
(No content)
```

---

## 4. Obtener todos los departamentos (con paginación)

```bash
# Página 0, tamaño 10, sin ordenamiento
curl -X GET "http://localhost:8080/api/departments?page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por nombre ascendente
curl -X GET "http://localhost:8080/api/departments?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por código descendente
curl -X GET "http://localhost:8080/api/departments?page=0&size=10&sort=code,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por fecha de creación descendente
curl -X GET "http://localhost:8080/api/departments?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Múltiples criterios de ordenamiento
curl -X GET "http://localhost:8080/api/departments?page=0&size=10&sort=createdAt,desc&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta paginada (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Antioquia",
      "code": "AN",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null,
      "municipalities": []
    },
    {
      "id": 2,
      "name": "Cundinamarca",
      "code": "CU",
      "createdAt": "2024-01-16T09:15:00",
      "updatedAt": null,
      "municipalities": []
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false
    }
  },
  "totalElements": 32,
  "totalPages": 4,
  "last": false,
  "first": true,
  "numberOfElements": 10,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": false
  }
}
```

---

## 5. Obtener todos los departamentos (sin paginación)

```bash
curl -X GET http://localhost:8080/api/departments/all \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Antioquia",
    "code": "AN",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null,
    "municipalities": []
  },
  {
    "id": 2,
    "name": "Cundinamarca",
    "code": "CU",
    "createdAt": "2024-01-16T09:15:00",
    "updatedAt": null,
    "municipalities": []
  },
  {
    "id": 3,
    "name": "Valle del Cauca",
    "code": "VC",
    "createdAt": "2024-01-17T08:20:00",
    "updatedAt": null,
    "municipalities": []
  }
]
```

**Nota:** Este endpoint devuelve una lista completa sin paginación. Úsalo cuando necesites todos los departamentos de una vez.

---

## 6. Buscar un departamento por nombre exacto

```bash
curl -X GET "http://localhost:8080/api/departments/by-name?name=Antioquia" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "Antioquia",
  "code": "AN",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "municipalities": []
}
```

**Respuesta si no existe (404 Not Found):**
```
(No content)
```

**Nota:** Para codificar espacios en la URL, usa `%20` o encierra la URL entre comillas.

---

## 7. Buscar departamentos por nombre (búsqueda parcial)

```bash
# Búsqueda básica
curl -X GET "http://localhost:8080/api/departments/search?name=Anti" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Búsqueda con espacios (usar %20 o comillas)
curl -X GET "http://localhost:8080/api/departments/search?name=Valle%20del" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Antioquia",
    "code": "AN",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": null,
    "municipalities": []
  }
]
```

**Nota:** Este endpoint realiza una búsqueda parcial (LIKE) y devuelve todos los departamentos cuyo nombre contenga el texto buscado. La búsqueda no distingue entre mayúsculas y minúsculas.

---

## Parámetros de Paginación

El endpoint `GET /api/departments` acepta los siguientes parámetros de query:

- `page`: Número de página (empezando en 0). **Por defecto: 0**
- `size`: Tamaño de la página (número de elementos por página). **Por defecto: 20**
- `sort`: Campo(s) por el cual ordenar. Formato: `campo,direccion` donde dirección puede ser `asc` o `desc`
  - Ejemplo simple: `sort=name,asc`
  - Múltiples ordenamientos: `sort=createdAt,desc&sort=name,asc`

### Campos disponibles para ordenamiento:

- `id`: ID del departamento
- `name`: Nombre del departamento
- `code`: Código del departamento
- `createdAt`: Fecha de creación
- `updatedAt`: Fecha de actualización

### Ejemplos de ordenamiento:

```bash
# Ordenar por un solo campo
?sort=name,asc
?sort=code,desc
?sort=createdAt,desc

# Ordenar por múltiples campos
?sort=createdAt,desc&sort=name,asc
?sort=code,asc&sort=name,asc&sort=id,asc
```

---

## Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 OK | Operación exitosa (GET, PUT) |
| 201 Created | Departamento creado exitosamente (POST) |
| 400 Bad Request | Datos inválidos o faltantes |
| 403 Forbidden | Usuario no tiene permisos (rol SUPER_ADMIN requerido) |
| 404 Not Found | Departamento no encontrado |
| 401 Unauthorized | Token JWT inválido o ausente |

---

## Validaciones

### Al crear o actualizar un departamento:

1. **name**: 
   - Requerido (no puede estar vacío)
   - Máximo 100 caracteres
   - Debe ser único en la base de datos

2. **code**: 
   - Requerido (no puede ser null)
   - Máximo 2 caracteres
   - Debe ser único en la base de datos

### Ejemplo de error de validación (400 Bad Request):

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "name",
      "message": "Department name is required"
    },
    {
      "field": "code",
      "message": "Department code cannot exceed 2 characters"
    }
  ]
}
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <JWT_TOKEN>`
2. **Rol requerido**: Todos los endpoints requieren el rol `SUPER_ADMIN`
3. **Content-Type**: Para POST y PUT, siempre incluir `Content-Type: application/json`
4. **Codificación de URL**: En Windows PowerShell, encierra las URLs entre comillas dobles para evitar problemas con caracteres especiales
5. **Nombre único**: El campo `name` debe ser único. Intentar crear un departamento con un nombre existente resultará en error
6. **Código único**: El campo `code` debe ser único. Intentar crear un departamento con un código existente resultará en error
7. **Relaciones**: Los departamentos pueden tener municipios asociados. Al eliminar un departamento, considera las relaciones existentes
8. **Búsqueda parcial**: El endpoint `/search` realiza búsquedas parciales (LIKE) y no distingue entre mayúsculas y minúsculas

---

## Obtener Token JWT

Para obtener un token JWT, primero debes autenticarte:

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "tu_usuario",
    "password": "tu_contraseña"
  }'
```

La respuesta incluirá un token JWT que puedes usar en los headers de los demás endpoints.

**Ejemplo de respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTYxMDYyMzAwMCwiZXhwIjoxNjEwNzA5NDAwfQ...",
  "type": "Bearer",
  "username": "admin",
  "roles": ["SUPER_ADMIN"]
}
```

---

## Ejemplos de Uso Completo

### Flujo completo: Crear, Leer, Actualizar y Buscar

```bash
# 1. Obtener token JWT
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "admin123"
  }' | jq -r '.token')

# 2. Crear un nuevo departamento
curl -X POST http://localhost:8080/api/departments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Antioquia",
    "code": "AN"
  }'

# 3. Obtener todos los departamentos (con paginación)
curl -X GET "http://localhost:8080/api/departments?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 4. Obtener todos los departamentos (sin paginación)
curl -X GET http://localhost:8080/api/departments/all \
  -H "Authorization: Bearer $TOKEN"

# 5. Obtener departamento por ID (asumiendo que se creó con ID 1)
curl -X GET http://localhost:8080/api/departments/1 \
  -H "Authorization: Bearer $TOKEN"

# 6. Buscar departamento por nombre exacto
curl -X GET "http://localhost:8080/api/departments/by-name?name=Antioquia" \
  -H "Authorization: Bearer $TOKEN"

# 7. Buscar departamentos por nombre (búsqueda parcial)
curl -X GET "http://localhost:8080/api/departments/search?name=Anti" \
  -H "Authorization: Bearer $TOKEN"

# 8. Actualizar el departamento
curl -X PUT http://localhost:8080/api/departments/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Antioquia",
    "code": "AN"
  }'
```

**Nota:** El ejemplo anterior usa `jq` para extraer el token. Si no tienes `jq` instalado, puedes copiar el token manualmente de la respuesta JSON.

---

## Comparación de Endpoints de Búsqueda

| Endpoint | Tipo de Búsqueda | Resultado | Paginación |
|----------|------------------|-----------|------------|
| `/api/departments/{id}` | Por ID exacto | Un departamento o 404 | No |
| `/api/departments/by-name?name=...` | Por nombre exacto | Un departamento o 404 | No |
| `/api/departments/search?name=...` | Por nombre parcial (LIKE) | Lista de departamentos | No |
| `/api/departments` | Todos | Página de departamentos | Sí |
| `/api/departments/all` | Todos | Lista completa | No |

---

## Ejemplos de Datos de Prueba

### Departamentos de Colombia (ejemplos):

```json
{
  "name": "Antioquia",
  "code": "AN"
}
```

```json
{
  "name": "Cundinamarca",
  "code": "CU"
}
```

```json
{
  "name": "Valle del Cauca",
  "code": "VC"
}
```

```json
{
  "name": "Atlántico",
  "code": "AT"
}
```

```json
{
  "name": "Santander",
  "code": "SA"
}
```

