-- Migration: Unified Domain Setup with Multi-tenancy Support
-- Version: v2_unified
-- Description: Complete domain entity setup with multi-tenancy, audit fields, and proper dependencies
-- This script unifies v2, v3, and v4 migrations with correct precedence

-- =====================================================
-- 1. CREATE BASE REFERENCE TABLES (No dependencies)
-- =====================================================

-- Create departments table
CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    code VARCHAR(2) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create categories table
CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    value DECIMAL(5,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. CREATE CORPORATIONS TABLE (Depends on users from v1)
-- =====================================================

-- Create corporations table
CREATE TABLE corporations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL UNIQUE,
    code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    owner_user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (owner_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. UPDATE USERS TABLE FOR CORPORATION SUPPORT
-- =====================================================

-- Update users table to add corporation support
ALTER TABLE users ADD COLUMN corporation_id BIGINT NULL;
ALTER TABLE users ADD COLUMN has_created_corporation BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE users ADD CONSTRAINT fk_users_corporation 
    FOREIGN KEY (corporation_id) REFERENCES corporations(id);

-- Create authorization_types table (depends on corporations)
CREATE TABLE authorization_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. CREATE MUNICIPALITIES TABLE (Depends on departments, categories)
-- =====================================================

-- Create municipalities table with is_active field (from v4)
CREATE TABLE municipalities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    code VARCHAR(3) NOT NULL,
    department_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    nbi DECIMAL(6,2),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. CREATE DOMAIN ENTITIES WITH AUDIT FIELDS
-- =====================================================

-- Create water_basins table
CREATE TABLE water_basins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_water_basin_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create basin_sections table
CREATE TABLE basin_sections (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    start_point VARCHAR(100),
    end_point VARCHAR(100),
    water_basin_id BIGINT NOT NULL,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (water_basin_id) REFERENCES water_basins(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_basin_section_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create economic_activities table
CREATE TABLE economic_activities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(250) NOT NULL,
    code VARCHAR(4) UNIQUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    UNIQUE KEY uk_economic_activity_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create discharge_users table
CREATE TABLE discharge_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(200) NOT NULL,
    code VARCHAR(8) NOT NULL UNIQUE,
    document_type ENUM('NIT', 'CC', 'CE') NOT NULL,
    document_number VARCHAR(50) NOT NULL UNIQUE,
    contact_person VARCHAR(150) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    email VARCHAR(255) NOT NULL,
    alternative_email VARCHAR(255),
    alternative_phone VARCHAR(10),
    address VARCHAR(200) NOT NULL,
    file_number VARCHAR(20) NOT NULL,
    has_ptar BOOLEAN NOT NULL DEFAULT FALSE,
    efficiency_percentage DECIMAL(7,2),
    municipality_id BIGINT NOT NULL,
    economic_activity_id BIGINT NOT NULL,
    authorization_type_id BIGINT NOT NULL,
    is_public_service_company BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    FOREIGN KEY (economic_activity_id) REFERENCES economic_activities(id),
    FOREIGN KEY (authorization_type_id) REFERENCES authorization_types(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create discharges table
CREATE TABLE discharges (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discharge_user_id BIGINT NOT NULL,
    basin_section_id BIGINT NOT NULL,
    municipality_id BIGINT NOT NULL,
    discharge_type ENUM('ARD', 'ARND') NOT NULL,
    number INTEGER NOT NULL,
    year INTEGER NOT NULL,
    name VARCHAR(200) NOT NULL,
    discharge_point VARCHAR(500),
    water_resource_type ENUM('RIVER', 'LAKE') NOT NULL,
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    is_basin_rehuse BOOLEAN NOT NULL DEFAULT FALSE,
    cc_dbo_vert DECIMAL(9,2) NOT NULL,
    cc_sst_vert DECIMAL(9,2) NOT NULL,
    cc_dbo_cap DECIMAL(9,2) NOT NULL,
    cc_sst_cap DECIMAL(9,2) NOT NULL,
    cc_dbo_total DECIMAL(9,2) NOT NULL,
    cc_sst_total DECIMAL(9,2) NOT NULL,
    dqo DECIMAL(9,2) NOT NULL,
    is_source_monitored BOOLEAN NOT NULL DEFAULT FALSE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (discharge_user_id) REFERENCES discharge_users(id),
    FOREIGN KEY (basin_section_id) REFERENCES basin_sections(id),
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_discharge_number_year (number, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create discharge_parameters table
CREATE TABLE discharge_parameters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discharge_id BIGINT NOT NULL,
    month ENUM('ENERO', 'FEBRERO', 'MARZO', 'ABRIL', 'MAYO', 'JUNIO', 
               'JULIO', 'AGOSTO', 'SEPTIEMBRE', 'OCTUBRE', 'NOVIEMBRE', 'DICIEMBRE') NOT NULL,
    origin ENUM('VERTIMIENTO', 'CAPTACION') NOT NULL,
    caudal_volumen DECIMAL(12,2) NOT NULL,
    frequency SMALLINT NOT NULL,
    duration SMALLINT NOT NULL,
    conc_dbo DECIMAL(9,2) NOT NULL,
    conc_sst DECIMAL(9,2) NOT NULL,
    cc_dbo DECIMAL(9,2) NOT NULL,
    cc_sst DECIMAL(9,2) NOT NULL,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (discharge_id) REFERENCES discharges(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_discharge_parameter_month_origin (discharge_id, month, origin)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create discharge_monitorings table
CREATE TABLE discharge_monitorings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discharge_id BIGINT NOT NULL,
    monitoring_date DATE NOT NULL,
    weather_conditions VARCHAR(200),
    water_temperature DOUBLE,
    air_temperature DOUBLE,
    notes TEXT,
    performed_by VARCHAR(200),
    od DECIMAL(11,3) NOT NULL,
    sst DECIMAL(11,3) NOT NULL,
    dqo DECIMAL(11,3) NOT NULL,
    ce DECIMAL(11,3) NOT NULL,
    ph DECIMAL(11,3) NOT NULL,
    n DECIMAL(11,3),
    p DECIMAL(11,3),
    rnp DECIMAL(11,3),
    iod DECIMAL(11,3) NOT NULL,
    isst DECIMAL(11,3) NOT NULL,
    idqo DECIMAL(11,3) NOT NULL,
    ice DECIMAL(11,3) NOT NULL,
    iph DECIMAL(11,3) NOT NULL,
    irnp DECIMAL(11,3),
    caudal_volumen DECIMAL(12,2) NOT NULL,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (discharge_id) REFERENCES discharges(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create monitoring_stations table
CREATE TABLE monitoring_stations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    location VARCHAR(500),
    latitude DECIMAL(10,8),
    longitude DECIMAL(11,8),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_monitoring_station_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create monitorings table
CREATE TABLE monitorings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    monitoring_station_id BIGINT NOT NULL,
    monitoring_date DATE NOT NULL,
    weather_conditions VARCHAR(200),
    water_temperature DOUBLE,
    air_temperature DOUBLE,
    notes TEXT,
    performed_by VARCHAR(200),
    od DECIMAL(11,3) NOT NULL,
    sst DECIMAL(11,3) NOT NULL,
    dqo DECIMAL(11,3) NOT NULL,
    ce DECIMAL(11,3) NOT NULL,
    ph DECIMAL(11,3) NOT NULL,
    n DECIMAL(11,3),
    p DECIMAL(11,3),
    rnp DECIMAL(11,3),
    iod DECIMAL(11,3) NOT NULL,
    isst DECIMAL(11,3) NOT NULL,
    idqo DECIMAL(11,3) NOT NULL,
    ice DECIMAL(11,3) NOT NULL,
    iph DECIMAL(11,3) NOT NULL,
    irnp DECIMAL(11,3),
    caudal_volumen DECIMAL(12,2) NOT NULL,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (monitoring_station_id) REFERENCES monitoring_stations(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_monitoring_station_date (monitoring_station_id, monitoring_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create invoices table
CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    discharge_id BIGINT NOT NULL,
    number INTEGER NOT NULL,
    year INTEGER NOT NULL,
    environmental_variable DECIMAL(5,2) NOT NULL,
    socioeconomic_variable DECIMAL(5,2) NOT NULL,
    Economic_variable DECIMAL(5,2) NOT NULL,
    regional_factor DECIMAL(5,2) NOT NULL,
    cc_dbo DECIMAL(9,2) NOT NULL,
    cc_sst DECIMAL(9,2) NOT NULL,
    minimum_tariff_dbo DECIMAL(8,2) NOT NULL,
    minimum_tariff_sst DECIMAL(8,2) NOT NULL,
    amount_to_pay_dbo DECIMAL(16,2) NOT NULL,
    amount_to_pay_sst DECIMAL(16,2) NOT NULL,
    total_amount_to_pay DECIMAL(16,2) NOT NULL,
    number_ica_variables INTEGER NOT NULL,
    ica_coefficient DECIMAL(5,2) NOT NULL,
    r_coefficient DECIMAL(5,2) NOT NULL,
    b_coefficient DECIMAL(5,2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (discharge_id) REFERENCES discharges(id),
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create minimum_tariffs table
CREATE TABLE minimum_tariffs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    amount DECIMAL(15,2) NOT NULL,
    effective_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_minimum_tariff_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create project_progress table
CREATE TABLE project_progress (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    progress_percentage DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    start_date DATE,
    end_date DATE,
    status VARCHAR(50),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    corporation_id BIGINT NOT NULL,
    created_by_user_id BIGINT NOT NULL,
    updated_by_user_id BIGINT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (corporation_id) REFERENCES corporations(id),
    FOREIGN KEY (created_by_user_id) REFERENCES users(id),
    FOREIGN KEY (updated_by_user_id) REFERENCES users(id),
    UNIQUE KEY uk_project_progress_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 6. CREATE INDEXES FOR PERFORMANCE
-- =====================================================

-- Indexes for corporations table
CREATE INDEX idx_corporations_owner ON corporations(owner_user_id);
CREATE INDEX idx_corporations_code ON corporations(code);

-- Indexes for users table
CREATE INDEX idx_users_corporation ON users(corporation_id);

-- Indexes for departments table
CREATE INDEX idx_departments_name ON departments(name);
CREATE INDEX idx_departments_code ON departments(code);
CREATE INDEX idx_departments_created_at ON departments(created_at);

-- Indexes for categories table
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_categories_created_at ON categories(created_at);

-- Indexes for authorization_types table
CREATE INDEX idx_authorization_types_name ON authorization_types(name);
CREATE INDEX idx_authorization_types_corporation ON authorization_types(corporation_id);
CREATE INDEX idx_authorization_types_active ON authorization_types(is_active);
CREATE INDEX idx_authorization_types_created_at ON authorization_types(created_at);

-- Indexes for municipalities table
CREATE INDEX idx_municipalities_name ON municipalities(name);
CREATE INDEX idx_municipalities_code ON municipalities(code);
CREATE INDEX idx_municipalities_department ON municipalities(department_id);
CREATE INDEX idx_municipalities_category ON municipalities(category_id);
CREATE INDEX idx_municipalities_nbi ON municipalities(nbi);
CREATE INDEX idx_municipalities_is_active ON municipalities(is_active);
CREATE INDEX idx_municipalities_created_by ON municipalities(created_by_user_id);

-- Indexes for water_basins table
CREATE INDEX idx_water_basins_name ON water_basins(name);
CREATE INDEX idx_water_basins_corporation ON water_basins(corporation_id);
CREATE INDEX idx_water_basins_active ON water_basins(is_active);
CREATE INDEX idx_water_basins_created_at ON water_basins(created_at);

-- Indexes for basin_sections table
CREATE INDEX idx_basin_sections_name ON basin_sections(name);
CREATE INDEX idx_basin_sections_water_basin ON basin_sections(water_basin_id);
CREATE INDEX idx_basin_sections_corporation ON basin_sections(corporation_id);
CREATE INDEX idx_basin_sections_active ON basin_sections(is_active);
CREATE INDEX idx_basin_sections_start_point ON basin_sections(start_point);
CREATE INDEX idx_basin_sections_end_point ON basin_sections(end_point);

-- Indexes for discharge_users table
CREATE INDEX idx_discharge_users_company_name ON discharge_users(company_name);
CREATE INDEX idx_discharge_users_code ON discharge_users(code);
CREATE INDEX idx_discharge_users_document ON discharge_users(document_type, document_number);
CREATE INDEX idx_discharge_users_document_number ON discharge_users(document_number);
CREATE INDEX idx_discharge_users_municipality ON discharge_users(municipality_id);
CREATE INDEX idx_discharge_users_economic_activity ON discharge_users(economic_activity_id);
CREATE INDEX idx_discharge_users_authorization_type ON discharge_users(authorization_type_id);
CREATE INDEX idx_discharge_users_corporation ON discharge_users(corporation_id);
CREATE INDEX idx_discharge_users_active ON discharge_users(is_active);
CREATE INDEX idx_discharge_users_created_by ON discharge_users(created_by_user_id);

-- Indexes for discharges table
CREATE INDEX idx_discharges_discharge_user ON discharges(discharge_user_id);
CREATE INDEX idx_discharges_basin_section ON discharges(basin_section_id);
CREATE INDEX idx_discharges_municipality ON discharges(municipality_id);
CREATE INDEX idx_discharges_corporation ON discharges(corporation_id);
CREATE INDEX idx_discharges_type ON discharges(discharge_type);
CREATE INDEX idx_discharges_year ON discharges(year);
CREATE INDEX idx_discharges_number_year ON discharges(number, year);
CREATE INDEX idx_discharges_name ON discharges(name);
CREATE INDEX idx_discharges_monitored ON discharges(is_source_monitored);
CREATE INDEX idx_discharges_created_by ON discharges(created_by_user_id);

-- Indexes for discharge_parameters table
CREATE INDEX idx_discharge_parameters_discharge ON discharge_parameters(discharge_id);
CREATE INDEX idx_discharge_parameters_corporation ON discharge_parameters(corporation_id);
CREATE INDEX idx_discharge_parameters_month ON discharge_parameters(month);
CREATE INDEX idx_discharge_parameters_origin ON discharge_parameters(origin);
CREATE INDEX idx_discharge_parameters_frequency ON discharge_parameters(frequency);
CREATE INDEX idx_discharge_parameters_duration ON discharge_parameters(duration);
CREATE INDEX idx_discharge_parameters_created_by ON discharge_parameters(created_by_user_id);

-- Indexes for discharge_monitorings table
CREATE INDEX idx_discharge_monitorings_discharge ON discharge_monitorings(discharge_id);
CREATE INDEX idx_discharge_monitorings_corporation ON discharge_monitorings(corporation_id);
CREATE INDEX idx_discharge_monitorings_date ON discharge_monitorings(monitoring_date);
CREATE INDEX idx_discharge_monitorings_created_by ON discharge_monitorings(created_by_user_id);

-- Indexes for monitoring_stations table
CREATE INDEX idx_monitoring_stations_name ON monitoring_stations(name);
CREATE INDEX idx_monitoring_stations_corporation ON monitoring_stations(corporation_id);
CREATE INDEX idx_monitoring_stations_active ON monitoring_stations(is_active);
CREATE INDEX idx_monitoring_stations_created_by ON monitoring_stations(created_by_user_id);

-- Indexes for monitorings table
CREATE INDEX idx_monitorings_station ON monitorings(monitoring_station_id);
CREATE INDEX idx_monitorings_corporation ON monitorings(corporation_id);
CREATE INDEX idx_monitorings_date ON monitorings(monitoring_date);
CREATE INDEX idx_monitorings_station_date ON monitorings(monitoring_station_id, monitoring_date);
CREATE INDEX idx_monitorings_created_by ON monitorings(created_by_user_id);

-- Indexes for invoices table
CREATE INDEX idx_invoices_discharge ON invoices(discharge_id);
CREATE INDEX idx_invoices_corporation ON invoices(corporation_id);
CREATE INDEX idx_invoices_number ON invoices(number);
CREATE INDEX idx_invoices_year ON invoices(year);
CREATE INDEX idx_invoices_number_year ON invoices(number, year);
CREATE INDEX idx_invoices_total_amount ON invoices(total_amount_to_pay);
CREATE INDEX idx_invoices_active ON invoices(is_active);
CREATE INDEX idx_invoices_created_by ON invoices(created_by_user_id);

-- Indexes for minimum_tariffs table
CREATE INDEX idx_minimum_tariffs_name ON minimum_tariffs(name);
CREATE INDEX idx_minimum_tariffs_corporation ON minimum_tariffs(corporation_id);
CREATE INDEX idx_minimum_tariffs_active ON minimum_tariffs(is_active);
CREATE INDEX idx_minimum_tariffs_amount ON minimum_tariffs(amount);
CREATE INDEX idx_minimum_tariffs_effective_date ON minimum_tariffs(effective_date);
CREATE INDEX idx_minimum_tariffs_created_by ON minimum_tariffs(created_by_user_id);

-- Indexes for project_progress table
CREATE INDEX idx_project_progress_name ON project_progress(name);
CREATE INDEX idx_project_progress_corporation ON project_progress(corporation_id);
CREATE INDEX idx_project_progress_active ON project_progress(is_active);
CREATE INDEX idx_project_progress_percentage ON project_progress(progress_percentage);
CREATE INDEX idx_project_progress_status ON project_progress(status);
CREATE INDEX idx_project_progress_created_by ON project_progress(created_by_user_id);

-- Indexes for economic_activities table
CREATE INDEX idx_economic_activities_name ON economic_activities(name);
CREATE INDEX idx_economic_activities_active ON economic_activities(is_active);
CREATE INDEX idx_economic_activities_code ON economic_activities(code);

-- =====================================================
-- 7. INSERT DEFAULT DATA
-- =====================================================

-- Insert default departments (Colombian departments)
INSERT IGNORE INTO departments (name, code) VALUES 
('Antioquia', 'AN'),
('Atlántico', 'AT'),
('Bogotá D.C.', 'DC'),
('Bolívar', 'BO'),
('Boyacá', 'BY'),
('Caldas', 'CA'),
('Caquetá', 'CQ'),
('Cauca', 'CU'),
('Cesar', 'CE'),
('Córdoba', 'CO'),
('Cundinamarca', 'CU'),
('Huila', 'HU'),
('La Guajira', 'LG'),
('Magdalena', 'MA'),
('Meta', 'ME'),
('Nariño', 'NA'),
('Norte de Santander', 'NS'),
('Quindío', 'QU'),
('Risaralda', 'RI'),
('Santander', 'SA'),
('Sucre', 'SU'),
('Tolima', 'TO'),
('Valle del Cauca', 'VC');

-- Insert default categories
INSERT IGNORE INTO categories (name, value) VALUES 
('Primera', 1.00),
('Segunda', 0.80),
('Tercera', 0.60),
('Cuarta', 0.40),
('Quinta', 0.20),
('Sexta', 0.10);

-- Note: Authorization types will be created per corporation, not as global data
-- This is because they now have corporation_id as a required field

-- =====================================================
-- 8. VERIFICATION
-- =====================================================

-- Show created tables
SHOW TABLES;

-- Show table counts
SELECT 'departments' as table_name, COUNT(*) as count FROM departments
UNION ALL
SELECT 'categories', COUNT(*) FROM categories
UNION ALL
SELECT 'authorization_types', COUNT(*) FROM authorization_types
UNION ALL
SELECT 'corporations', COUNT(*) FROM corporations
UNION ALL
SELECT 'municipalities', COUNT(*) FROM municipalities
UNION ALL
SELECT 'water_basins', COUNT(*) FROM water_basins
UNION ALL
SELECT 'basin_sections', COUNT(*) FROM basin_sections
UNION ALL
SELECT 'discharge_users', COUNT(*) FROM discharge_users
UNION ALL
SELECT 'discharges', COUNT(*) FROM discharges
UNION ALL
SELECT 'discharge_parameters', COUNT(*) FROM discharge_parameters
UNION ALL
SELECT 'discharge_monitorings', COUNT(*) FROM discharge_monitorings
UNION ALL
SELECT 'monitoring_stations', COUNT(*) FROM monitoring_stations
UNION ALL
SELECT 'monitorings', COUNT(*) FROM monitorings
UNION ALL
SELECT 'invoices', COUNT(*) FROM invoices
UNION ALL
SELECT 'minimum_tariffs', COUNT(*) FROM minimum_tariffs
UNION ALL
SELECT 'project_progress', COUNT(*) FROM project_progress
UNION ALL
SELECT 'economic_activities', COUNT(*) FROM economic_activities;
