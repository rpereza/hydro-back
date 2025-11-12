package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Municipality;
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
        
        // Verificar que el departamento exista
        if (municipality.getDepartment() != null) {
            Optional<Department> departmentOpt = departmentRepository.findById(municipality.getDepartment().getId());
            if (departmentOpt.isEmpty()) {
                throw new IllegalArgumentException("El departamento no existe");
            }
        }
        
        // Verificar que la categoría exista
        if (municipality.getCategory() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(municipality.getCategory().getId());
            if (categoryOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría no existe");
            }
        }
        
        municipality.setCreatedBy(currentUser);
        municipality.setCreatedAt(LocalDateTime.now());
        
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
        
        // Verificar que el departamento exista
        if (municipality.getDepartment() != null) {
            Optional<Department> departmentOpt = departmentRepository.findById(municipality.getDepartment().getId());
            if (departmentOpt.isEmpty()) {
                throw new IllegalArgumentException("El departamento no existe");
            }
        }
        
        // Verificar que la categoría exista
        if (municipality.getCategory() != null) {
            Optional<Category> categoryOpt = categoryRepository.findById(municipality.getCategory().getId());
            if (categoryOpt.isEmpty()) {
                throw new IllegalArgumentException("La categoría no existe");
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
    public List<Municipality> searchMunicipalitiesByName(String name) {
        return municipalityRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Municipality> getMunicipalityByName(String name) {
        return municipalityRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return municipalityRepository.existsByCode(code);
    }
}
