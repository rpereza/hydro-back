-- Migration: Create consecutive_sequences table
-- Version: v13
-- Description: Table for managing consecutive numbers per year and corporation by sequence type (DISCHARGE, INVOICE, etc.)

CREATE TABLE consecutive_sequences (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    corporation_id BIGINT NOT NULL,
    year INT NOT NULL,
    sequence_type VARCHAR(50) NOT NULL,
    next_value INT NOT NULL,
    UNIQUE KEY uk_consecutive_sequences_corp_year_type (corporation_id, year, sequence_type),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
