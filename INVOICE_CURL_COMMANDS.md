# Invoice Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `InvoiceController`.

**Base URL:** `http://localhost:8080/api/invoices`

**Nota:** Todos los endpoints requieren autenticación JWT y rol `USER` o `ADMIN`. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Obtener Factura por ID

**Endpoint:** `GET /api/invoices/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene una factura por su ID. Solo retorna facturas que pertenezcan a la corporación del usuario autenticado.

```bash
curl -X GET "http://localhost:8080/api/invoices/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la factura (Long)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "discharge": { "id": 1 },
  "number": 1001,
  "year": 2024,
  "environmentalVariable": 2.50,
  "socioeconomicVariable": 1.20,
  "economicVariable": 0.75,
  "regionalFactor": 2.95,
  "ccDbo": 150.00,
  "ccSst": 120.00,
  "minimumTariffDbo": 250.50,
  "minimumTariffSst": 180.75,
  "amountToPayDbo": 110250,
  "amountToPaySst": 63870,
  "totalAmountToPay": 174120,
  "numberIcaVariables": 5,
  "icaCoefficient": 1.00,
  "rCoefficient": 1.00,
  "bCoefficient": 1.00,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `404 Not Found`: Factura no encontrada o no pertenece a la corporación
- `403 Forbidden`: Sin permisos

---

## 2. Listar Facturas Activas por Año

**Endpoint:** `GET /api/invoices`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene las facturas activas de la corporación del usuario autenticado para un año dado, con filtro opcional por `dischargeUserId` y soporte de paginación.

### 2.1 Solo por año (sin filtro de usuario)

```bash
curl -X GET "http://localhost:8080/api/invoices?year=2024&page=0&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 2.2 Filtrado por año y dischargeUserId

```bash
curl -X GET "http://localhost:8080/api/invoices?year=2024&dischargeUserId=42&page=0&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 2.3 Con ordenamiento

```bash
curl -X GET "http://localhost:8080/api/invoices?year=2024&page=0&size=10&sort=number,asc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de query:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `year` | Integer | Sí | Año de las facturas |
| `dischargeUserId` | Long | No | Filtra por el ID del usuario de descarga |
| `page` | Integer | No | Número de página (default: 0) |
| `size` | Integer | No | Tamaño de página (default: 20) |
| `sort` | String | No | Campo y dirección de ordenamiento (ej. `number,asc`) |

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "companyName": "Empresa Ejemplo S.A.",
      "year": 2024,
      "number": 1001,
      "dischargePoint": "Punto de vertimiento río Bogotá",
      "amountToPayDbo": 110250,
      "amountToPaySst": 63870,
      "totalAmountToPay": 174120
    },
    {
      "id": 2,
      "companyName": "Industrias XYZ Ltda.",
      "year": 2024,
      "number": 1002,
      "dischargePoint": "Descarga canal norte",
      "amountToPayDbo": 85000,
      "amountToPaySst": 47500,
      "totalAmountToPay": 132500
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 2,
  "totalPages": 1,
  "first": true,
  "last": true,
  "hasNext": false,
  "hasPrevious": false
}
```

**Errores posibles:**
- `403 Forbidden`: Usuario sin corporación

---

## 3. Generar Factura desde Descarga

**Endpoint:** `POST /api/invoices/generate-from-discharge/{dischargeId}`  
**Rol requerido:** `USER`  
**Descripción:** Genera una factura calculada automáticamente a partir de los datos de una descarga. Calcula todas las variables (ambiental, socioeconómica, económica, regional, coeficientes ICA/R/B, montos DBO/SST, etc.) usando la tarifa mínima y el progreso del proyecto (si aplica).

**Lógica de negocio:**
- Si existe una factura activa para el `dischargeId` y su `totalAmountToPay` es igual al valor calculado → retorna la factura existente (no consume consecutivo ni persiste).
- Si no existe factura activa o el monto difiere → marca la anterior como inactiva (si existe), consume consecutivo y persiste la nueva factura como activa.

```bash
curl -X POST "http://localhost:8080/api/invoices/generate-from-discharge/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `dischargeId`: ID de la descarga (Long)

**Respuesta exitosa (201 Created):** Factura generada o existente.
```json
{
  "id": 1,
  "discharge": { "id": 1 },
  "number": 1001,
  "year": 2024,
  "environmentalVariable": 2.50,
  "socioeconomicVariable": 1.20,
  "economicVariable": 0.75,
  "regionalFactor": 2.95,
  "ccDbo": 150.00,
  "ccSst": 120.00,
  "minimumTariffDbo": 250.50,
  "minimumTariffSst": 180.75,
  "amountToPayDbo": 110250,
  "amountToPaySst": 63870,
  "totalAmountToPay": 174120,
  "numberIcaVariables": 5,
  "icaCoefficient": 1.00,
  "rCoefficient": 1.00,
  "bCoefficient": 1.00,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": null
}
```

**Errores posibles:**
- `404 Not Found`: Descarga no encontrada o tarifa mínima no configurada para el año
- `400 Bad Request`: Datos insuficientes (ej. descarga sin monitoreos, sin clasificación de calidad o caudal)
- `500 Internal Server Error`: Error interno al generar la factura

---

## Resumen de Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/invoices/{id}` | Obtener factura por ID |
| GET | `/api/invoices?year={year}[&dischargeUserId={id}]` | Listar facturas activas por año (paginado) |
| POST | `/api/invoices/generate-from-discharge/{dischargeId}` | Generar factura calculada desde descarga |

---

## Ejemplo de Flujo Completo con Autenticación

```bash
# 1. Obtener token JWT
TOKEN_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "usuario@ejemplo.com", "password": "tu_password"}')
TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.token')

# 2. Generar factura desde descarga
curl -X POST "http://localhost:8080/api/invoices/generate-from-discharge/1" \
  -H "Authorization: Bearer $TOKEN"

# 3. Listar facturas activas del año 2024 (primera página)
curl -X GET "http://localhost:8080/api/invoices?year=2024&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 4. Listar facturas activas del año 2024 filtradas por dischargeUser
curl -X GET "http://localhost:8080/api/invoices?year=2024&dischargeUserId=42&page=0&size=20" \
  -H "Authorization: Bearer $TOKEN"

# 5. Obtener factura por ID
curl -X GET "http://localhost:8080/api/invoices/1" \
  -H "Authorization: Bearer $TOKEN"
```
