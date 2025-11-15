-- Migration: Add basin_section_id column to monitoring_stations table
-- Version: v7
-- Description: Adds the missing basin_section_id foreign key column to monitoring_stations table
--              to match the MonitoringStation entity definition
-- Changes:
--   - Adds: basin_section_id column to monitoring_stations table
--   - Adds: Foreign key constraint to basin_sections table

-- =====================================================
-- 1. ADD basin_section_id COLUMN
-- =====================================================

-- Add basin_section_id column to monitoring_stations table as NOT NULL
-- WARNING: This will fail if there are existing records in monitoring_stations table
-- If you have existing data, you need to either:
--   1. Delete existing monitoring_stations records, OR
--   2. Update them to have a valid basin_section_id before running this migration
ALTER TABLE monitoring_stations 
ADD COLUMN basin_section_id BIGINT NOT NULL AFTER longitude;

-- =====================================================
-- 2. ADD FOREIGN KEY CONSTRAINT
-- =====================================================

-- Add foreign key constraint to basin_sections table
ALTER TABLE monitoring_stations 
ADD CONSTRAINT fk_monitoring_station_basin_section 
FOREIGN KEY (basin_section_id) REFERENCES basin_sections(id);

-- =====================================================
-- 3. CREATE INDEX FOR PERFORMANCE
-- =====================================================

-- Create index for basin_section_id to improve query performance
CREATE INDEX idx_monitoring_stations_basin_section ON monitoring_stations(basin_section_id);

-- =====================================================
-- 4. VERIFICATION
-- =====================================================

-- Show table structure
DESCRIBE monitoring_stations;

-- Show foreign keys
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
    AND TABLE_NAME = 'monitoring_stations'
    AND CONSTRAINT_NAME = 'fk_monitoring_station_basin_section';

-- Note: The column is created as NOT NULL from the start to match the entity definition.
-- If you have existing monitoring_stations records, you must either:
--   1. Delete them before running this migration, OR
--   2. Temporarily modify this migration to add the column as NULL, update existing records,
--      then make it NOT NULL in a separate step

