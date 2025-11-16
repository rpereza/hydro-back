package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.MonitoringRepository;
import com.univercloud.hydro.repository.MonitoringStationRepository;
import com.univercloud.hydro.service.MonitoringService;
import com.univercloud.hydro.util.AuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio para la gestión de Monitoreos.
 * Proporciona operaciones CRUD y lógica de negocio para monitoreos de estaciones.
 */
@Service
@Transactional
public class MonitoringServiceImpl implements MonitoringService {
    
    @Autowired
    private MonitoringRepository monitoringRepository;
    
    @Autowired
    private MonitoringStationRepository monitoringStationRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public Monitoring createMonitoring(Monitoring monitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Verificar que no exista un monitoreo para la misma estación y fecha
        if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
            throw new DuplicateResourceException("Monitoring", "monitoringStation and monitoringDate", monitoring.getMonitoringStation().getId() + "/" + monitoring.getMonitoringDate());
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
            }
        }
        
        monitoring.setCorporation(corporation);
        monitoring.setCreatedBy(currentUser);
        monitoring.setCreatedAt(LocalDateTime.now());
        
        return monitoringRepository.save(monitoring);
    }
    
    @Override
    @Transactional
    public Monitoring updateMonitoring(Monitoring monitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Monitoring> existingOpt = monitoringRepository.findById(monitoring.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("Monitoring", "id", monitoring.getId());
        }
        
        Monitoring existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (existing.getCorporation() == null || !existing.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to update this monitoring");
        }
        
        // Verificar que no exista otro monitoreo para la misma estación y fecha (si cambió)
        if (!existing.getMonitoringStation().getId().equals(monitoring.getMonitoringStation().getId()) ||
            !existing.getMonitoringDate().equals(monitoring.getMonitoringDate())) {
            if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
                throw new DuplicateResourceException("Monitoring", "monitoringStation and monitoringDate", monitoring.getMonitoringStation().getId() + "/" + monitoring.getMonitoringDate());
            }
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
            }
        }
        
        existing.setMonitoringStation(monitoring.getMonitoringStation());
        existing.setMonitoringDate(monitoring.getMonitoringDate());
        existing.setWaterTemperature(monitoring.getWaterTemperature());
        existing.setAirTemperature(monitoring.getAirTemperature());
        existing.setPh(monitoring.getPh());
        existing.setOd(monitoring.getOd());
        existing.setSst(monitoring.getSst());
        existing.setDqo(monitoring.getDqo());
        existing.setCe(monitoring.getCe());
        existing.setIce(monitoring.getIce());
        existing.setIph(monitoring.getIph());
        existing.setIrnp(monitoring.getIrnp());
        existing.setIod(monitoring.getIod());
        existing.setIsst(monitoring.getIsst());
        existing.setIdqo(monitoring.getIdqo());
        existing.setPerformedBy(monitoring.getPerformedBy());
        existing.setNotes(monitoring.getNotes());
        existing.setWeatherConditions(monitoring.getWeatherConditions());
        existing.setCaudalVolumen(monitoring.getCaudalVolumen());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        
        return monitoringRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getMonitoringById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return monitoringRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Monitoring> getMyCorporationMonitorings(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return monitoringRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Monitoring> getMonitoringsByStation(Long monitoringStationId, Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStation(stationOpt.get(), pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStation(stationOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByDate(LocalDate monitoringDate) {
        return monitoringRepository.findByMonitoringDate(monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getMonitoringByStationAndDate(Long monitoringStationId, LocalDate monitoringDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStationAndMonitoringDate(stationOpt.get(), monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByDateRange(LocalDate startDate, LocalDate endDate) {
        return monitoringRepository.findByMonitoringDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStationAndDateRange(Long monitoringStationId, LocalDate startDate, LocalDate endDate) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findByMonitoringStationAndMonitoringDateBetween(stationOpt.get(), startDate, endDate);
    }
        
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getLatestMonitoringByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (stationOpt.isEmpty() || stationOpt.get().getCorporation() == null || !stationOpt.get().getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("Monitoring station does not belong to your corporation");
        }
        
        return monitoringRepository.findLatestByMonitoringStation(stationOpt.get());
    }
        
    @Override
    @Transactional
    public boolean deleteMonitoring(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<Monitoring> monitoringOpt = monitoringRepository.findById(id);
        if (monitoringOpt.isEmpty()) {
            throw new ResourceNotFoundException("Monitoring", "id", id);
        }
        
        Monitoring monitoring = monitoringOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (monitoring.getCorporation() == null || !monitoring.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalArgumentException("You do not have permission to delete this monitoring");
        }
        
        monitoringRepository.delete(monitoring);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByStationAndDate(Long monitoringStationId, LocalDate monitoringDate) {
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty()) {
            return false;
        }
        return monitoringRepository.existsByMonitoringStationAndMonitoringDate(stationOpt.get(), monitoringDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public MonitoringStats getMyCorporationMonitoringStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        MonitoringStats stats = new MonitoringStats();
        
       
        // Estadísticas de estaciones activas
        List<MonitoringStation> activeStations = monitoringStationRepository.findByCorporationAndIsActiveTrue(corporation);
        long activeStationsCount = activeStations.size();
        
        stats.setActiveStations(activeStationsCount);
        
        return stats;
    }
}
