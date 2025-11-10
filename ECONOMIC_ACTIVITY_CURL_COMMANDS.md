# Comandos cURL para EconomicActivityController

Base URL: `http://localhost:8080/api/economic-activities`

**Nota:** Todos los endpoints requieren autenticación con rol `SUPER_ADMIN`. Reemplaza `<JWT_TOKEN>` con tu token JWT válido.

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

## 8. Obtener actividades económicas por rango de fechas (con paginación)

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

## 9. Eliminar una actividad económica

```bash
curl -X DELETE http://localhost:8080/api/economic-activities/1 \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## 10. Verificar si existe una actividad económica por nombre

```bash
curl -X GET "http://localhost:8080/api/economic-activities/exists?name=Agricultura" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

**Respuesta:** `true` o `false`

---

## 11. Obtener actividades económicas más recientes (con paginación)

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
2. **Rol requerido**: Todos los endpoints requieren el rol `SUPER_ADMIN`
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

