# Checklist de Despliegue en Railway

## ✅ Archivos Creados/Modificados

### Archivos de Configuración
- [x] `Dockerfile` - Configuración de Docker para build multi-stage
- [x] `.dockerignore` - Archivos excluidos del build Docker
- [x] `railway.json` - Configuración específica de Railway (opcional)
- [x] `src/main/resources/application-prod.properties` - Configuración de producción
- [x] `.env.example` - Ejemplo de variables de entorno

### Documentación
- [x] `RAILWAY_DEPLOYMENT.md` - Guía completa de despliegue
- [x] `QUICK_START_RAILWAY.md` - Guía rápida de inicio
- [x] `DEPLOYMENT_CHECKLIST.md` - Este archivo

### Scripts de Utilidad
- [x] `scripts/generate-jwt-secret.sh` - Generar JWT_SECRET (Linux/Mac)
- [x] `scripts/generate-jwt-secret.ps1` - Generar JWT_SECRET (Windows)

### Dependencias Agregadas
- [x] `spring-boot-starter-actuator` - Para health checks y monitoreo

## 📋 Checklist Pre-Despliegue

### Antes de Desplegar

#### Repositorio
- [ ] Todos los archivos están commiteados
- [ ] El repositorio está sincronizado con GitHub/GitLab
- [ ] No hay credenciales hardcodeadas en el código

#### Configuración Local
- [ ] El proyecto compila correctamente: `mvn clean package`
- [ ] Las migraciones de Flyway funcionan localmente
- [ ] Los tests pasan (opcional pero recomendado)

#### Railway Setup
- [ ] Cuenta creada en Railway
- [ ] Repositorio conectado a Railway
- [ ] Servicio MySQL agregado al proyecto
- [ ] Variables de entorno configuradas:
  - [ ] `SPRING_PROFILES_ACTIVE=prod`
  - [ ] `JWT_SECRET` (generado y configurado)
  - [ ] `FRONTEND_URL` (URL de tu frontend Angular)
  - [ ] Variables MySQL (se configuran automáticamente)

### Durante el Despliegue

- [ ] Monitorear los logs de build en Railway
- [ ] Verificar que el build se complete sin errores
- [ ] Verificar que las migraciones de Flyway se ejecuten correctamente
- [ ] Verificar que la aplicación inicie correctamente

### Después del Despliegue

#### Verificación
- [ ] Health check funciona: `curl https://tu-app.up.railway.app/actuator/health`
- [ ] La aplicación responde en la URL proporcionada por Railway
- [ ] La conexión a la base de datos funciona
- [ ] Los endpoints de autenticación funcionan

#### Seguridad
- [ ] HTTPS está habilitado (automático en Railway)
- [ ] `JWT_SECRET` es único y seguro (mínimo 64 caracteres)
- [ ] No hay información sensible en los logs
- [ ] CORS está configurado correctamente con `FRONTEND_URL`

#### Monitoreo
- [ ] Revisar métricas de CPU y memoria en Railway
- [ ] Configurar alertas si es necesario
- [ ] Revisar logs para errores o advertencias

## 🔧 Comandos Útiles

### Generar JWT_SECRET

**Linux/Mac:**
```bash
openssl rand -base64 64
```

**Windows PowerShell:**
```powershell
.\scripts\generate-jwt-secret.ps1
```

### Verificar Build Local
```bash
mvn clean package -DskipTests
docker build -t hydro-backend .
docker run -p 8080:8080 hydro-backend
```

### Verificar Health Check Local
```bash
curl http://localhost:8080/actuator/health
```

## 🚨 Troubleshooting

### Problema: Build falla
**Solución:**
- Verifica que el Dockerfile esté en la raíz del proyecto
- Verifica que `pom.xml` esté presente
- Revisa los logs de build en Railway

### Problema: No conecta a la base de datos
**Solución:**
- Verifica que el servicio MySQL esté ejecutándose
- Verifica que las variables `MYSQL_*` estén configuradas
- Verifica la URL de conexión en los logs

### Problema: Error de JWT
**Solución:**
- Verifica que `JWT_SECRET` esté configurado
- Asegúrate de que tenga al menos 64 caracteres
- Verifica que no tenga caracteres especiales problemáticos

### Problema: Migraciones de Flyway fallan
**Solución:**
- Verifica que la base de datos esté vacía o compatible
- Revisa los logs de Flyway
- Verifica que todas las migraciones estén en `src/main/resources/db/migration/`

## 📚 Recursos

- [Railway Documentation](https://docs.railway.app)
- [Spring Boot Production Ready](https://spring.io/guides/gs/actuator-service/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)

## 🎯 Próximos Pasos

1. Desplegar el frontend Angular en Railway
2. Configurar dominio personalizado
3. Configurar backups de base de datos
4. Configurar monitoreo y alertas avanzadas
5. Configurar CI/CD para despliegues automáticos

