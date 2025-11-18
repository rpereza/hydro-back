package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.dto.MonitoringStationWithLastMonitoringDTO;
import com.univercloud.hydro.entity.Monitoring;
import com.univercloud.hydro.entity.MonitoringStation;
import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.BasinSectionRepository;
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
    private BasinSectionRepository basinSectionRepository;
    
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
        monitoringStation.setCreatedAt(LocalDateTime.now());
        
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
            throw new IllegalStateException("You do not have permission to update this monitoring station");
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
    public List<MonitoringStation> searchMonitoringStationsByName(String name) {
        return monitoringStationRepository.findByNameContainingIgnoreCase(name);
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
            throw new IllegalStateException("You do not have permission to delete this monitoring station");
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
    public List<MonitoringStationWithLastMonitoringDTO> findMonitoringStationWithLastMonitoring(String name, Long basinSectionId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Validar que la sección de cuenca exista y pertenezca a la corporación
        Optional<BasinSection> basinSectionOpt = basinSectionRepository.findByIdAndCorporationId(basinSectionId, corporation.getId());
        if (basinSectionOpt.isEmpty()) {
            throw new ResourceNotFoundException("BasinSection", "id", basinSectionId);
        }
        
        // Buscar las estaciones de monitoreo por nombre (búsqueda parcial) y sección de cuenca
        List<MonitoringStation> stations = monitoringStationRepository.findByNameAndBasinSectionId(name, basinSectionId);
        if (stations.isEmpty()) {
            throw new ResourceNotFoundException("MonitoringStation", "name and basinSection", name + "/" + basinSectionId);
        }
        
        // Filtrar estaciones que pertenezcan a la corporación del usuario y estén activas
        List<MonitoringStation> validStations = stations.stream()
            .filter(s -> s.getCorporation() != null && s.getCorporation().getId().equals(corporation.getId()))
            .filter(MonitoringStation::isActive)
            .toList();
        
        if (validStations.isEmpty()) {
            // Si hay estaciones pero ninguna cumple las condiciones, determinar el error específico
            boolean hasInactive = stations.stream().anyMatch(s -> 
                s.getCorporation() != null && s.getCorporation().getId().equals(corporation.getId()) && !s.isActive());
            if (hasInactive) {
                throw new IllegalArgumentException("No active monitoring stations found");
            }
            throw new IllegalStateException("No monitoring stations found that belong to your corporation");
        }
        
        // Procesar cada estación válida y construir los DTOs
        return validStations.stream()
            .filter(station -> {
                // Validar que la estación tenga al menos un monitoreo
                long monitoringCount = monitoringRepository.countByMonitoringStationId(station.getId());
                return monitoringCount > 0;
            })
            .map(station -> {
                // Obtener el último monitoreo (más reciente por fecha)
                Optional<Monitoring> latestMonitoringOpt = monitoringRepository.findLatestByMonitoringStation(station);
                if (latestMonitoringOpt.isEmpty()) {
                    return null; // Filtrar estaciones sin monitoreos válidos
                }
                
                Monitoring latestMonitoring = latestMonitoringOpt.get();
                
                // Construir el DTO del último monitoreo
                MonitoringStationWithLastMonitoringDTO.LastMonitoringDTO lastMonitoringDTO = 
                    new MonitoringStationWithLastMonitoringDTO.LastMonitoringDTO(
                        latestMonitoring.getId(),
                        latestMonitoring.getOd(),
                        latestMonitoring.getSst(),
                        latestMonitoring.getDqo(),
                        latestMonitoring.getCe(),
                        latestMonitoring.getPh(),
                        latestMonitoring.getN(),
                        latestMonitoring.getP(),
                        latestMonitoring.getCaudalVolumen()
                    );
                
                // Construir y retornar el DTO de la estación
                return new MonitoringStationWithLastMonitoringDTO(
                    station.getId(),
                    station.getName(),
                    station.getDescription(),
                    station.getLatitude(),
                    station.getLongitude(),
                    lastMonitoringDTO
                );
            })
            .filter(dto -> dto != null) // Filtrar nulos (estaciones sin monitoreos)
            .toList();
    }
}
