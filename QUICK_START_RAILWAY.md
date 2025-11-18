# Quick Start: Desplegar en Railway

## Pasos Rápidos

### 1. Preparar el Repositorio
```bash
# Asegúrate de que todos los cambios estén commiteados
git add .
git commit -m "Prepare for Railway deployment"
git push
```

### 2. Crear Proyecto en Railway

1. Ve a https://railway.app y crea una cuenta
2. Haz clic en "New Project" → "Deploy from GitHub repo"
3. Conecta tu repositorio y selecciona `hydro-back`

### 3. Agregar MySQL

1. En tu proyecto, haz clic en "New" → "Database" → "Add MySQL"
2. Railway creará automáticamente la base de datos

### 4. Configurar Variables de Entorno

En la configuración de tu servicio backend → "Variables", agrega:

**Obligatorias:**
```
SPRING_PROFILES_ACTIVE=prod
JWT_SECRET=<genera uno con: openssl rand -base64 64>
FRONTEND_URL=https://tu-frontend.railway.app
```

**Las variables de MySQL se configuran automáticamente** cuando agregas el servicio MySQL.

### 5. Desplegar

Railway detectará automáticamente el Dockerfile y comenzará el build. Monitorea los logs para ver el progreso.

### 6. Verificar

Una vez desplegado, Railway te dará una URL. Verifica:
```bash
curl https://tu-app.up.railway.app/actuator/health
```

## Generar JWT_SECRET

**Linux/Mac:**
```bash
openssl rand -base64 64
```

**Windows PowerShell:**
```powershell
.\scripts\generate-jwt-secret.ps1
```

**Windows CMD:**
```cmd
# Usa el script de PowerShell o genera manualmente
```

## Troubleshooting Rápido

- **Build falla**: Verifica que el Dockerfile esté en la raíz
- **No conecta a DB**: Verifica que las variables MYSQL_* estén configuradas
- **Error JWT**: Asegúrate de tener JWT_SECRET configurado (mínimo 64 caracteres)

## Documentación Completa

Ver `RAILWAY_DEPLOYMENT.md` para documentación detallada.

