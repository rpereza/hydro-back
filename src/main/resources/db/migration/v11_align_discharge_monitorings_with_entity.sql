-- Migration: Align discharge_monitorings table with DischargeMonitoring entity
-- Version: v11
-- Description: Updates discharge_monitorings table to match the current DischargeMonitoring entity structure
-- Changes:
--   - Adds: monitoring_station_id column (nullable, FK to monitoring_stations)
--   - Adds: latitude column (DECIMAL(10,8), nullable)
--   - Adds: longitude column (DECIMAL(11,8), nullable)
--   - Removes: monitoring_date column (not in entity)
--   - Removes: weather_conditions column (not in entity)
--   - Removes: water_temperature column (not in entity)
--   - Removes: air_temperature column (not in entity)
--   - Removes: notes column (not in entity)
--   - Removes: performed_by column (not in entity)
-- Note: ICA fields (number_ica_variables, ica_coefficient, quality_clasification) 
--       were already added in v9 migration

-- =====================================================
-- 1. REMOVE COLUMNS NOT IN ENTITY
-- =====================================================

-- Remove performed_by column
ALTER TABLE discharge_monitorings 
DROP COLUMN performed_by;

-- Remove notes column
ALTER TABLE discharge_monitorings 
DROP COLUMN notes;

-- Remove air_temperature column
ALTER TABLE discharge_monitorings 
DROP COLUMN air_temperature;

-- Remove water_temperature column
ALTER TABLE discharge_monitorings 
DROP COLUMN water_temperature;

-- Remove weather_conditions column
ALTER TABLE discharge_monitorings 
DROP COLUMN weather_conditions;

-- Remove monitoring_date column
ALTER TABLE discharge_monitorings 
DROP COLUMN monitoring_date;

-- =====================================================
-- 2. ADD monitoring_station_id COLUMN
-- =====================================================

-- Add monitoring_station_id column as nullable BIGINT after discharge_id
ALTER TABLE discharge_monitorings 
ADD COLUMN monitoring_station_id BIGINT NULL AFTER discharge_id;

-- Add foreign key constraint for monitoring_station_id
ALTER TABLE discharge_monitorings 
ADD CONSTRAINT fk_discharge_monitorings_monitoring_station 
FOREIGN KEY (monitoring_station_id) REFERENCES monitoring_stations(id);

-- =====================================================
-- 3. ADD latitude COLUMN
-- =====================================================

-- Add latitude column as nullable DECIMAL(10,8) after caudal_volumen
ALTER TABLE discharge_monitorings 
ADD COLUMN latitude DECIMAL(10,8) NULL AFTER caudal_volumen;

-- =====================================================
-- 4. ADD longitude COLUMN
-- =====================================================

-- Add longitude column as nullable DECIMAL(11,8) after latitude
ALTER TABLE discharge_monitorings 
ADD COLUMN longitude DECIMAL(11,8) NULL AFTER latitude;

-- =====================================================
-- 5. UPDATE INDEXES
-- =====================================================

-- Add index for monitoring_station_id
CREATE INDEX idx_discharge_monitorings_monitoring_station 
ON discharge_monitorings(monitoring_station_id);

-- =====================================================
-- 6. VERIFICATION
-- =====================================================

-- Show table structure
DESCRIBE discharge_monitorings;

-- Show the new columns
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    IS_NULLABLE, 
    COLUMN_DEFAULT,
    NUMERIC_PRECISION,
    NUMERIC_SCALE,
    COLUMN_KEY
FROM 
    INFORMATION_SCHEMA.COLUMNS
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'discharge_monitorings'
    AND COLUMN_NAME IN ('monitoring_station_id', 'latitude', 'longitude', 
                        'monitoring_date', 'weather_conditions', 'water_temperature', 
                        'air_temperature', 'notes', 'performed_by')
ORDER BY ORDINAL_POSITION;

-- Verify foreign key constraints
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'discharge_monitorings'
    AND CONSTRAINT_NAME = 'fk_discharge_monitorings_monitoring_station';

