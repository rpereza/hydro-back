-- Script de prueba para verificar la funcionalidad isActive
-- Ejecutar después de aplicar la migración v4_add_is_active_to_municipalities.sql

-- Verificar que la columna is_active existe en municipalities
SELECT column_name, data_type, is_nullable, column_default 
FROM information_schema.columns 
WHERE table_name = 'municipalities' AND column_name = 'is_active';

-- Verificar que la columna is_active existe en basin_sections
SELECT column_name, data_type, is_nullable, column_default 
FROM information_schema.columns 
WHERE table_name = 'basin_sections' AND column_name = 'is_active';

-- Verificar que el índice existe
SELECT indexname, tablename, indexdef 
FROM pg_indexes 
WHERE tablename = 'municipalities' AND indexname = 'idx_municipalities_is_active';

-- Insertar datos de prueba para municipalities
INSERT INTO municipalities (name, code, department_id, category_id, is_active, created_at, created_by_user_id)
VALUES 
    ('Municipio Activo 1', 'MA1', 1, 1, true, NOW(), 1),
    ('Municipio Inactivo 1', 'MI1', 1, 1, false, NOW(), 1),
    ('Municipio Activo 2', 'MA2', 1, 1, true, NOW(), 1);

-- Insertar datos de prueba para basin_sections (asumiendo que ya existe la estructura)
-- INSERT INTO basin_sections (name, water_basin_id, description, is_active, created_at, corporation_id, created_by_user_id)
-- VALUES 
--     ('Sección Activa 1', 1, 'Descripción sección activa', true, NOW(), 1, 1),
--     ('Sección Inactiva 1', 1, 'Descripción sección inactiva', false, NOW(), 1, 1),
--     ('Sección Activa 2', 1, 'Descripción sección activa 2', true, NOW(), 1, 1);

-- Probar consultas de municipios activos
SELECT 'Municipios activos:' as test_description;
SELECT id, name, code, is_active FROM municipalities WHERE is_active = true;

-- Probar consultas de municipios inactivos
SELECT 'Municipios inactivos:' as test_description;
SELECT id, name, code, is_active FROM municipalities WHERE is_active = false;

-- Probar consultas de secciones activas (descomentar si basin_sections tiene datos)
-- SELECT 'Secciones activas:' as test_description;
-- SELECT id, name, is_active FROM basin_sections WHERE is_active = true;

-- Probar consultas de secciones inactivas (descomentar si basin_sections tiene datos)
-- SELECT 'Secciones inactivas:' as test_description;
-- SELECT id, name, is_active FROM basin_sections WHERE is_active = false;

-- Probar actualización de estado
UPDATE municipalities SET is_active = false WHERE name = 'Municipio Activo 1';
SELECT 'Desactivando Municipio Activo 1:' as test_description;
SELECT id, name, code, is_active FROM municipalities WHERE name = 'Municipio Activo 1';

UPDATE municipalities SET is_active = true WHERE name = 'Municipio Inactivo 1';
SELECT 'Activando Municipio Inactivo 1:' as test_description;
SELECT id, name, code, is_active FROM municipalities WHERE name = 'Municipio Inactivo 1';

-- Limpiar datos de prueba
DELETE FROM municipalities WHERE name LIKE 'Municipio %';
-- DELETE FROM basin_sections WHERE name LIKE 'Sección %';

SELECT 'Pruebas completadas exitosamente!' as result;
