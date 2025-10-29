package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.MinimumTariff;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.repository.MinimumTariffRepository;
import com.univercloud.hydro.service.MinimumTariffService;
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
 * Implementación del servicio para la gestión de Tarifas Mínimas.
 */
@Service
@Transactional
public class MinimumTariffServiceImpl implements MinimumTariffService {
    
    @Autowired
    private MinimumTariffRepository minimumTariffRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public MinimumTariff createMinimumTariff(MinimumTariff minimumTariff) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Verificar que no existe una tarifa mínima para el mismo año en la corporación
        if (minimumTariff.getYear() != null && minimumTariffRepository.existsByCorporationAndYear(corporation, minimumTariff.getYear())) {
            throw new IllegalArgumentException("Minimum tariff for year " + minimumTariff.getYear() + " already exists in your corporation");
        }
        
        // Asignar corporación y usuario creador
        minimumTariff.setCorporation(corporation);
        minimumTariff.setCreatedBy(currentUser);
        minimumTariff.setCreatedAt(LocalDateTime.now());
        minimumTariff.setActive(true);
        
        return minimumTariffRepository.save(minimumTariff);
    }
    
    @Override
    public MinimumTariff updateMinimumTariff(MinimumTariff minimumTariff) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        MinimumTariff existingMinimumTariff = minimumTariffRepository.findById(minimumTariff.getId())
                .orElseThrow(() -> new IllegalArgumentException("Minimum tariff not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(existingMinimumTariff.getCorporation())) {
            throw new IllegalStateException("Access denied: Minimum tariff does not belong to your corporation");
        }
        
        // Verificar cambios en el año
        if (minimumTariff.getYear() != null && !minimumTariff.getYear().equals(existingMinimumTariff.getYear())) {
            if (minimumTariffRepository.existsByCorporationAndYear(corporation, minimumTariff.getYear())) {
                throw new IllegalArgumentException("Minimum tariff for year " + minimumTariff.getYear() + " already exists in your corporation");
            }
        }
        
        // Actualizar campos
        existingMinimumTariff.setYear(minimumTariff.getYear());
        existingMinimumTariff.setDboValue(minimumTariff.getDboValue());
        existingMinimumTariff.setSstValue(minimumTariff.getSstValue());
        existingMinimumTariff.setIpcValue(minimumTariff.getIpcValue());
        existingMinimumTariff.setActive(minimumTariff.isActive());
        existingMinimumTariff.setUpdatedBy(currentUser);
        existingMinimumTariff.setUpdatedAt(LocalDateTime.now());
        
        return minimumTariffRepository.save(existingMinimumTariff);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<MinimumTariff> getMinimumTariffById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        Optional<MinimumTariff> minimumTariff = minimumTariffRepository.findById(id);
        if (minimumTariff.isPresent() && !corporation.equals(minimumTariff.get().getCorporation())) {
            throw new IllegalStateException("Access denied: Minimum tariff does not belong to your corporation");
        }
        
        return minimumTariff;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MinimumTariff> getMyCorporationMinimumTariffs(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getAllMyCorporationMinimumTariffs() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getActiveMyCorporationMinimumTariffs() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getAllActiveMinimumTariffs() {
        return minimumTariffRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getAllInactiveMinimumTariffs() {
        return minimumTariffRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsByYear(Integer year) {
        return minimumTariffRepository.findByYear(year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getActiveMinimumTariffsByYear(Integer year) {
        return minimumTariffRepository.findByIsActiveTrueAndYear(year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMyCorporationMinimumTariffsByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporationAndYear(corporation, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getActiveMyCorporationMinimumTariffsByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporationAndIsActiveTrueAndYear(corporation, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return minimumTariffRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsByDboValueRange(BigDecimal minDboValue, BigDecimal maxDboValue) {
        return minimumTariffRepository.findByDboValueBetween(minDboValue, maxDboValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsBySstValueRange(BigDecimal minSstValue, BigDecimal maxSstValue) {
        return minimumTariffRepository.findBySstValueBetween(minSstValue, maxSstValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsByIpcValueRange(BigDecimal minIpcValue, BigDecimal maxIpcValue) {
        return minimumTariffRepository.findByIpcValueBetween(minIpcValue, maxIpcValue);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationMinimumTariffs() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.countByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveMyCorporationMinimumTariffs() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.countByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMinimumTariffsByYear(Integer year) {
        return minimumTariffRepository.countByYear(year);
    }
    
    @Override
    public boolean activateMinimumTariff(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        MinimumTariff minimumTariff = minimumTariffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Minimum tariff not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(minimumTariff.getCorporation())) {
            throw new IllegalStateException("Access denied: Minimum tariff does not belong to your corporation");
        }
        
        minimumTariff.setActive(true);
        minimumTariff.setUpdatedBy(currentUser);
        minimumTariff.setUpdatedAt(LocalDateTime.now());
        
        minimumTariffRepository.save(minimumTariff);
        return true;
    }
    
    @Override
    public boolean deactivateMinimumTariff(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        MinimumTariff minimumTariff = minimumTariffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Minimum tariff not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(minimumTariff.getCorporation())) {
            throw new IllegalStateException("Access denied: Minimum tariff does not belong to your corporation");
        }
        
        minimumTariff.setActive(false);
        minimumTariff.setUpdatedBy(currentUser);
        minimumTariff.setUpdatedAt(LocalDateTime.now());
        
        minimumTariffRepository.save(minimumTariff);
        return true;
    }
    
    @Override
    public boolean deleteMinimumTariff(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        MinimumTariff minimumTariff = minimumTariffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Minimum tariff not found"));
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null || !corporation.equals(minimumTariff.getCorporation())) {
            throw new IllegalStateException("Access denied: Minimum tariff does not belong to your corporation");
        }
        
        minimumTariffRepository.delete(minimumTariff);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.existsByCorporationAndYear(corporation, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsOrderByCreatedAtDesc() {
        return minimumTariffRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMinimumTariffsOrderByYearDesc() {
        return minimumTariffRepository.findAllOrderByYearDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getActiveMinimumTariffsOrderByYearDesc() {
        return minimumTariffRepository.findByIsActiveTrueOrderByYearDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getMyCorporationMinimumTariffsOrderByYearDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporationOrderByYearDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MinimumTariff> getActiveMyCorporationMinimumTariffsOrderByYearDesc() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return minimumTariffRepository.findByCorporationAndIsActiveTrueOrderByYearDesc(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MinimumTariffStats getMyCorporationMinimumTariffStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        MinimumTariffStats stats = new MinimumTariffStats();
        List<MinimumTariff> tariffs = minimumTariffRepository.findByCorporation(corporation);
        
        stats.setTotalMinimumTariffs(tariffs.size());
        
        long activeCount = tariffs.stream()
                .mapToLong(tariff -> tariff.isActive() ? 1 : 0)
                .sum();
        stats.setActiveMinimumTariffs(activeCount);
        stats.setInactiveMinimumTariffs(tariffs.size() - activeCount);
        
        // Contar años únicos
        long uniqueYears = tariffs.stream()
                .map(MinimumTariff::getYear)
                .distinct()
                .count();
        stats.setTotalYears(uniqueYears);
        
        // Calcular promedios y valores mínimos/máximos
        if (!tariffs.isEmpty()) {
            BigDecimal totalDboValue = tariffs.stream()
                    .map(MinimumTariff::getDboValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageDboValue(totalDboValue.divide(BigDecimal.valueOf(tariffs.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalSstValue = tariffs.stream()
                    .map(MinimumTariff::getSstValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageSstValue(totalSstValue.divide(BigDecimal.valueOf(tariffs.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            BigDecimal totalIpcValue = tariffs.stream()
                    .map(MinimumTariff::getIpcValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.setAverageIpcValue(totalIpcValue.divide(BigDecimal.valueOf(tariffs.size()), 2, BigDecimal.ROUND_HALF_UP));
            
            // Valores mínimos y máximos
            stats.setMinDboValue(tariffs.stream().map(MinimumTariff::getDboValue).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxDboValue(tariffs.stream().map(MinimumTariff::getDboValue).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            
            stats.setMinSstValue(tariffs.stream().map(MinimumTariff::getSstValue).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxSstValue(tariffs.stream().map(MinimumTariff::getSstValue).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            
            stats.setMinIpcValue(tariffs.stream().map(MinimumTariff::getIpcValue).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            stats.setMaxIpcValue(tariffs.stream().map(MinimumTariff::getIpcValue).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
        }
        
        return stats;
    }
}
