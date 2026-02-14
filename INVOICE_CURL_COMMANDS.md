# Invoice Controller - Ejemplos de cURL

Este documento contiene ejemplos de comandos cURL para todos los endpoints del `InvoiceController`.

**Base URL:** `http://localhost:8080/api/invoices`

**Nota:** Todos los endpoints requieren autenticación JWT y rol `USER` o `ADMIN`. Reemplaza `YOUR_JWT_TOKEN` con el token obtenido después de autenticarte.

---

## 1. Crear Factura

**Endpoint:** `POST /api/invoices`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Crea una nueva factura. El número y año se asignan automáticamente desde la secuencia de consecutivos de la corporación.

```bash
curl -X POST "http://localhost:8080/api/invoices" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "discharge": { "id": 1 },
    "year": 2024,
    "environmentalVariable": 2.50,
    "socioeconomicVariable": 1.20,
    "economicVariable": 0.75,
    "regionalFactor": 2.95,
    "ccDbo": 150.00,
    "ccSst": 120.00,
    "minimumTariffDbo": 250.50,
    "minimumTariffSst": 180.75,
    "amountToPayDbo": 110250.00,
    "amountToPaySst": 63870.00,
    "totalAmountToPay": 174120.00,
    "numberIcaVariables": 5,
    "icaCoefficient": 1.00,
    "rCoefficient": 1.00,
    "bCoefficient": 1.00,
    "isActive": true
  }'
```

**Campos del request:**
- `discharge`: Objeto con `id` de la descarga (requerido)
- `year`: Año de la factura (requerido, Integer)
- `environmentalVariable`, `socioeconomicVariable`, `economicVariable`, `regionalFactor`: Variables de cálculo (BigDecimal)
- `ccDbo`, `ccSst`: Cargas contaminantes (BigDecimal)
- `minimumTariffDbo`, `minimumTariffSst`: Tarifas mínimas (BigDecimal)
- `amountToPayDbo`, `amountToPaySst`, `totalAmountToPay`: Montos a pagar (BigDecimal)
- `numberIcaVariables`: Número de variables ICA (Integer)
- `icaCoefficient`, `rCoefficient`, `bCoefficient`: Coeficientes (BigDecimal)
- `isActive`: Estado activo/inactivo (opcional, Boolean, default: true)

**Nota:** El campo `number` se asigna automáticamente por el backend desde la secuencia de consecutivos.

**Respuesta exitosa (201 Created):**
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
  "amountToPayDbo": 110250.00,
  "amountToPaySst": 63870.00,
  "totalAmountToPay": 174120.00,
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
- `400 Bad Request`: Datos inválidos o validación fallida
- `403 Forbidden`: El usuario no tiene permisos o la descarga no pertenece a la corporación

---

## 2. Actualizar Factura

**Endpoint:** `PUT /api/invoices/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Actualiza una factura existente.

```bash
curl -X PUT "http://localhost:8080/api/invoices/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "discharge": { "id": 1 },
    "number": 1001,
    "year": 2024,
    "environmentalVariable": 2.55,
    "socioeconomicVariable": 1.20,
    "economicVariable": 0.75,
    "regionalFactor": 3.00,
    "ccDbo": 155.00,
    "ccSst": 125.00,
    "minimumTariffDbo": 250.50,
    "minimumTariffSst": 180.75,
    "amountToPayDbo": 116250.00,
    "amountToPaySst": 67500.00,
    "totalAmountToPay": 183750.00,
    "numberIcaVariables": 5,
    "icaCoefficient": 1.00,
    "rCoefficient": 1.00,
    "bCoefficient": 1.00,
    "isActive": true
  }'
```

**Parámetros de URL:**
- `id`: ID de la factura a actualizar (Long)

**Respuesta exitosa (200 OK):** Devuelve la factura actualizada.

**Errores posibles:**
- `404 Not Found`: Factura no encontrada
- `400 Bad Request`: Datos inválidos
- `403 Forbidden`: Sin permisos para actualizar

---

## 3. Obtener Factura por ID

**Endpoint:** `GET /api/invoices/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene una factura por su ID.

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
  "totalAmountToPay": 174120.00,
  "isActive": true,
  ...
}
```

**Errores posibles:**
- `404 Not Found`: Factura no encontrada o no pertenece a la corporación
- `403 Forbidden`: Sin permisos

---

## 4. Obtener Todas las Facturas de la Corporación

**Endpoint:** `GET /api/invoices`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Obtiene todas las facturas de la corporación del usuario autenticado.

```bash
curl -X GET "http://localhost:8080/api/invoices" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):** Lista de facturas.
```json
[
  {
    "id": 1,
    "discharge": { "id": 1 },
    "number": 1001,
    "year": 2024,
    "totalAmountToPay": 174120.00,
    "isActive": true,
    ...
  },
  ...
]
```

**Errores posibles:**
- `403 Forbidden`: Usuario sin corporación

---

## 5. Eliminar Factura

**Endpoint:** `DELETE /api/invoices/{id}`  
**Rol requerido:** `USER` o `ADMIN`  
**Descripción:** Elimina una factura.

```bash
curl -X DELETE "http://localhost:8080/api/invoices/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Parámetros de URL:**
- `id`: ID de la factura a eliminar (Long)

**Respuesta exitosa (200 OK):**
```json
true
```

**Errores posibles:**
- `404 Not Found`: Factura no encontrada
- `403 Forbidden`: Sin permisos para eliminar

---

## 6. Generar Factura desde Descarga

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
  "amountToPayDbo": 110250.00,
  "amountToPaySst": 63870.00,
  "totalAmountToPay": 174120.00,
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
- `404 Not Found`: Descarga no encontrada
- `400 Bad Request`: Datos insuficientes (ej. descarga sin monitoreos, sin clasificación de calidad o caudal)
- `500 Internal Server Error`: Error interno al generar la factura

---

## Resumen de Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/invoices` | Crear factura manual |
| PUT | `/api/invoices/{id}` | Actualizar factura |
| GET | `/api/invoices/{id}` | Obtener factura por ID |
| GET | `/api/invoices` | Listar todas las facturas de la corporación |
| DELETE | `/api/invoices/{id}` | Eliminar factura |
| POST | `/api/invoices/generate-from-discharge/{dischargeId}` | Generar factura calculada desde descarga |

---

## Ejemplo de Flujo Completo con Autenticación

```bash
# 1. Obtener token JWT
TOKEN_RESPONSE=$(curl -s -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username": "usuario@ejemplo.com", "password": "tu_password"}')
TOKEN=$(echo $TOKEN_RESPONSE | jq -r '.token')

# 2. Generar factura desde descarga (recomendado)
curl -X POST "http://localhost:8080/api/invoices/generate-from-discharge/1" \
  -H "Authorization: Bearer $TOKEN"

# 3. Obtener todas las facturas
curl -X GET "http://localhost:8080/api/invoices" \
  -H "Authorization: Bearer $TOKEN"

# 4. Obtener factura por ID
curl -X GET "http://localhost:8080/api/invoices/1" \
  -H "Authorization: Bearer $TOKEN"
```
