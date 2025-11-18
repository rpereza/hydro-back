# Guía para Ejecutar Migraciones en MySQL de Railway

Esta guía explica cómo ejecutar los scripts de migración de Flyway en la base de datos MySQL de Railway.

## ⚡ Opción 1: Automático con Flyway (Recomendado)

**Flyway ejecutará automáticamente las migraciones cuando la aplicación inicie**, siempre que:
- Las variables de entorno de MySQL estén configuradas correctamente
- `spring.flyway.enabled=true` (ya está configurado en `application-prod.properties`)
- La aplicación tenga acceso a la base de datos

### Verificar que Flyway está funcionando

1. Despliega tu aplicación en Railway
2. Revisa los logs de la aplicación
3. Busca mensajes como:
   ```
   Flyway Community Edition X.X.X by Redgate
   Database: jdbc:mysql://...
   Successfully validated X migrations
   Current version of schema `tu_base_de_datos`: X
   Migrating schema `tu_base_de_datos` to version X - Nombre de migración
   Successfully applied X migration(s)
   ```

Si ves estos mensajes, las migraciones se ejecutaron correctamente.

---

## 🔧 Opción 2: Manual con Cliente MySQL

Si necesitas ejecutar las migraciones manualmente o verificar el estado de la base de datos:

### Paso 1: Obtener Credenciales de Railway

1. Ve a tu proyecto en Railway
2. Haz clic en el servicio MySQL
3. Ve a la pestaña "Variables" o "Connect"
4. Anota las siguientes variables:
   - `MYSQL_HOST`
   - `MYSQL_PORT` (generalmente 3306)
   - `MYSQL_DATABASE`
   - `MYSQL_USER`
   - `MYSQL_PASSWORD`

### Paso 2: Conectarse a la Base de Datos

#### Opción A: Usando MySQL Command Line Client

**Windows:**
```bash
# Si tienes MySQL instalado localmente
mysql -h <MYSQL_HOST> -P <MYSQL_PORT> -u <MYSQL_USER> -p<MYSQL_PASSWORD> <MYSQL_DATABASE>
```

**Linux/Mac:**
```bash
mysql -h <MYSQL_HOST> -P <MYSQL_PORT> -u <MYSQL_USER> -p <MYSQL_DATABASE>
# Te pedirá la contraseña
```

**Ejemplo:**
```bash
mysql -h containers-us-west-xxx.railway.app -P 3306 -u root -p railway
```

#### Opción B: Usando Cliente Gráfico (Recomendado para principiantes)

**MySQL Workbench:**
1. Descarga e instala [MySQL Workbench](https://dev.mysql.com/downloads/workbench/)
2. Crea una nueva conexión:
   - **Hostname**: `MYSQL_HOST` (sin el puerto)
   - **Port**: `MYSQL_PORT` (generalmente 3306)
   - **Username**: `MYSQL_USER`
   - **Password**: `MYSQL_PASSWORD`
   - **Default Schema**: `MYSQL_DATABASE`
3. Conecta y ejecuta los scripts

**DBeaver (Alternativa):**
1. Descarga [DBeaver](https://dbeaver.io/download/)
2. Crea una nueva conexión MySQL
3. Usa las credenciales de Railway

### Paso 3: Ejecutar Migraciones Manualmente

Una vez conectado, puedes ejecutar los scripts SQL uno por uno en orden:

```sql
-- 1. Primero ejecuta v1_init_security_setup.sql
SOURCE /ruta/a/v1_init_security_setup.sql;

-- 2. Luego v2_unified_domain_setup.sql
SOURCE /ruta/a/v2_unified_domain_setup.sql;

-- Y así sucesivamente...
```

**O usando el contenido del archivo directamente:**

1. Abre cada archivo SQL en orden (v1, v2, v3, etc.)
2. Copia y pega el contenido en el cliente MySQL
3. Ejecuta cada script

**Orden de ejecución:**
1. `v1_init_security_setup.sql`
2. `v2_unified_domain_setup.sql`
3. `v3_initial_data.sql`
4. `v4_remove_discharge_user_unique_constraints.sql`
5. `v5_fix_minimum_tariffs_table.sql`
6. `v6_update_unique_constraints_water_basins_basin_sections.sql`
7. `v7_add_basin_section_id_to_monitoring_stations.sql`
8. `v8_add_ica_fields_to_monitorings.sql`
9. `v9_add_ica_fields_to_discharge_monitorings.sql`
10. `v10_remove_latitude_longitude_from_discharges.sql`
11. `v11_align_discharge_monitorings_with_entity.sql`
12. `v12_align_project_progress_with_entity.sql`

---

## 🚀 Opción 3: Usando Railway CLI (Avanzado)

Si tienes Railway CLI instalado:

```bash
# Instalar Railway CLI (si no lo tienes)
npm i -g @railway/cli

# Login
railway login

# Conectarse a tu proyecto
railway link

# Conectarse a MySQL
railway connect mysql
```

Luego ejecuta los scripts SQL directamente.

---

## 📋 Verificar Estado de las Migraciones

### Verificar Tabla de Flyway

Flyway crea una tabla `flyway_schema_history` para rastrear las migraciones ejecutadas:

```sql
-- Conectado a la base de datos MySQL
USE tu_base_de_datos;

-- Ver historial de migraciones
SELECT * FROM flyway_schema_history ORDER BY installed_rank;

-- Ver última migración aplicada
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC LIMIT 1;
```

### Verificar Tablas Creadas

```sql
-- Listar todas las tablas
SHOW TABLES;

-- Ver estructura de una tabla específica
DESCRIBE nombre_de_tabla;
```

---

## ⚠️ Solución de Problemas

### Error: "Flyway migration failed"

**Causa común:** La base de datos ya tiene tablas o datos que entran en conflicto.

**Solución:**
1. Verifica si la base de datos está vacía:
   ```sql
   SHOW TABLES;
   ```
2. Si hay tablas, puedes:
   - **Opción A**: Eliminar todas las tablas y dejar que Flyway las cree desde cero
   - **Opción B**: Verificar que las migraciones sean compatibles con el estado actual

### Error: "Table already exists"

**Causa:** Las migraciones ya se ejecutaron parcialmente.

**Solución:**
1. Verifica el estado en `flyway_schema_history`
2. Si falta alguna migración, ejecútala manualmente
3. O marca las migraciones como ejecutadas en Flyway (avanzado)

### Error: "Cannot connect to database"

**Causa:** Variables de entorno incorrectas o firewall.

**Solución:**
1. Verifica que las variables `MYSQL_*` estén correctamente configuradas en Railway
2. Verifica que el servicio MySQL esté ejecutándose
3. Verifica que no haya restricciones de firewall

---

## 🔐 Seguridad

⚠️ **IMPORTANTE**: Nunca compartas tus credenciales de base de datos públicamente.

- Las credenciales están en las variables de entorno de Railway
- Solo accede a la base de datos desde conexiones seguras
- Usa conexiones SSL cuando sea posible (Railway las proporciona automáticamente)

---

## 📝 Resumen Rápido

**Para la mayoría de casos, simplemente:**

1. ✅ Configura las variables de entorno de MySQL en Railway
2. ✅ Despliega la aplicación
3. ✅ Flyway ejecutará las migraciones automáticamente
4. ✅ Revisa los logs para confirmar

**Solo ejecuta manualmente si:**
- Necesitas verificar el estado de la base de datos
- Hay un error en las migraciones automáticas
- Necesitas hacer cambios específicos en la base de datos

---

## 📚 Recursos Adicionales

- [Flyway Documentation](https://flywaydb.org/documentation/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Railway MySQL Documentation](https://docs.railway.app/databases/mysql)

