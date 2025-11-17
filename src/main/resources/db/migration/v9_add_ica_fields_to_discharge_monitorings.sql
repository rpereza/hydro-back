-- Migration: Add ICA (Índice de Calidad del Agua) fields to discharge_monitorings table
-- Version: v9
-- Description: Adds three optional fields to discharge_monitorings table for ICA calculation:
--              - number_ica_variables (INTEGER): Number of ICA variables used in calculation
--              - ica_coefficient (DECIMAL(3,2)): ICA coefficient value
--              - quality_clasification (VARCHAR): Quality classification enum (BUENA, ACEPTABLE, REGULAR, MALA, MUY_MALA)
-- Changes:
--   - Adds: number_ica_variables column to discharge_monitorings table
--   - Adds: ica_coefficient column to discharge_monitorings table
--   - Adds: quality_clasification column to discharge_monitorings table

-- =====================================================
-- 1. ADD number_ica_variables COLUMN
-- =====================================================

-- Add number_ica_variables column to discharge_monitorings table as nullable INTEGER
ALTER TABLE discharge_monitorings 
ADD COLUMN number_ica_variables INT NULL AFTER irnp;

-- =====================================================
-- 2. ADD ica_coefficient COLUMN
-- =====================================================

-- Add ica_coefficient column to discharge_monitorings table as nullable DECIMAL(3,2)
ALTER TABLE discharge_monitorings 
ADD COLUMN ica_coefficient DECIMAL(3,2) NULL AFTER number_ica_variables;

-- =====================================================
-- 3. ADD quality_clasification COLUMN
-- =====================================================

-- Add quality_clasification column to discharge_monitorings table as nullable VARCHAR
-- This will store enum values: BUENA, ACEPTABLE, REGULAR, MALA, MUY_MALA
ALTER TABLE discharge_monitorings 
ADD COLUMN quality_clasification VARCHAR(20) NULL AFTER ica_coefficient;

-- =====================================================
-- 4. VERIFICATION
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
    NUMERIC_SCALE
FROM 
    INFORMATION_SCHEMA.COLUMNS
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'discharge_monitorings'
    AND COLUMN_NAME IN ('number_ica_variables', 'ica_coefficient', 'quality_clasification');

-- Note: All three columns are nullable (optional) as they will be calculated 
-- from the service layer based on other monitoring data.

