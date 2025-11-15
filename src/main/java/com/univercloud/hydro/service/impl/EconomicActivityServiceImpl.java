package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.EconomicActivity;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.EconomicActivityRepository;
import com.univercloud.hydro.repository.DischargeUserRepository;
import com.univercloud.hydro.service.EconomicActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Actividades Económicas.
 */
@Service
@Transactional
public class EconomicActivityServiceImpl implements EconomicActivityService {
    
    @Autowired
    private EconomicActivityRepository economicActivityRepository;
    
    @Autowired
    private DischargeUserRepository dischargeUserRepository;
    
    @Override
    public EconomicActivity createEconomicActivity(EconomicActivity economicActivity) {
        // Verificar que no existe una actividad económica con el mismo nombre
        if (economicActivity.getName() != null && economicActivityRepository.existsByName(economicActivity.getName())) {
            throw new DuplicateResourceException("EconomicActivity", "name", economicActivity.getName());
        }
        
        // Verificar que no existe una actividad económica con el mismo código
        if (economicActivity.getCode() != null && economicActivityRepository.existsByCode(economicActivity.getCode())) {
            throw new DuplicateResourceException("EconomicActivity", "code", economicActivity.getCode());
        }
        
        // Asignar fecha de creación y estado activo
        economicActivity.setCreatedAt(LocalDateTime.now());
        economicActivity.setActive(true);
        
        return economicActivityRepository.save(economicActivity);
    }
    
    @Override
    public EconomicActivity updateEconomicActivity(EconomicActivity economicActivity) {
        EconomicActivity existingEconomicActivity = economicActivityRepository.findById(economicActivity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("EconomicActivity", "id", economicActivity.getId()));
        
        // Verificar cambios en el nombre
        if (economicActivity.getName() != null && !economicActivity.getName().equals(existingEconomicActivity.getName())) {
            if (economicActivityRepository.existsByName(economicActivity.getName())) {
                throw new DuplicateResourceException("EconomicActivity", "name", economicActivity.getName());
            }
        }
        
        // Verificar cambios en el código
        if (economicActivity.getCode() != null && !economicActivity.getCode().equals(existingEconomicActivity.getCode())) {
            if (economicActivityRepository.existsByCode(economicActivity.getCode())) {
                throw new DuplicateResourceException("EconomicActivity", "code", economicActivity.getCode());
            }
        }
        
        // Actualizar campos
        existingEconomicActivity.setName(economicActivity.getName());
        existingEconomicActivity.setCode(economicActivity.getCode());
        existingEconomicActivity.setActive(economicActivity.isActive());
        existingEconomicActivity.setUpdatedAt(LocalDateTime.now());
        
        return economicActivityRepository.save(existingEconomicActivity);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<EconomicActivity> getEconomicActivityById(Long id) {
        return economicActivityRepository.findById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> getAllEconomicActivities(Pageable pageable) {
        return economicActivityRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getAllEconomicActivities() {
        return economicActivityRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getAllActiveEconomicActivities() {
        return economicActivityRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getAllInactiveEconomicActivities() {
        return economicActivityRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<EconomicActivity> getEconomicActivityByName(String name) {
        return economicActivityRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<EconomicActivity> getEconomicActivityByCode(String code) {
        return economicActivityRepository.findByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> searchEconomicActivitiesByName(String name) {
        return economicActivityRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> searchEconomicActivitiesByName(String name, Pageable pageable) {
        return economicActivityRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> searchEconomicActivitiesByCode(String code) {
        return economicActivityRepository.findByCodeContainingIgnoreCase(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> searchEconomicActivitiesByCode(String code, Pageable pageable) {
        return economicActivityRepository.findByCodeStartingWithIgnoreCase(code, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> searchActiveEconomicActivitiesByName(String name) {
        return economicActivityRepository.findActiveByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> searchActiveEconomicActivitiesByCode(String code) {
        return economicActivityRepository.findByIsActiveTrueAndCodeContainingIgnoreCase(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getEconomicActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return economicActivityRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> getEconomicActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return economicActivityRepository.findByCreatedAtBetween(startDate, endDate, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countAllEconomicActivities() {
        return economicActivityRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveEconomicActivities() {
        return economicActivityRepository.countByIsActiveTrue();
    }
    
    @Override
    public boolean activateEconomicActivity(Long id) {
        EconomicActivity economicActivity = economicActivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EconomicActivity", "id", id));
        
        economicActivity.setActive(true);
        economicActivity.setUpdatedAt(LocalDateTime.now());
        
        economicActivityRepository.save(economicActivity);
        return true;
    }
    
    @Override
    public boolean deactivateEconomicActivity(Long id) {
        EconomicActivity economicActivity = economicActivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EconomicActivity", "id", id));
        
        economicActivity.setActive(false);
        economicActivity.setUpdatedAt(LocalDateTime.now());
        
        economicActivityRepository.save(economicActivity);
        return true;
    }
    
    @Override
    public boolean deleteEconomicActivity(Long id) {
        EconomicActivity economicActivity = economicActivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EconomicActivity", "id", id));
        
        // Verificar si hay usuarios de descarga asociados
        long dischargeUserCount = dischargeUserRepository.countByEconomicActivityId(id);
        if (dischargeUserCount > 0) {
            throw new ResourceInUseException("EconomicActivity", "id", id, "DischargeUser", dischargeUserCount);
        }
        
        economicActivityRepository.delete(economicActivity);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return economicActivityRepository.existsByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return economicActivityRepository.existsByCode(code);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getEconomicActivitiesOrderByCreatedAtDesc() {
        return economicActivityRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> getEconomicActivitiesOrderByCreatedAtDesc(Pageable pageable) {
        return economicActivityRepository.findAllOrderByCreatedAtDesc(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getActiveEconomicActivitiesOrderByName() {
        return economicActivityRepository.findActiveOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getEconomicActivitiesOrderByCode() {
        return economicActivityRepository.findAllOrderByCode();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<EconomicActivity> getActiveEconomicActivitiesOrderByCode() {
        return economicActivityRepository.findByIsActiveTrueOrderByCode();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<EconomicActivity> searchEconomicActivitiesByCodeOrName(String query, Pageable pageable) {
        return economicActivityRepository.findByCodeOrNameContainingIgnoreCase(query, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public EconomicActivityStats getEconomicActivityStats() {
        EconomicActivityStats stats = new EconomicActivityStats();
        
        List<EconomicActivity> activities = economicActivityRepository.findAll();
        stats.setTotalEconomicActivities(activities.size());
        
        long activeCount = activities.stream()
                .mapToLong(activity -> activity.isActive() ? 1 : 0)
                .sum();
        stats.setActiveEconomicActivities(activeCount);
        stats.setInactiveEconomicActivities(activities.size() - activeCount);
        
        // Contar usuarios asociados
        long totalUsers = activities.stream()
                .mapToLong(activity -> activity.getUsers().size())
                .sum();
        stats.setTotalUsers(totalUsers);
        
        // Contar actividades con código
        long activitiesWithCode = activities.stream()
                .mapToLong(activity -> activity.getCode() != null && !activity.getCode().trim().isEmpty() ? 1 : 0)
                .sum();
        stats.setActivitiesWithCode(activitiesWithCode);
        stats.setActivitiesWithoutCode(activities.size() - activitiesWithCode);
        
        return stats;
    }
}
