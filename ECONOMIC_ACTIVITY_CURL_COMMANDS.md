# Comandos cURL para EconomicActivityController

Base URL: `http://localhost:8080/api/economic-activities`

**Nota:** La mayoría de los endpoints requieren autenticación con rol `SUPER_ADMIN`. El endpoint de búsqueda unificada (`/search-unified`) requiere rol `USER`. Reemplaza `<JWT_TOKEN>` con tu token JWT válido.

---

## 1. Crear una nueva actividad económica

```bash
curl -X POST http://localhost:8080/api/economic-activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Agricultura",
    "code": "AGR1"
  }'
```

---

## 2. Actualizar una actividad económica existente

```bash
curl -X PUT http://localhost:8080/api/economic-activities/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -d '{
    "name": "Agricultura Actualizada",
    "code": "AGR1",
    "active": true
  }'
```

---

## 3. Obtener una actividad económica por ID

```bash
curl -X GET http://localhost:8080/api/economic-activities/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 4. Obtener todas las actividades económicas (con paginación)

```bash
# Página 0, tamaño 10, sin ordenamiento
curl -X GET "http://localhost:8080/api/economic-activities?page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por nombre ascendente
curl -X GET "http://localhost:8080/api/economic-activities?page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por código descendente
curl -X GET "http://localhost:8080/api/economic-activities?page=0&size=10&sort=code,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Múltiples criterios de ordenamiento
curl -X GET "http://localhost:8080/api/economic-activities?page=0&size=10&sort=createdAt,desc&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 5. Buscar una actividad económica por nombre exacto

```bash
curl -X GET "http://localhost:8080/api/economic-activities/by-name?name=Agricultura" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 6. Buscar actividades económicas por nombre (búsqueda parcial con paginación)

```bash
# Búsqueda básica
curl -X GET "http://localhost:8080/api/economic-activities/search?name=Agri&page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento
curl -X GET "http://localhost:8080/api/economic-activities/search?name=Agri&page=0&size=10&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 7. Buscar actividades económicas por código que comienza con (con paginación)

```bash
# Búsqueda básica
curl -X GET "http://localhost:8080/api/economic-activities/search-by-code?code=AGR&page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por código
curl -X GET "http://localhost:8080/api/economic-activities/search-by-code?code=AGR&page=0&size=10&sort=code,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 8. Búsqueda unificada de actividades económicas por código o nombre (con paginación)

**Endpoint:** `GET /api/economic-activities/search-unified`  
**Rol requerido:** `USER` o `ADMIN` (no requiere `SUPER_ADMIN`)  
**Descripción:** Búsqueda unificada que busca simultáneamente en los campos `code` y `name`. Útil para componentes de autocompletado o búsqueda rápida en la interfaz de usuario.

```bash
# Búsqueda básica (busca en código o nombre)
curl -X GET "http://localhost:8080/api/economic-activities/search-unified?query=AGR&page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Búsqueda por nombre parcial
curl -X GET "http://localhost:8080/api/economic-activities/search-unified?query=Agric&page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Búsqueda por código parcial
curl -X GET "http://localhost:8080/api/economic-activities/search-unified?query=123&page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por código y luego por nombre
curl -X GET "http://localhost:8080/api/economic-activities/search-unified?query=AGR&page=0&size=20&sort=code,asc&sort=name,asc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Parámetros de consulta:**
- `query`: Término de búsqueda que se busca tanto en el código como en el nombre (requerido, búsqueda parcial case-insensitive)
- `page`: Número de página (default: 0)
- `size`: Tamaño de la página (default: 20)
- `sort`: Campo(s) para ordenar (opcional, por defecto ordena por código y nombre ascendente)

**Características:**
- La búsqueda es **parcial** y **case-insensitive** (no distingue mayúsculas/minúsculas)
- Busca simultáneamente en los campos `code` y `name`
- Retorna resultados ordenados por código y luego por nombre (por defecto)
- Ideal para componentes de autocompletado o búsqueda rápida en la UI

**Ejemplo de respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Agricultura",
      "code": "AGR1",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    },
    {
      "id": 2,
      "name": "Agricultura y Ganadería",
      "code": "AGR2",
      "isActive": true,
      "createdAt": "2024-01-15T11:00:00",
      "updatedAt": null
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false
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
    "sorted": true,
    "unsorted": false
  },
  "empty": false
}
```

**Códigos de respuesta:**
- `200 OK`: Búsqueda exitosa
- `403 Forbidden`: Usuario no tiene permisos

**Nota:** Este endpoint es especialmente útil para implementar componentes de búsqueda/autocompletado en el frontend, ya que permite buscar por código o nombre con un solo parámetro.

---

## 9. Obtener actividades económicas por rango de fechas (con paginación)

```bash
# Formato de fecha: YYYY-MM-DDTHH:mm:ss (ISO 8601)
curl -X GET "http://localhost:8080/api/economic-activities/by-date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con ordenamiento por fecha de creación
curl -X GET "http://localhost:8080/api/economic-activities/by-date-range?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59&page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Nota:** Para codificar espacios en la URL, usa `%20` o encierra la URL entre comillas.

---

## 10. Eliminar una actividad económica

```bash
curl -X DELETE http://localhost:8080/api/economic-activities/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 11. Verificar si existe una actividad económica por nombre

```bash
curl -X GET "http://localhost:8080/api/economic-activities/exists?name=Agricultura" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Respuesta:** `true` o `false`

---

## 12. Obtener actividades económicas más recientes (con paginación)

```bash
# Página 0, tamaño 10
curl -X GET "http://localhost:8080/api/economic-activities/recent?page=0&size=10" \
  -H "Authorization: Bearer <JWT_TOKEN>"

# Con tamaño personalizado
curl -X GET "http://localhost:8080/api/economic-activities/recent?page=0&size=20" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## Parámetros de Paginación

Todos los endpoints que soportan paginación aceptan los siguientes parámetros de query:

- `page`: Número de página (empezando en 0). **Por defecto: 0**
- `size`: Tamaño de la página (número de elementos por página). **Por defecto: 20**
- `sort`: Campo(s) por el cual ordenar. Formato: `campo,direccion` donde dirección puede ser `asc` o `desc`
  - Ejemplo simple: `sort=name,asc`
  - Múltiples ordenamientos: `sort=createdAt,desc&sort=name,asc`

### Ejemplos de ordenamiento:

```bash
# Ordenar por un solo campo
?sort=name,asc
?sort=code,desc
?sort=createdAt,desc

# Ordenar por múltiples campos
?sort=createdAt,desc&sort=name,asc
?sort=isActive,desc&sort=code,asc&sort=name,asc
```

---

## Ejemplo de Respuesta Paginada

```json
{
  "content": [
    {
      "id": 1,
      "name": "Agricultura",
      "code": "AGR1",
      "active": true,
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": null
    }
  ],
  "page": {
    "size": 10,
    "number": 0,
    "totalElements": 25,
    "totalPages": 3
  },
  "first": true,
  "last": false,
  "numberOfElements": 10
}
```

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer <JWT_TOKEN>`
2. **Rol requerido**: 
   - La mayoría de los endpoints requieren el rol `SUPER_ADMIN`
   - El endpoint `/search-unified` requiere rol `USER` o `ADMIN` (no requiere `SUPER_ADMIN`)
3. **Content-Type**: Para POST y PUT, siempre incluir `Content-Type: application/json`
4. **Fechas**: El formato de fecha para `by-date-range` debe ser ISO 8601: `YYYY-MM-DDTHH:mm:ss`
5. **Codificación de URL**: En Windows PowerShell, encierra las URLs entre comillas dobles para evitar problemas con caracteres especiales

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

