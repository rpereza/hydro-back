# Ejemplos de cURL para DischargeController API

Base URL: `http://localhost:8080/api/discharges`

**Nota:** Todos los endpoints requieren autenticación. Reemplaza `YOUR_JWT_TOKEN` con tu token de autenticación válido.

---

## 1. Crear Vertimiento (POST)

Crea un nuevo vertimiento con sus parámetros y monitoreos asociados.

```bash
curl -X POST "http://localhost:8080/api/discharges" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "basinSection": {
      "id": 1
    },
    "municipality": {
      "id": 1
    },
    "dischargeType": "ARD",
    "number": 1,
    "year": 2024,
    "name": "Vertimiento Principal - Planta de Tratamiento",
    "dischargePoint": "Punto de vertimiento #1",
    "waterResourceType": "RIVER",
    "isBasinRehuse": true,
    "ccDboVert": 150.50,
    "ccSstVert": 120.75,
    "ccDboCap": 200.00,
    "ccSstCap": 180.25,
    "ccDboTotal": 350.50,
    "ccSstTotal": 301.00,
    "dqo": 250.75,
    "isSourceMonitored": true,
    "dischargeParameters": [
      {
        "month": "ENERO",
        "origin": "VERTIMIENTO",
        "caudalVolumen": 1000.50,
        "frequency": 30,
        "duration": 24,
        "concDbo": 150.25,
        "concSst": 120.50
      },
      {
        "month": "FEBRERO",
        "origin": "CAPTACION",
        "caudalVolumen": 1200.75,
        "frequency": 25,
        "duration": 20,
        "concDbo": 160.50,
        "concSst": 130.25
      }
    ],
    "dischargeMonitorings": [
      {
        "monitoringStation": {
          "id": 1
        },
        "od": 5.5,
        "sst": 10.2,
        "dqo": 8.5,
        "ce": 250.0,
        "ph": 7.2,
        "n": 15.5,
        "p": 3.2,
        "caudalVolumen": 1000.50,
        "latitude": 4.6097102,
        "longitude": -74.0817494
      }
    ]
  }'
```

**Respuesta exitosa (201 Created):**
```json
{
  "id": 1,
  "dischargeUser": { "id": 1, "companyName": "..." },
  "basinSection": { "id": 1, "name": "..." },
  "municipality": { "id": 1, "name": "..." },
  "dischargeType": "ARD",
  "number": 1,
  "year": 2024,
  "name": "Vertimiento Principal - Planta de Tratamiento",
  ...
}
```

---

## 2. Actualizar Vertimiento (PUT)

Actualiza un vertimiento existente. Puedes incluir `dischargeParameters` y `dischargeMonitorings` para actualizar, crear o eliminar elementos.

```bash
curl -X PUT "http://localhost:8080/api/discharges/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "dischargeUser": {
      "id": 1
    },
    "basinSection": {
      "id": 1
    },
    "municipality": {
      "id": 1
    },
    "dischargeType": "ARD",
    "number": 1,
    "year": 2024,
    "name": "Vertimiento Principal - Planta de Tratamiento (Actualizada)",
    "dischargePoint": "Punto de vertimiento #1 - Actualizado",
    "waterResourceType": "RIVER",
    "isBasinRehuse": true,
    "ccDboVert": 155.50,
    "ccSstVert": 125.75,
    "ccDboCap": 205.00,
    "ccSstCap": 185.25,
    "ccDboTotal": 360.50,
    "ccSstTotal": 311.00,
    "dqo": 255.75,
    "isSourceMonitored": true,
    "dischargeParameters": [
      {
        "id": 1,
        "month": "ENERO",
        "origin": "VERTIMIENTO",
        "caudalVolumen": 1100.50,
        "frequency": 30,
        "duration": 24,
        "concDbo": 155.25,
        "concSst": 125.50,
        "ccDbo": 155.00,
        "ccSst": 125.00
      },
      {
        "month": "MARZO",
        "origin": "VERTIMIENTO",
        "caudalVolumen": 1300.00,
        "frequency": 28,
        "duration": 22,
        "concDbo": 170.00,
        "concSst": 140.00,
        "ccDbo": 170.00,
        "ccSst": 140.00
      }
    ],
    "dischargeMonitorings": [
      {
        "id": 1,
        "monitoringStation": {
          "id": 1
        },
        "od": 5.8,
        "sst": 10.5,
        "dqo": 8.8,
        "ce": 255.0,
        "ph": 7.3,
        "n": 16.0,
        "p": 3.3,
        "caudalVolumen": 1100.50,
        "latitude": 4.6097102,
        "longitude": -74.0817494
      }
    ]
  }'
```

**Nota:** 
- Si incluyes un `id` en `dischargeParameters` o `dischargeMonitorings`, se actualizará el elemento existente.
- Si no incluyes `id`, se creará un nuevo elemento.
- Los elementos que no estén en la lista serán eliminados.
- Los campos `numberIcaVariables`, `icaCoefficient` y `qualityClasification` en `dischargeMonitorings` son calculados automáticamente por el servicio y no deben enviarse en el JSON.

---

## 3. Obtener Vertimiento por ID (GET)

Obtiene un vertimiento específica por su ID.

```bash
curl -X GET "http://localhost:8080/api/discharges/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "dischargeUser": { "id": 1, "companyName": "..." },
  "basinSection": { "id": 1, "name": "..." },
  "municipality": { "id": 1, "name": "..." },
  "dischargeType": "ARD",
  "number": 1,
  "year": 2024,
  "name": "Vertimiento Principal - Planta de Tratamiento",
  ...
}
```

---

## 4. Obtener Todos los Vertimientos con Paginación (GET)

Obtiene todos los vertimientos de la corporación del usuario autenticado con paginación.

```bash
# Primera página, 10 elementos por página
curl -X GET "http://localhost:8080/api/discharges?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Segunda página, 20 elementos por página
curl -X GET "http://localhost:8080/api/discharges?page=1&size=20" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"

# Con ordenamiento
curl -X GET "http://localhost:8080/api/discharges?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Vertimiento Principal",
      ...
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    ...
  },
  "totalElements": 50,
  "totalPages": 5,
  ...
}
```

---

## 5. Obtener Vertimientos por Usuario que vierte (GET)

Obtiene todos los vertimientos asociadas a un usuario que vierte en específico.

```bash
curl -X GET "http://localhost:8080/api/discharges/by-discharge-user/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Vertimiento Principal",
    "dischargeUser": { "id": 1, "companyName": "..." },
    ...
  },
  {
    "id": 2,
    "name": "Vertimiento Secundaria",
    ...
  }
]
```

---

## 6. Obtener Vertimientos por Año (GET)

Obtiene todos los vertimientos de un año específico.

```bash
curl -X GET "http://localhost:8080/api/discharges/by-year/2024" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Vertimiento Principal",
    "year": 2024,
    ...
  }
]
```

---

## 7. Buscar Vertimiento por Número y Año (GET)

Busca un vertimiento específica por su número y año.

```bash
curl -X GET "http://localhost:8080/api/discharges/by-number-year?number=1&year=2024" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "number": 1,
  "year": 2024,
  "name": "Vertimiento Principal",
  ...
}
```

**Respuesta si no existe (404 Not Found):**
```
(empty body)
```

---

## 8. Buscar Vertimientos por Nombre (GET)

Busca vertimientos cuyo nombre contenga el texto especificado (búsqueda parcial, case-insensitive).

```bash
curl -X GET "http://localhost:8080/api/discharges/search?name=Principal" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "Vertimiento Principal - Planta de Tratamiento",
    ...
  },
  {
    "id": 3,
    "name": "Vertimiento Principal - Sector 2",
    ...
  }
]
```

---

## 9. Eliminar Vertimiento (DELETE)

Elimina un vertimiento. Solo se puede eliminar si no tiene parámetros, monitoreos o facturas asociadas.

```bash
curl -X DELETE "http://localhost:8080/api/discharges/1" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Respuesta exitosa (200 OK):**
```json
true
```

**Respuesta si tiene dependencias (400 Bad Request):**
```
(empty body)
```

**Respuesta si no existe (404 Not Found):**
```
(empty body)
```

---

## Valores de Enums

### DischargeType
- `ARD` - Aguas Residuales Domésticas
- `ARND` - Aguas Residuales No Domésticas

### WaterResourceType
- `RIVER` - Lótico
- `LAKE` - Léntico

### DischargeParameter.Month
- `ENERO`, `FEBRERO`, `MARZO`, `ABRIL`, `MAYO`, `JUNIO`
- `JULIO`, `AGOSTO`, `SEPTIEMBRE`, `OCTUBRE`, `NOVIEMBRE`, `DICIEMBRE`

### DischargeParameter.Origin
- `VERTIMIENTO` - Vertimiento
- `CAPTACION` - Captación

### QualityClasification (Enum compartido)
- `BUENA` - Buena
- `ACEPTABLE` - Aceptable
- `REGULAR` - Regular
- `MALA` - Mala
- `MUY_MALA` - Muy Mala

**Nota:** Este enum es compartido entre `Monitoring` y `DischargeMonitoring`. Es calculado automáticamente por el servicio basado en el `icaCoefficient` y no debe enviarse en el JSON de creación/actualización.

---

## Códigos de Respuesta HTTP

- **200 OK**: Operación exitosa
- **201 Created**: Recurso creado exitosamente
- **400 Bad Request**: Datos inválidos o error en la solicitud
- **403 Forbidden**: No tienes permiso para realizar esta operación
- **404 Not Found**: Recurso no encontrado

---

## Notas Importantes

1. **Autenticación**: Todos los endpoints requieren un token JWT válido en el header `Authorization: Bearer YOUR_JWT_TOKEN`.

2. **Relaciones**: Al crear o actualizar un vertimiento, puedes incluir `dischargeParameters` y `dischargeMonitorings` en el mismo JSON. El sistema procesará automáticamente:
   - Creación de nuevos elementos
   - Actualización de elementos existentes (si tienen `id`)
   - Eliminación de elementos que no estén en la lista

3. **Validaciones**:
   - No puede haber dos `DischargeParameter` con el mismo `month` y `origin` para el mismo vertimiento.
   - El `dischargeUser`, `basinSection` y `municipality` deben pertenecer a tu corporación.

4. **Campos Opcionales en DischargeParameter**:
   - `ccDbo`, `ccSst` son opcionales
   - Todos los demas campos son requeridos excepto `id` (solo para actualizaciones).

5. **Campos Opcionales en DischargeMonitoring**:
   - `monitoringStation` es opcional
   - `n`, `p`, `rnp`, `irnp`, `iod`, `isst`, `idqo`, `ice`, `iph` son opcionales
   - `latitude` y `longitude` son opcionales
   - `numberIcaVariables`, `icaCoefficient`, `qualityClasification` son calculados automáticamente por el servicio (no enviar en el JSON)
   - Todos los demás campos son requeridos.

6. **Campos Calculados Automáticamente en DischargeMonitoring**:
   - `iod`, `isst`, `idqo`, `ice`, `iph`, `irnp`: Se calculan automáticamente basados en `od`, `sst`, `dqo`, `ce`, `ph`, `n`, `p`
   - `numberIcaVariables`: Se calcula contando cuántos índices ICA (`iod`, `isst`, `idqo`, `ice`, `iph`, `irnp`) tienen valor
   - `icaCoefficient`: Se calcula usando una fórmula ponderada basada en `numberIcaVariables`
   - `qualityClasification`: Se calcula basado en el rango del `icaCoefficient`:
     - `BUENA`: icaCoefficient > 0.9 y <= 1
     - `ACEPTABLE`: icaCoefficient > 0.7 y <= 0.9
     - `REGULAR`: icaCoefficient > 0.5 y <= 0.7
     - `MALA`: icaCoefficient > 0.25 y <= 0.5
     - `MUY_MALA`: icaCoefficient >= 0 y <= 0.25

