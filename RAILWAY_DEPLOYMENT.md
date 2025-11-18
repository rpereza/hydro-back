# Guía de Despliegue en Railway

Esta guía te ayudará a desplegar la aplicación Hydro Backend en Railway.

## Prerrequisitos

1. Cuenta en [Railway](https://railway.app)
2. Repositorio Git (GitHub, GitLab, o Bitbucket)
3. Aplicación compilada correctamente

## Nota Importante sobre Puerto Dinámico

La aplicación está configurada para usar el puerto dinámico que Railway asigna. El Dockerfile incluye:
- Script de entrada (`docker-entrypoint.sh`) que lee la variable de entorno `PORT` y configura Spring Boot
- Script de health check (`docker-healthcheck.sh`) que usa el puerto dinámico
- La aplicación se vinculará automáticamente al puerto asignado por Railway

## Pasos para Desplegar

### 1. Preparar el Repositorio

Asegúrate de que todos los archivos necesarios estén en tu repositorio:
- `Dockerfile`
- `.dockerignore`
- `pom.xml`
- `src/` (código fuente)
- `railway.json` (opcional, Railway puede detectar automáticamente)

### 2. Crear Proyecto en Railway

1. Ve a [Railway Dashboard](https://railway.app/dashboard)
2. Haz clic en "New Project"
3. Selecciona "Deploy from GitHub repo" (o tu proveedor Git)
4. Conecta tu repositorio y selecciona el proyecto `hydro-back`

### 3. Agregar Base de Datos MySQL

1. En tu proyecto de Railway, haz clic en "New"
2. Selecciona "Database" → "Add MySQL"
3. Railway creará automáticamente una base de datos MySQL
4. Anota las variables de entorno que se generan (las necesitarás después)

### 4. Configurar Variables de Entorno

En la configuración de tu servicio backend, ve a la pestaña "Variables" y agrega:

#### Variables de Base de Datos (se generan automáticamente al agregar MySQL)
- `MYSQL_HOST` - Host de la base de datos
- `MYSQL_PORT` - Puerto (generalmente 3306)
- `MYSQL_DATABASE` - Nombre de la base de datos
- `MYSQL_USER` - Usuario de la base de datos
- `MYSQL_PASSWORD` - Contraseña de la base de datos

#### Variables de Aplicación
- `PORT` - Puerto del servidor (Railway lo establece automáticamente, pero puedes usar 8080 como fallback)
- `SPRING_PROFILES_ACTIVE=prod` - Activa el perfil de producción
- `JWT_SECRET` - **IMPORTANTE**: Genera un secreto seguro para JWT (mínimo 64 caracteres)
  ```bash
  # Puedes generar uno con:
  openssl rand -base64 64
  ```
- `JWT_EXPIRATION` - Tiempo de expiración en milisegundos (opcional, default: 86400000 = 24 horas)
- `FRONTEND_URL` - **IMPORTANTE**: URL completa de tu aplicación Angular frontend (ej: `https://hydro-front-production.up.railway.app`)
  - **CRÍTICO para CORS**: Esta variable debe estar configurada con la URL exacta del frontend
  - Sin esta variable, las peticiones CORS fallarán con errores de "Access-Control-Allow-Origin"
  - No incluyas la barra final (`/`) en la URL

#### Ejemplo de Variables de Entorno:
```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=tu-secreto-super-seguro-generado-con-openssl-rand-base64-64-caracteres-minimo
JWT_EXPIRATION=86400000
FRONTEND_URL=https://tu-frontend.railway.app
```

### 5. Configurar el Build

Railway detectará automáticamente el Dockerfile. Si necesitas configurar manualmente:

1. Ve a "Settings" → "Build & Deploy"
2. Asegúrate de que "Dockerfile Path" esté configurado como `Dockerfile`
3. El "Root Directory" debe estar vacío (raíz del proyecto)

### 6. Desplegar

1. Railway comenzará automáticamente el build cuando detecte cambios en tu repositorio
2. Puedes hacer clic en "Deploy" para forzar un despliegue manual
3. Monitorea los logs en tiempo real para ver el progreso

### 7. Verificar el Despliegue

Una vez desplegado:

1. Railway te proporcionará una URL pública (ej: `https://tu-app.up.railway.app`)
2. Verifica el health check:
   ```bash
   curl https://tu-app.up.railway.app/actuator/health
   ```
3. Deberías recibir una respuesta JSON con el estado de salud

### 8. Configurar Dominio Personalizado (Opcional)

1. Ve a "Settings" → "Networking"
2. Haz clic en "Generate Domain" o agrega un dominio personalizado
3. Railway configurará automáticamente SSL/HTTPS

## Troubleshooting

### Error: "Cannot connect to database"
- Verifica que las variables de entorno de MySQL estén correctamente configuradas
- Asegúrate de que el servicio MySQL esté ejecutándose
- Verifica que `MYSQL_HOST` incluya el puerto si es necesario

### Error: "Port already in use"
- Railway maneja el puerto automáticamente, pero asegúrate de usar `${PORT}` en tu configuración

### Error: "JWT Secret not found"
- Asegúrate de haber configurado `JWT_SECRET` en las variables de entorno
- El secreto debe tener al menos 64 caracteres

### Error: "Flyway migration failed"
- Verifica que la base de datos esté vacía o que las migraciones sean compatibles
- Revisa los logs para ver el error específico de Flyway

### Error: "CORS policy: No 'Access-Control-Allow-Origin' header"
- **Solución**: Configura la variable de entorno `FRONTEND_URL` con la URL exacta de tu frontend
- Ejemplo: `FRONTEND_URL=https://hydro-front-production.up.railway.app`
- Asegúrate de que la URL no tenga barra final (`/`)
- Reinicia el servicio después de agregar/actualizar la variable
- Verifica que el valor coincida exactamente con la URL desde donde se hacen las peticiones

### Build falla
- Verifica que el Dockerfile esté en la raíz del proyecto
- Asegúrate de que `pom.xml` esté presente
- Revisa los logs de build para errores específicos

## Monitoreo

Railway proporciona:
- **Logs en tiempo real**: Ve a la pestaña "Deployments" → "View Logs"
- **Métricas**: CPU, memoria, red en tiempo real
- **Health checks**: Automáticos cada 30 segundos

## Actualizaciones

Para actualizar la aplicación:
1. Haz push de tus cambios al repositorio
2. Railway detectará automáticamente los cambios y desplegará una nueva versión
3. Puedes configurar "Auto Deploy" en Settings para desplegar automáticamente en cada push

## Optimización de Memoria (1GB RAM)

La aplicación está optimizada para funcionar eficientemente con el límite de 1GB de RAM en Railway. Las siguientes optimizaciones están implementadas:

### Optimizaciones JVM

- **Heap máximo**: 512MB (`-Xmx512m`)
- **Heap inicial**: 256MB (`-Xms256m`)
- **Metaspace máximo**: 128MB (metadatos de clases)
- **Code cache**: 64MB
- **Garbage Collector**: G1GC (optimizado para bajos recursos)
- **Compresión de punteros**: Habilitada para reducir uso de memoria
- **Deduplicación de strings**: Automática para ahorrar memoria

### Optimizaciones de Base de Datos

- **Pool de conexiones reducido**: Máximo 5 conexiones (antes 10)
- **Conexiones idle mínimas**: 2 (antes 5)
- **Batch processing**: Habilitado para operaciones en lote
- **Cache de segundo nivel**: Deshabilitado (ahorra memoria significativa)
- **Open-in-View**: Deshabilitado (mejora el rendimiento y reduce memoria)

### Optimizaciones de Spring Boot

- **JMX**: Deshabilitado
- **Background pre-initialization**: Deshabilitado
- **Actuator**: Solo endpoint de health (métricas deshabilitadas)
- **Logging**: Reducido a niveles esenciales (WARN/ERROR para frameworks)
- **File logging**: Deshabilitado en producción

### Monitoreo de Memoria

Para monitorear el uso de memoria en Railway:

1. Ve a la pestaña "Metrics" en tu servicio
2. Revisa el uso de memoria en tiempo real
3. Si ves picos cercanos a 1GB, considera:
   - Reducir aún más el pool de conexiones
   - Revisar consultas que carguen muchos datos
   - Implementar paginación en endpoints que devuelvan listas grandes

### Ajustes Adicionales (si es necesario)

Si después del despliegue observas problemas de memoria, puedes ajustar en `docker-entrypoint.sh`:

- Reducir `-Xmx512m` a `-Xmx384m` (dejar más espacio para no-heap)
- Reducir `spring.datasource.hikari.maximum-pool-size` a 3
- Aumentar `-XX:MaxGCPauseMillis` a 300 si hay pausas frecuentes

## Costos

- **Plan Hobby**: $5/mes (incluye $5 de crédito)
- **Plan Pro**: $20/mes (más recursos y características)
- Los primeros $5 son gratuitos para probar

## Seguridad

- ✅ Nunca commitees credenciales en el código
- ✅ Usa variables de entorno para todos los secretos
- ✅ Railway proporciona SSL/HTTPS automáticamente
- ✅ Genera un `JWT_SECRET` fuerte y único
- ✅ Actualiza regularmente las dependencias

## Próximos Pasos

1. Despliega el frontend Angular en Railway también
2. Configura `FRONTEND_URL` con la URL del frontend
3. Configura un dominio personalizado
4. Configura alertas y monitoreo
5. Configura backups de la base de datos

## Recursos

- [Railway Documentation](https://docs.railway.app)
- [Railway Discord](https://discord.gg/railway)
- [Spring Boot Production Ready](https://spring.io/guides/gs/actuator-service/)

