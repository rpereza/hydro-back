-- Migration: Remove UNIQUE constraints from discharge_users code and document_number
-- Version: v4
-- Description: Removes UNIQUE constraints from code and document_number fields in discharge_users table
--              to allow duplicate values across different corporations. Uniqueness is now enforced
--              at the application level per corporation.

-- =====================================================
-- REMOVE UNIQUE CONSTRAINTS FROM discharge_users TABLE
-- =====================================================

-- Drop UNIQUE constraint from code column
-- MySQL creates an index with the same name as the column when UNIQUE is specified in the column definition
-- We need to drop the index to remove the UNIQUE constraint
-- Note: If the index doesn't exist, this will fail, which is acceptable for Flyway migrations
ALTER TABLE discharge_users DROP INDEX code;

-- Drop UNIQUE constraint from document_number column
ALTER TABLE discharge_users DROP INDEX document_number;

-- Note: The non-unique indexes for these columns (idx_discharge_users_code and idx_discharge_users_document_number)
-- will still exist for performance (created in v2_unified_domain_setup.sql), but they are no longer UNIQUE,
-- allowing duplicate values across different corporations.
-- Uniqueness is now enforced at the application level per corporation.

