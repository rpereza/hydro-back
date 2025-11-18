package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Department;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.DepartmentRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.service.DepartmentService;
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
 * Implementación del servicio para la gestión de Departamentos.
 * Proporciona operaciones CRUD y lógica de negocio para departamentos.
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Department createDepartment(Department department) {
        // Verificar que el nombre no exista
        if (existsByName(department.getName())) {
            throw new DuplicateResourceException("Department", "name", department.getName());
        }
        
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        
        return departmentRepository.save(department);
    }
    
    @Override
    @Transactional
    public Department updateDepartment(Department department) {
        Optional<Department> existingOpt = departmentRepository.findById(department.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Department", "id", department.getId());
        }
        
        Department existing = existingOpt.get();

        // Verificar que el nombre no exista (si cambió)
        if (!existing.getName().equals(department.getName()) && existsByName(department.getName())) {
            throw new DuplicateResourceException("Department", "name", department.getName());
        }
        
        existing.setName(department.getName());
        existing.setCode(department.getCode());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return departmentRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentById(Long id) {
        Optional<Department> departmentOpt = departmentRepository.findById(id);
        if (departmentOpt.isPresent()) {
            return departmentOpt;
        }
        return Optional.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Department> searchDepartmentsByName(String name) {
        return departmentRepository.findByNameContainingIgnoreCase(name);
    }
        
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return departmentRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public DepartmentStats getDepartmentStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        DepartmentStats stats = new DepartmentStats();
        
        // Estadísticas básicas
        List<Department> allDepartments = departmentRepository.findAll();
        long totalDepartments = allDepartments.size();
        
        // Estadísticas de municipios
        long totalMunicipalities = municipalityRepository.count();
        
        // Calcular estadísticas por departamento
        long departmentsWithMunicipalities = 0;
        long departmentsWithoutMunicipalities = 0;
        
        for (Department department : allDepartments) {
            long municipalityCount = municipalityRepository.countByDepartmentId(department.getId());
            if (municipalityCount > 0) {
                departmentsWithMunicipalities++;
            } else {
                departmentsWithoutMunicipalities++;
            }
        }
        
        // Promedio de municipios por departamento
        double averageMunicipalitiesPerDepartment = totalDepartments > 0 ? (double) totalMunicipalities / totalDepartments : 0.0;
        
        stats.setTotalDepartments(totalDepartments);
        stats.setTotalMunicipalities(totalMunicipalities);
        stats.setAverageMunicipalitiesPerDepartment(averageMunicipalitiesPerDepartment);
        stats.setDepartmentsWithMunicipalities(departmentsWithMunicipalities);
        stats.setDepartmentsWithoutMunicipalities(departmentsWithoutMunicipalities);
        
        return stats;
    }
}
