# Comandos cURL para CategoryController

Base URL: `http://localhost:8080/api/categories`

**Nota:** Todos los endpoints requieren autenticación con rol `SUPER_ADMIN`. Reemplaza `<JWT_TOKEN>` con tu token JWT válido.

---

## 1. Crear una nueva categoría

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "CAT1",
    "value": 150.50
  }'
```

**Campos requeridos:**
- `name`: Nombre de la categoría (máximo 10 caracteres, único)
- `value`: Valor de la categoría (BigDecimal con precisión 5, escala 2)

**Ejemplo de respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "name": "CAT1",
  "value": 150.50,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null,
  "municipalities": []
}
```

---

## 2. Actualizar una categoría existente

```bash
curl -X PUT http://localhost:8080/api/categories/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "CAT1",
    "value": 175.75
  }'
```

**Nota:** El `id` en la URL debe coincidir con el ID de la categoría a actualizar. El campo `id` en el body será ignorado (se usa el de la URL).

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "CAT1",
  "value": 175.75,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:45:00",
  "municipalities": []
}
```

---

## 3. Obtener una categoría por ID

```bash
curl -X GET http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "CAT1",
  "value": 150.50,
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

## 4. Obtener todas las categorías (con paginación)

```bash
# Página 0, tamaño 10, sin ordenamiento
curl -X GET "http://localhost:8080/api/categories?page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por nombre ascendente
curl -X GET "http://localhost:8080/api/categories?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por valor descendente
curl -X GET "http://localhost:8080/api/categories?page=0&size=10&sort=value,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por fecha de creación descendente
curl -X GET "http://localhost:8080/api/categories?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Múltiples criterios de ordenamiento
curl -X GET "http://localhost:8080/api/categories?page=0&size=10&sort=createdAt,desc&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta paginada (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "CAT1",
      "value": 150.50,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null,
      "municipalities": []
    },
    {
      "id": 2,
      "name": "CAT2",
      "value": 200.00,
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
  "totalElements": 25,
  "totalPages": 3,
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

## 5. Eliminar una categoría

```bash
curl -X DELETE http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Ejemplo de respuesta exitosa (200 OK):**
```json
true
```

**Respuesta si no existe (404 Not Found):**
```
(No content)
```

**Nota:** La eliminación puede fallar si la categoría tiene municipios asociados (dependiendo de la configuración de cascada).

---

## Parámetros de Paginación

Todos los endpoints que soportan paginación aceptan los siguientes parámetros de query:

- `page`: Número de página (empezando en 0). **Por defecto: 0**
- `size`: Tamaño de la página (número de elementos por página). **Por defecto: 20**
- `sort`: Campo(s) por el cual ordenar. Formato: `campo,direccion` donde dirección puede ser `asc` o `desc`
  - Ejemplo simple: `sort=name,asc`
  - Múltiples ordenamientos: `sort=createdAt,desc&sort=name,asc`

### Campos disponibles para ordenamiento:

- `id`: ID de la categoría
- `name`: Nombre de la categoría
- `value`: Valor de la categoría
- `createdAt`: Fecha de creación
- `updatedAt`: Fecha de actualización

### Ejemplos de ordenamiento:

```bash
# Ordenar por un solo campo
?sort=name,asc
?sort=value,desc
?sort=createdAt,desc

# Ordenar por múltiples campos
?sort=createdAt,desc&sort=name,asc
?sort=value,desc&sort=name,asc&sort=id,asc
```

---

## Códigos de Estado HTTP

| Código | Descripción |
|--------|-------------|
| 200 OK | Operación exitosa (GET, PUT, DELETE) |
| 201 Created | Categoría creada exitosamente (POST) |
| 400 Bad Request | Datos inválidos o faltantes |
| 403 Forbidden | Usuario no tiene permisos (rol SUPER_ADMIN requerido) |
| 404 Not Found | Categoría no encontrada |
| 401 Unauthorized | Token JWT inválido o ausente |

---

## Validaciones

### Al crear o actualizar una categoría:

1. **name**: 
   - Requerido (no puede estar vacío)
   - Máximo 10 caracteres
   - Debe ser único en la base de datos

2. **value**: 
   - Requerido (no puede ser null)
   - Debe ser un número decimal válido
   - Precisión: 5 dígitos totales, 2 decimales

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
      "message": "Category name is required"
    },
    {
      "field": "value",
      "message": "Value is required"
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
5. **Nombre único**: El campo `name` debe ser único. Intentar crear una categoría con un nombre existente resultará en error
6. **Relaciones**: Las categorías pueden tener municipios asociados. Al eliminar una categoría, considera las relaciones existentes

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

### Flujo completo: Crear, Leer, Actualizar y Eliminar

```bash
# 1. Obtener token JWT
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "admin123"
  }' | jq -r '.token')

# 2. Crear una nueva categoría
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "CAT1",
    "value": 150.50
  }'

# 3. Obtener todas las categorías
curl -X GET "http://localhost:8080/api/categories?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN"

# 4. Obtener categoría por ID (asumiendo que se creó con ID 1)
curl -X GET http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer $TOKEN"

# 5. Actualizar la categoría
curl -X PUT http://localhost:8080/api/categories/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "CAT1",
    "value": 200.00
  }'

# 6. Eliminar la categoría
curl -X DELETE http://localhost:8080/api/categories/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Nota:** El ejemplo anterior usa `jq` para extraer el token. Si no tienes `jq` instalado, puedes copiar el token manualmente de la respuesta JSON.

