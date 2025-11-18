-- Migration: Fix minimum_tariffs table structure
-- Version: v5
-- Description: Aligns minimum_tariffs table structure with MinimumTariff entity
-- Changes:
--   - Removes: name, description, amount, effective_date columns and unique constraint
--   - Adds: year, dbo_value, sst_value, ipc_value columns
--
-- Note: This migration assumes the table was created with v2_unified_domain_setup.sql
-- If columns/constraints don't exist, the migration will fail gracefully
-- and can be adjusted based on the actual database state.

-- =====================================================
-- 1. DROP UNIQUE CONSTRAINT
-- =====================================================

-- Drop unique constraint on name (if it exists)
-- Note: For MySQL 8.0.19+, use: DROP INDEX IF EXISTS uk_minimum_tariff_name ON minimum_tariffs;
-- For older versions, this will fail if index doesn't exist - adjust accordingly
ALTER TABLE minimum_tariffs DROP INDEX uk_minimum_tariff_name;

-- =====================================================
-- 2. DROP COLUMNS THAT DON'T EXIST IN ENTITY
-- =====================================================

-- Drop name column
-- Note: For MySQL 8.0.19+, use: ALTER TABLE minimum_tariffs DROP COLUMN IF EXISTS name;
ALTER TABLE minimum_tariffs DROP COLUMN name;

-- Drop description column
ALTER TABLE minimum_tariffs DROP COLUMN description;

-- Drop amount column
ALTER TABLE minimum_tariffs DROP COLUMN amount;

-- Drop effective_date column
ALTER TABLE minimum_tariffs DROP COLUMN effective_date;

-- =====================================================
-- 3. ADD COLUMNS THAT EXIST IN ENTITY BUT NOT IN TABLE
-- =====================================================

-- Add year column
ALTER TABLE minimum_tariffs 
ADD COLUMN year INTEGER NOT NULL AFTER id;

-- Add dbo_value column
ALTER TABLE minimum_tariffs 
ADD COLUMN dbo_value DECIMAL(5,2) NOT NULL AFTER year;

-- Add sst_value column
ALTER TABLE minimum_tariffs 
ADD COLUMN sst_value DECIMAL(5,2) NOT NULL AFTER dbo_value;

-- Add ipc_value column
ALTER TABLE minimum_tariffs 
ADD COLUMN ipc_value DECIMAL(5,2) NOT NULL AFTER sst_value;

-- =====================================================
-- 4. CREATE INDEXES FOR NEW COLUMNS
-- =====================================================

-- Index for year column (useful for queries filtering by year)
CREATE INDEX idx_minimum_tariffs_year ON minimum_tariffs(year);

-- Index for corporation and year (useful for queries filtering by corporation and year)
CREATE INDEX idx_minimum_tariffs_corporation_year ON minimum_tariffs(corporation_id, year);
