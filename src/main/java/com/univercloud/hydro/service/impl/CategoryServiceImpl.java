package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.CategoryRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.service.CategoryService;
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
 * Implementación del servicio para la gestión de Categorías.
 * Proporciona operaciones CRUD y lógica de negocio para categorías.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Category createCategory(Category category) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que el nombre no exista
        if (existsByName(category.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
        }

        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(category);
    }
    
    @Override
    @Transactional
    public Category updateCategory(Category category) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Category> existingOpt = categoryRepository.findById(category.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la categoría con ID: " + category.getId());
        }
        
        Category existing = existingOpt.get();
        
        // Verificar que el nombre no exista (si cambió)
        if (!existing.getName().equals(category.getName()) && existsByName(category.getName())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + category.getName());
        }
        
        existing.setName(category.getName());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            return categoryOpt;
        }
        return Optional.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return categoryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return categoryRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return categoryRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la categoría con ID: " + id);
        }
        
        Category category = categoryOpt.get();
        
        // Verificar si hay municipios asociados
        long municipalityCount = municipalityRepository.countByCategoryId(id);
        if (municipalityCount > 0) {
            throw new IllegalArgumentException("No se puede eliminar la categoría porque tiene " + municipalityCount + " municipios asociados");
        }
        
        categoryRepository.delete(category);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesOrderByName() {
        return categoryRepository.findAllOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Category> getCategoriesOrderByCreatedAtDesc() {
        return categoryRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public CategoryStats getCategoryStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        CategoryStats stats = new CategoryStats();
        
        // Estadísticas básicas
        List<Category> allCategories = categoryRepository.findAll();
        long totalCategories = allCategories.size();
        
        // Estadísticas de municipios
        long totalMunicipalities = municipalityRepository.count();
        
        // Calcular estadísticas por categoría
        long categoriesWithMunicipalities = 0;
        long categoriesWithoutMunicipalities = 0;
        
        for (Category category : allCategories) {
            long municipalityCount = municipalityRepository.countByCategoryId(category.getId());
            if (municipalityCount > 0) {
                categoriesWithMunicipalities++;
            } else {
                categoriesWithoutMunicipalities++;
            }
        }
        
        // Promedio de municipios por categoría
        double averageMunicipalitiesPerCategory = totalCategories > 0 ? (double) totalMunicipalities / totalCategories : 0.0;
        
        stats.setTotalCategories(totalCategories);
        stats.setTotalMunicipalities(totalMunicipalities);
        stats.setAverageMunicipalitiesPerCategory(averageMunicipalitiesPerCategory);
        stats.setCategoriesWithMunicipalities(categoriesWithMunicipalities);
        stats.setCategoriesWithoutMunicipalities(categoriesWithoutMunicipalities);
        
        return stats;
    }
}
