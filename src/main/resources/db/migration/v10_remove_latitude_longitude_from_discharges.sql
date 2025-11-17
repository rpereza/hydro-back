-- Migration: Remove latitude and longitude columns from discharges table
-- Version: v10
-- Description: Removes the latitude and longitude columns from the discharges table
--              as these attributes are no longer needed in the Discharge entity.
-- Changes:
--   - Removes: latitude column from discharges table
--   - Removes: longitude column from discharges table

-- =====================================================
-- 1. REMOVE longitude COLUMN
-- =====================================================

-- Remove longitude column from discharges table
ALTER TABLE discharges 
DROP COLUMN longitude;

-- =====================================================
-- 2. REMOVE latitude COLUMN
-- =====================================================

-- Remove latitude column from discharges table
ALTER TABLE discharges 
DROP COLUMN latitude;

-- =====================================================
-- 3. VERIFICATION
-- =====================================================

-- Show table structure
DESCRIBE discharges;

-- Verify that the columns have been removed
SELECT 
    COLUMN_NAME, 
    DATA_TYPE, 
    IS_NULLABLE
FROM 
    INFORMATION_SCHEMA.COLUMNS
WHERE 
    TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'discharges'
    AND COLUMN_NAME IN ('latitude', 'longitude');

-- Expected result: No rows should be returned (columns removed)

