package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.MonitoringStation;
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que no exista un monitoreo para la misma estación y fecha
        if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
            throw new IllegalArgumentException("Ya existe un monitoreo para esta estación en la fecha: " + monitoring.getMonitoringDate());
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
            }
        }
        
        monitoring.setCorporation(corporation);
        monitoring.setCreatedBy(currentUser);
        monitoring.setUpdatedBy(currentUser);
        monitoring.setCreatedAt(LocalDateTime.now());
        monitoring.setUpdatedAt(LocalDateTime.now());
        
        return monitoringRepository.save(monitoring);
    }
    
    @Override
    @Transactional
    public Monitoring updateMonitoring(Monitoring monitoring) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Monitoring> existingOpt = monitoringRepository.findById(monitoring.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el monitoreo con ID: " + monitoring.getId());
        }
        
        Monitoring existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        if (!existing.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar este monitoreo");
        }
        
        // Verificar que no exista otro monitoreo para la misma estación y fecha (si cambió)
        if (!existing.getMonitoringStation().getId().equals(monitoring.getMonitoringStation().getId()) ||
            !existing.getMonitoringDate().equals(monitoring.getMonitoringDate())) {
            if (existsByStationAndDate(monitoring.getMonitoringStation().getId(), monitoring.getMonitoringDate())) {
                throw new IllegalArgumentException("Ya existe un monitoreo para esta estación en la fecha: " + monitoring.getMonitoringDate());
            }
        }
        
        // Verificar que la estación de monitoreo pertenezca a la corporación
        if (monitoring.getMonitoringStation() != null) {
            Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoring.getMonitoringStation().getId());
            if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
                throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return monitoringRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getAllMyCorporationMonitorings() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return monitoringRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
        }
        
        return monitoringRepository.findByMonitoringStationAndMonitoringDateBetween(stationOpt.get(), startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByPerformer(String performedBy) {
        return monitoringRepository.findByPerformedBy(performedBy);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByCreatedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return monitoringRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMonitoringsByStation(Long monitoringStationId) {
        return monitoringRepository.countByMonitoringStationId(monitoringStationId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationMonitorings() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return monitoringRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Monitoring> getLatestMonitoringByStation(Long monitoringStationId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
        }
        
        return monitoringRepository.findLatestByMonitoringStation(stationOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsOrderByDateDesc() {
        return monitoringRepository.findAllOrderByMonitoringDateDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByYear(int year) {
        return monitoringRepository.findByYear(year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Monitoring> getMonitoringsByStationAndYear(Long monitoringStationId, int year) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<MonitoringStation> stationOpt = monitoringStationRepository.findById(monitoringStationId);
        if (stationOpt.isEmpty() || !stationOpt.get().getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("La estación de monitoreo no pertenece a su corporación");
        }
        
        return monitoringRepository.findByMonitoringStationAndYear(stationOpt.get(), year);
    }
    
    @Override
    @Transactional
    public boolean deleteMonitoring(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Monitoring> monitoringOpt = monitoringRepository.findById(id);
        if (monitoringOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el monitoreo con ID: " + id);
        }
        
        Monitoring monitoring = monitoringOpt.get();
        if (!monitoring.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar este monitoreo");
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
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        MonitoringStats stats = new MonitoringStats();
        
        // Estadísticas básicas
        List<Monitoring> allMonitorings = monitoringRepository.findByCorporation(corporation);
        long totalMonitorings = allMonitorings.size();
        
        // Estadísticas por año y mes
        int currentYear = LocalDateTime.now().getYear();
        int currentMonth = LocalDateTime.now().getMonthValue();
        
        long monitoringsThisYear = allMonitorings.stream()
            .filter(monitoring -> monitoring.getMonitoringDate().getYear() == currentYear)
            .count();
        
        long monitoringsThisMonth = allMonitorings.stream()
            .filter(monitoring -> monitoring.getMonitoringDate().getYear() == currentYear && 
                                 monitoring.getMonitoringDate().getMonthValue() == currentMonth)
            .count();
        
        // Estadísticas de estaciones activas
        List<MonitoringStation> activeStations = monitoringStationRepository.findByCorporationAndIsActiveTrue(corporation);
        long activeStationsCount = activeStations.size();
        
        // Calcular promedios de parámetros
        double averageWaterTemperature = allMonitorings.stream()
            .filter(m -> m.getWaterTemperature() != null)
            .mapToDouble(Monitoring::getWaterTemperature)
            .average()
            .orElse(0.0);
        
        double averageAirTemperature = allMonitorings.stream()
            .filter(m -> m.getAirTemperature() != null)
            .mapToDouble(Monitoring::getAirTemperature)
            .average()
            .orElse(0.0);
        
        double averagePh = allMonitorings.stream()
            .filter(m -> m.getPh() != null)
            .mapToDouble(m -> m.getPh().doubleValue())
            .average()
            .orElse(0.0);
        
        double averageOd = allMonitorings.stream()
            .filter(m -> m.getOd() != null)
            .mapToDouble(m -> m.getOd().doubleValue())
            .average()
            .orElse(0.0);
        
        double averageSst = allMonitorings.stream()
            .filter(m -> m.getSst() != null)
            .mapToDouble(m -> m.getSst().doubleValue())
            .average()
            .orElse(0.0);
        
        double averageDqo = allMonitorings.stream()
            .filter(m -> m.getDqo() != null)
            .mapToDouble(m -> m.getDqo().doubleValue())
            .average()
            .orElse(0.0);
        
        stats.setTotalMonitorings(totalMonitorings);
        stats.setMonitoringsThisYear(monitoringsThisYear);
        stats.setMonitoringsThisMonth(monitoringsThisMonth);
        stats.setActiveStations(activeStationsCount);
        stats.setAverageWaterTemperature(averageWaterTemperature);
        stats.setAverageAirTemperature(averageAirTemperature);
        stats.setAveragePh(averagePh);
        stats.setAverageOd(averageOd);
        stats.setAverageSst(averageSst);
        stats.setAverageDqo(averageDqo);
        
        return stats;
    }
}
