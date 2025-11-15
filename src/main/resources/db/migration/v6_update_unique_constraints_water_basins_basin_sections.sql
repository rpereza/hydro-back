-- Migration: Update unique constraints for water_basins and basin_sections
-- Version: v6
-- Description: Removes single-column unique constraints on name and creates composite unique constraints
-- Changes:
--   - Removes: uk_water_basin_name from water_basins
--   - Removes: uk_basin_section_name from basin_sections
--   - Adds: uk_water_basin_corporation_name (corporation_id, name) to water_basins
--   - Adds: uk_basin_section_corporation_water_basin_name (corporation_id, water_basin_id, name) to basin_sections

-- =====================================================
-- 1. DROP EXISTING UNIQUE CONSTRAINTS
-- =====================================================

-- Drop unique constraint on name from water_basins
-- Note: For MySQL 8.0.19+, use: DROP INDEX IF EXISTS uk_water_basin_name ON water_basins;
-- For older versions, this will fail if index doesn't exist - adjust accordingly
ALTER TABLE water_basins DROP INDEX uk_water_basin_name;

-- Drop unique constraint on name from basin_sections
-- Note: For MySQL 8.0.19+, use: DROP INDEX IF EXISTS uk_basin_section_name ON basin_sections;
-- For older versions, this will fail if index doesn't exist - adjust accordingly
ALTER TABLE basin_sections DROP INDEX uk_basin_section_name;

-- =====================================================
-- 2. CREATE NEW COMPOSITE UNIQUE CONSTRAINTS
-- =====================================================

-- Create composite unique constraint for water_basins (corporation_id, name)
-- This ensures that names are unique within each corporation
CREATE UNIQUE INDEX uk_water_basin_corporation_name 
ON water_basins(corporation_id, name);

-- Create composite unique constraint for basin_sections (corporation_id, water_basin_id, name)
-- This ensures that names are unique within each corporation and water basin combination
CREATE UNIQUE INDEX uk_basin_section_corporation_water_basin_name 
ON basin_sections(corporation_id, water_basin_id, name);

-- =====================================================
-- 3. VERIFICATION
-- =====================================================

-- Show indexes for water_basins
SHOW INDEXES FROM water_basins WHERE Key_name LIKE 'uk_%';

-- Show indexes for basin_sections
SHOW INDEXES FROM basin_sections WHERE Key_name LIKE 'uk_%';

