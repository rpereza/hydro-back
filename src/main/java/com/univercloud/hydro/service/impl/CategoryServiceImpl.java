package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Category;
import com.univercloud.hydro.repository.CategoryRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.service.CategoryService;
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
    
    @Override
    @Transactional
    public Category createCategory(Category category) {
        // Verify that the name does not exist
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
        existing.setValue(category.getValue());
        existing.setUpdatedAt(LocalDateTime.now());
        
        return categoryRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Category> getCategoryById(Long id) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            return categoryOpt;
        }
        return Optional.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
    
    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
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
    public CategoryStats getCategoryStats() {
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
