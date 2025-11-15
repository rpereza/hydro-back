package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.MonitoringStationRepository;
import com.univercloud.hydro.repository.MonitoringRepository;
import com.univercloud.hydro.service.MonitoringStationService;
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
 * Implementación del servicio para la gestión de Estaciones de Monitoreo.
 * Proporciona operaciones CRUD y lógica de negocio para estaciones de monitoreo.
 */
@Service
@Transactional
public class MonitoringStationServiceImpl implements MonitoringStationService {
    
    @Autowired
    private MonitoringStationRepository monitoringStationRepository;
    
    @Autowired
    private MonitoringRepository monitoringRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public MonitoringStation createMonitoringStation(MonitoringStation monitoringStation) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Verificar que el nombre no exista en la corporación
        if (monitoringStation.getName() != null && monitoringStationRepository.existsByCorporationAndName(corporation, monitoringStation.getName())) {
            throw new DuplicateResourceException("MonitoringStation", "name", monitoringStation.getName());
        }
        
        monitoringStation.setCorporation(corporation);
        monitoringStation.setCreatedBy(currentUser);
        monitoringStation.setUpdatedBy(currentUser);
        monitoringStation.setCreatedAt(LocalDateTime.now());
        monitoringStation.setUpdatedAt(LocalDateTime.now());
        
        return monitoringStationRepository.save(monitoringStation);
    }
    
    @Override
    @Transactional
    public MonitoringStation updateMonitoringStation(MonitoringStation monitoringStation) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> existingOpt = monitoringStationRepository.findById(monitoringStation.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("MonitoringStation", "id", monitoringStation.getId());
        }
        
        MonitoringStation existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (existing.getCorporation() == null || !existing.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this monitoring station");
        }
        
        // Verificar que el nombre no exista en la corporación (si cambió)
        if (monitoringStation.getName() != null && !existing.getName().equals(monitoringStation.getName())) {
            if (monitoringStationRepository.existsByCorporationAndNameExcludingId(corporation, monitoringStation.getName(), existing.getId())) {
                throw new DuplicateResourceException("MonitoringStation", "name", monitoringStation.getName());
            }
        }
        
        existing.setName(monitoringStation.getName());
        existing.setBasinSection(monitoringStation.getBasinSection());
        existing.setDescription(monitoringStation.getDescription());
        existing.setLatitude(monitoringStation.getLatitude());
        existing.setLongitude(monitoringStation.getLongitude());
        existing.setActive(monitoringStation.isActive());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return monitoringStationRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<MonitoringStation> getMonitoringStationById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return monitoringStationRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<MonitoringStation> getMyCorporationMonitoringStations(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getAllMyCorporationMonitoringStations() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getActiveMyCorporationMonitoringStations() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getAllActiveMonitoringStations() {
        return monitoringStationRepository.findByIsActiveTrue();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getAllInactiveMonitoringStations() {
        return monitoringStationRepository.findByIsActiveFalse();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<MonitoringStation> getMonitoringStationByName(String name) {
        return monitoringStationRepository.findByName(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> searchMonitoringStationsByName(String name) {
        return monitoringStationRepository.findByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> searchActiveMonitoringStationsByName(String name) {
        return monitoringStationRepository.findActiveByNameContainingIgnoreCase(name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> searchMonitoringStationsByCorporationAndName(Long corporationId, String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        if (!corporation.getId().equals(corporationId)) {
            throw new IllegalArgumentException("You do not have permission to search stations from another corporation");
        }
        
        return monitoringStationRepository.findByCorporationAndNameContainingIgnoreCase(corporation, name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getMonitoringStationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return monitoringStationRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationMonitoringStations() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveMyCorporationMonitoringStations() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.countActiveByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional
    public boolean activateMonitoringStation(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(id);
        if (stationOpt.isEmpty()) {
            throw new ResourceNotFoundException("MonitoringStation", "id", id);
        }
        
        MonitoringStation station = stationOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (station.getCorporation() == null || !station.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to activate this monitoring station");
        }
        
        station.setActive(true);
        station.setUpdatedBy(currentUser);
        station.setUpdatedAt(LocalDateTime.now());
        
        monitoringStationRepository.save(station);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deactivateMonitoringStation(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(id);
        if (stationOpt.isEmpty()) {
            throw new ResourceNotFoundException("MonitoringStation", "id", id);
        }
        
        MonitoringStation station = stationOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (station.getCorporation() == null || !station.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to deactivate this monitoring station");
        }
        
        station.setActive(false);
        station.setUpdatedBy(currentUser);
        station.setUpdatedAt(LocalDateTime.now());
        
        monitoringStationRepository.save(station);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteMonitoringStation(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(id);
        if (stationOpt.isEmpty()) {
            throw new ResourceNotFoundException("MonitoringStation", "id", id);
        }
        
        MonitoringStation station = stationOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (station.getCorporation() == null || !station.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this monitoring station");
        }
        
        // Verificar si hay monitoreos asociados
        long monitoringCount = monitoringRepository.countByMonitoringStationId(id);
        if (monitoringCount > 0) {
            throw new ResourceInUseException("MonitoringStation", "id", id, "Monitoring", monitoringCount);
        }
        
        monitoringStationRepository.delete(station);
        return true;
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.existsByCorporationAndName(corporation, name);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getMonitoringStationsOrderByName() {
        return monitoringStationRepository.findAllOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getActiveMonitoringStationsOrderByName() {
        return monitoringStationRepository.findActiveOrderByName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getMyCorporationMonitoringStationsOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.findByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getActiveMyCorporationMonitoringStationsOrderByName() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringStationRepository.findActiveByCorporationOrderByName(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<MonitoringStation> getMonitoringStationsOrderByCreatedAtDesc() {
        return monitoringStationRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public MonitoringStationStats getMyCorporationMonitoringStationStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        MonitoringStationStats stats = new MonitoringStationStats();
        
        // Estadísticas básicas
        List<MonitoringStation> allStations = monitoringStationRepository.findByCorporation(corporation);
        long totalMonitoringStations = allStations.size();
        long activeMonitoringStations = monitoringStationRepository.findByCorporationAndIsActiveTrue(corporation).size();
        long inactiveMonitoringStations = totalMonitoringStations - activeMonitoringStations;
        
        // Estadísticas de monitoreos
        long totalMonitorings = monitoringRepository.countByCorporationId(corporation.getId());
        
        // Estadísticas por año y mes
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        
        List<Monitoring> allMonitorings = monitoringRepository.findByCorporation(corporation);
        long monitoringsThisYear = allMonitorings.stream()
            .filter(monitoring -> monitoring.getMonitoringDate().getYear() == currentYear)
            .count();
        
        long monitoringsThisMonth = allMonitorings.stream()
            .filter(monitoring -> monitoring.getMonitoringDate().getYear() == currentYear && 
                                 monitoring.getMonitoringDate().getMonthValue() == currentMonth)
            .count();
        
        // Promedio de monitoreos por estación
        double averageMonitoringsPerStation = totalMonitoringStations > 0 ? 
            (double) totalMonitorings / totalMonitoringStations : 0.0;
        
        // Estaciones con monitoreos recientes (últimos 30 días)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        long stationsWithRecentMonitorings = allStations.stream()
            .filter(station -> {
                Optional<Monitoring> latestMonitoring = monitoringRepository.findLatestByMonitoringStation(station);
                return latestMonitoring.isPresent() && 
                       latestMonitoring.get().getCreatedAt().isAfter(thirtyDaysAgo);
            })
            .count();
        
        stats.setTotalMonitoringStations(totalMonitoringStations);
        stats.setActiveMonitoringStations(activeMonitoringStations);
        stats.setInactiveMonitoringStations(inactiveMonitoringStations);
        stats.setTotalMonitorings(totalMonitorings);
        stats.setMonitoringsThisYear(monitoringsThisYear);
        stats.setMonitoringsThisMonth(monitoringsThisMonth);
        stats.setAverageMonitoringsPerStation(averageMonitoringsPerStation);
        stats.setStationsWithRecentMonitorings(stationsWithRecentMonitorings);
        
        return stats;
    }
}
