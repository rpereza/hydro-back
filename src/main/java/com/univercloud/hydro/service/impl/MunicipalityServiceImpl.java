package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.Department;
import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.repository.DepartmentRepository;
import com.univercloud.hydro.repository.CategoryRepository;
import com.univercloud.hydro.service.MunicipalityService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Municipios.
 * Proporciona operaciones CRUD y lógica de negocio para municipios.
 */
@Service
@Transactional
public class MunicipalityServiceImpl implements MunicipalityService {
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Municipality createMunicipality(Municipality municipality) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que el código no exista
        if (existsByCode(municipality.getCode())) {
            throw new IllegalArgumentException("Ya existe un municipio con el código: " + municipality.getCode());
        }
        
        // Verificar que el departamento pertenezca a la corporación
        if (municipality.getDepartment() != null) {
            Optional<Department> departmentOpt = departmentRepository.findById(municipality.getDepartment().getId());
            if (departmentOpt.isEmpty()) {
                throw new IllegalArgumentException("El departamento no pertenece a su corporación");
            }
        }
        
        // Verificar que la categoría pertenezca a la corporación
        if (municipality.getCategory() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(municipality.getCategory().getId());
            if (categoryOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría no pertenece a su corporación");
            }
        }
        
        municipality.setCreatedBy(currentUser);
        municipality.setUpdatedBy(currentUser);
        municipality.setCreatedAt(LocalDateTime.now());
        municipality.setUpdatedAt(LocalDateTime.now());
        
        return municipalityRepository.save(municipality);
    }
    
    @Override
    @Transactional
    public Municipality updateMunicipality(Municipality municipality) {
        User currentUser = authorizationUtils.getCurrentUser();
        Optional<Municipality> existingOpt = municipalityRepository.findById(municipality.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el municipio con ID: " + municipality.getId());
        }
        
        Municipality existing = existingOpt.get();
        
        // Verificar que el código no exista (si cambió)
        if (!existing.getCode().equals(municipality.getCode()) && existsByCode(municipality.getCode())) {
            throw new IllegalArgumentException("Ya existe un municipio con el código: " + municipality.getCode());
        }
        
        // Verificar que el departamento pertenezca a la corporación
        if (municipality.getDepartment() != null) {
            Optional<Department> departmentOpt = departmentRepository.findById(municipality.getDepartment().getId());
            if (departmentOpt.isEmpty()) {
                throw new IllegalArgumentException("El departamento no pertenece a su corporación");
            }
        }
        
        // Verificar que la categoría pertenezca a la corporación
        if (municipality.getCategory() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(municipality.getCategory().getId());
            if (categoryOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría no pertenece a su corporación");
            }
        }
        
        existing.setName(municipality.getName());
        existing.setCode(municipality.getCode());
        existing.setDepartment(municipality.getDepartment());
        existing.setCategory(municipality.getCategory());
        existing.setNbi(municipality.getNbi());
        existing.setActive(municipality.isActive());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return municipalityRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipality> getMunicipalityById(Long id) {
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(id);
        if (municipalityOpt.isPresent()) {
            return municipalityOpt;
        }
        return Optional.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Municipality> getAllMunicipalities(Pageable pageable) {
        return municipalityRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getAllMunicipalities() {
        return municipalityRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesByDepartment(Long departmentId) {
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el departamento con ID: " + departmentId);
        }
        
        return municipalityRepository.findByDepartment(departmentOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesByCategory(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("La categoría no pertenece a su corporación");
        }
        
        return municipalityRepository.findByCategory(categoryOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipality> getMunicipalityByCode(String code) {
        return municipalityRepository.findByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> searchMunicipalitiesByName(String name) {
        return municipalityRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> searchMunicipalitiesByDepartmentAndName(Long departmentId, String name) {
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el departamento con ID: " + departmentId);
        }
        
        return municipalityRepository.findByDepartmentAndNameContainingIgnoreCase(departmentOpt.get(), name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> searchMunicipalitiesByCode(String code) {
        return municipalityRepository.findByCodeContainingIgnoreCase(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return municipalityRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMunicipalitiesByDepartment(Long departmentId) {
        return municipalityRepository.countByDepartmentId(departmentId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMunicipalitiesByCategory(Long categoryId) {
        return municipalityRepository.countByCategoryId(categoryId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesWithNbiGreaterThan(BigDecimal nbi) {
        return municipalityRepository.findByNbiGreaterThan(nbi);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesWithNbiLessThan(BigDecimal nbi) {
        return municipalityRepository.findByNbiLessThan(nbi);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesWithNbiBetween(BigDecimal minNbi, BigDecimal maxNbi) {
        return municipalityRepository.findByNbiBetween(minNbi, maxNbi);
    }
    
    @Override
    @Transactional
    public boolean deleteMunicipality(Long id) {
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(id);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el municipio con ID: " + id);
        }
        
        Municipality municipality = municipalityOpt.get();
        municipalityRepository.delete(municipality);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return municipalityRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesOrderByName() {
        return municipalityRepository.findAllOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesByDepartmentOrderByName(Long departmentId) {
        Optional<Department> departmentOpt = departmentRepository.findById(departmentId);
        if (departmentOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el departamento con ID: " + departmentId);
        }
        
        return municipalityRepository.findByDepartmentOrderByName(departmentOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesByCategoryOrderByName(Long categoryId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la categoría con ID: " + categoryId);
        }
        
        return municipalityRepository.findByCategoryOrderByName(categoryOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getMunicipalitiesOrderByCreatedAtDesc() {
        return municipalityRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public MunicipalityStats getMunicipalityStats() {
        MunicipalityStats stats = new MunicipalityStats();
        
        // Estadísticas básicas
        List<Municipality> allMunicipalities = municipalityRepository.findAll();
        long totalMunicipalities = allMunicipalities.size();
        
        // Estadísticas por departamento y categoría
        List<Department> allDepartments = departmentRepository.findAll();
        List<Category> allCategories = categoryRepository.findAll();
        
        long municipalitiesByDepartment = allDepartments.stream()
            .mapToLong(dept -> municipalityRepository.countByDepartmentId(dept.getId()))
            .sum();
        
        long municipalitiesByCategory = allCategories.stream()
            .mapToLong(cat -> municipalityRepository.countByCategoryId(cat.getId()))
            .sum();
        
        // Estadísticas de NBI
        List<Municipality> municipalitiesWithNbi = allMunicipalities.stream()
            .filter(m -> m.getNbi() != null)
            .toList();
        
        long municipalitiesWithNbiData = municipalitiesWithNbi.size();
        long municipalitiesWithoutNbiData = totalMunicipalities - municipalitiesWithNbiData;
        
        double averageNbi = municipalitiesWithNbi.stream()
            .mapToDouble(m -> m.getNbi().doubleValue())
            .average()
            .orElse(0.0);
        
        double minNbi = municipalitiesWithNbi.stream()
            .mapToDouble(m -> m.getNbi().doubleValue())
            .min()
            .orElse(0.0);
        
        double maxNbi = municipalitiesWithNbi.stream()
            .mapToDouble(m -> m.getNbi().doubleValue())
            .max()
            .orElse(0.0);
        
        stats.setTotalMunicipalities(totalMunicipalities);
        stats.setMunicipalitiesByDepartment(municipalitiesByDepartment);
        stats.setMunicipalitiesByCategory(municipalitiesByCategory);
        stats.setAverageNbi(averageNbi);
        stats.setMinNbi(minNbi);
        stats.setMaxNbi(maxNbi);
        stats.setMunicipalitiesWithNbiData(municipalitiesWithNbiData);
        stats.setMunicipalitiesWithoutNbiData(municipalitiesWithoutNbiData);
        
        return stats;
    }
        
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getAllActiveMunicipalities() {
        return municipalityRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getAllInactiveMunicipalities() {
        return municipalityRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipality> getMunicipalityByName(String name) {
        return municipalityRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> searchActiveMunicipalitiesByName(String name) {
        return municipalityRepository.findByIsActiveTrueAndNameContainingIgnoreCase(name);
    }
        
    @Override
    @Transactional
    public boolean activateMunicipality(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(id);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el municipio con ID: " + id);
        }
        
        Municipality municipality = municipalityOpt.get();

        
        municipality.setActive(true);
        municipality.setUpdatedBy(currentUser);
        municipality.setUpdatedAt(LocalDateTime.now());
        
        municipalityRepository.save(municipality);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deactivateMunicipality(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(id);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el municipio con ID: " + id);
        }
        
        Municipality municipality = municipalityOpt.get();
        municipality.setActive(false);
        municipality.setUpdatedBy(currentUser);
        municipality.setUpdatedAt(LocalDateTime.now());
        
        municipalityRepository.save(municipality);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return municipalityRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Municipality> getActiveMunicipalitiesOrderByName() {
        return municipalityRepository.findByIsActiveTrueOrderByName();
    }
}
