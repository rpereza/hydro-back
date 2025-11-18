-- Migration: Align project_progress table with ProjectProgress entity
-- Version: v12
-- Description: Updates project_progress table to match the current ProjectProgress entity structure
-- Changes:
--   - Removes: name column (not in entity)
--   - Removes: description column (not in entity)
--   - Removes: progress_percentage column (not in entity)
--   - Removes: start_date column (not in entity)
--   - Removes: end_date column (not in entity)
--   - Removes: status column (not in entity)
--   - Removes: is_active column (not in entity)
--   - Adds: discharge_user_id column (FK to discharge_users, nullable=false)
--   - Adds: year column (INTEGER, nullable=false)
--   - Adds: cci_percentage column (DECIMAL(7,2), nullable=false)
--   - Adds: cev_percentage column (DECIMAL(7,2), nullable=false)
--   - Adds: cds_percentage column (DECIMAL(7,2), nullable=false)
--   - Adds: ccs_percentage column (DECIMAL(7,2), nullable=false)

-- =====================================================
-- 1. REMOVE INDEXES AND CONSTRAINTS THAT REFERENCE COLUMNS TO BE REMOVED
-- =====================================================

-- Remove unique constraint on name
ALTER TABLE project_progress 
DROP INDEX uk_project_progress_name;

-- Remove indexes that reference columns to be removed
ALTER TABLE project_progress 
DROP INDEX idx_project_progress_name;

ALTER TABLE project_progress 
DROP INDEX idx_project_progress_active;

ALTER TABLE project_progress 
DROP INDEX idx_project_progress_percentage;

ALTER TABLE project_progress 
DROP INDEX idx_project_progress_status;

-- =====================================================
-- 2. REMOVE COLUMNS NOT IN ENTITY
-- =====================================================

-- Remove is_active column
ALTER TABLE project_progress 
DROP COLUMN is_active;

-- Remove status column
ALTER TABLE project_progress 
DROP COLUMN status;

-- Remove end_date column
ALTER TABLE project_progress 
DROP COLUMN end_date;

-- Remove start_date column
ALTER TABLE project_progress 
DROP COLUMN start_date;

-- Remove progress_percentage column
ALTER TABLE project_progress 
DROP COLUMN progress_percentage;

-- Remove description column
ALTER TABLE project_progress 
DROP COLUMN description;

-- Remove name column
ALTER TABLE project_progress 
DROP COLUMN name;

-- =====================================================
-- 3. ADD discharge_user_id COLUMN
-- =====================================================

-- Add discharge_user_id column as NOT NULL BIGINT after id
ALTER TABLE project_progress 
ADD COLUMN discharge_user_id BIGINT NOT NULL AFTER id;

-- Add foreign key constraint for discharge_user_id
ALTER TABLE project_progress 
ADD CONSTRAINT fk_project_progress_discharge_user 
FOREIGN KEY (discharge_user_id) REFERENCES discharge_users(id);

-- =====================================================
-- 4. ADD year COLUMN
-- =====================================================

-- Add year column as NOT NULL INTEGER after discharge_user_id
ALTER TABLE project_progress 
ADD COLUMN year INT NOT NULL AFTER discharge_user_id;

-- =====================================================
-- 5. ADD cci_percentage COLUMN
-- =====================================================

-- Add cci_percentage column as NOT NULL DECIMAL(7,2) after year
ALTER TABLE project_progress 
ADD COLUMN cci_percentage DECIMAL(7,2) NOT NULL AFTER year;

-- =====================================================
-- 6. ADD cev_percentage COLUMN
-- =====================================================

-- Add cev_percentage column as NOT NULL DECIMAL(7,2) after cci_percentage
ALTER TABLE project_progress 
ADD COLUMN cev_percentage DECIMAL(7,2) NOT NULL AFTER cci_percentage;

-- =====================================================
-- 7. ADD cds_percentage COLUMN
-- =====================================================

-- Add cds_percentage column as NOT NULL DECIMAL(7,2) after cev_percentage
ALTER TABLE project_progress 
ADD COLUMN cds_percentage DECIMAL(7,2) NOT NULL AFTER cev_percentage;

-- =====================================================
-- 8. ADD ccs_percentage COLUMN
-- =====================================================

-- Add ccs_percentage column as NOT NULL DECIMAL(7,2) after cds_percentage
ALTER TABLE project_progress 
ADD COLUMN ccs_percentage DECIMAL(7,2) NOT NULL AFTER cds_percentage;

-- =====================================================
-- 9. ADD INDEXES FOR NEW COLUMNS
-- =====================================================

-- Add index for discharge_user_id
CREATE INDEX idx_project_progress_discharge_user 
ON project_progress(discharge_user_id);

-- Add index for year
CREATE INDEX idx_project_progress_year 
ON project_progress(year);

-- Add index for discharge_user_id and year combination (useful for unique constraint validation)
CREATE INDEX idx_project_progress_discharge_user_year 
ON project_progress(discharge_user_id, year);

-- =====================================================
-- 10. VERIFICATION
-- =====================================================

-- Show table structure
DESCRIBE project_progress;

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
    AND TABLE_NAME = 'project_progress'
    AND COLUMN_NAME IN ('discharge_user_id', 'year', 'cci_percentage', 'cev_percentage', 
                        'cds_percentage', 'ccs_percentage', 'name', 'description', 
                        'progress_percentage', 'start_date', 'end_date', 'status', 'is_active')
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
    AND TABLE_NAME = 'project_progress'
    AND CONSTRAINT_NAME = 'fk_project_progress_discharge_user';

