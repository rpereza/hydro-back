package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.DischargeParameter;
import com.univercloud.hydro.entity.DischargeMonitoring;
import com.univercloud.hydro.entity.BasinSection;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.DischargeUserRepository;
import com.univercloud.hydro.repository.DischargeParameterRepository;
import com.univercloud.hydro.repository.DischargeMonitoringRepository;
import com.univercloud.hydro.repository.InvoiceRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.repository.BasinSectionRepository;
import com.univercloud.hydro.service.DischargeService;
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
 * Implementación del servicio para la gestión de Descargas.
 */
@Service
@Transactional
public class DischargeServiceImpl implements DischargeService {
    
    @Autowired
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private DischargeUserRepository dischargeUserRepository;
    
    @Autowired
    private DischargeParameterRepository dischargeParameterRepository;
    
    @Autowired
    private DischargeMonitoringRepository dischargeMonitoringRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private BasinSectionRepository basinSectionRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    public Discharge createDischarge(Discharge discharge) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Validar que el usuario de descarga pertenece a la corporación
        if (discharge.getDischargeUser() != null) {
            DischargeUser dischargeUser = dischargeUserRepository.findById(discharge.getDischargeUser().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("DischargeUser", "id", discharge.getDischargeUser().getId()));
            
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalStateException("Discharge user does not belong to your corporation");
            }
        }
        
        // Validar que el municipio existe
        if (discharge.getMunicipality() != null) {
            municipalityRepository.findById(discharge.getMunicipality().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Municipality", "id", discharge.getMunicipality().getId()));
        }
        
        // Validar que la sección de cuenca pertenece a la corporación
        if (discharge.getBasinSection() != null) {
            BasinSection basinSection = basinSectionRepository.findById(discharge.getBasinSection().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("BasinSection", "id", discharge.getBasinSection().getId()));
            
            // Comparar por ID para evitar problemas con proxies de Hibernate
            if (basinSection.getCorporation() == null || !basinSection.getCorporation().getId().equals(corporation.getId())) {
                throw new IllegalStateException("Basin section does not belong to your corporation");
            }
        }
        
        // Verificar que no existe una descarga con el mismo número y año en la corporación
        if (discharge.getNumber() != null && discharge.getYear() != null) {
            if (dischargeRepository.existsByCorporationAndNumberAndYear(corporation, discharge.getNumber(), discharge.getYear())) {
                throw new DuplicateResourceException("Discharge", "number and year", discharge.getNumber() + "/" + discharge.getYear());
            }
        }
        
        // Asignar corporación y usuario creador
        discharge.setCorporation(corporation);
        discharge.setCreatedBy(currentUser);
        discharge.setCreatedAt(LocalDateTime.now());
        
        // Guardar la descarga primero para obtener el ID
        Discharge savedDischarge = dischargeRepository.save(discharge);
        
        // Procesar DischargeParameters si existen
        if (discharge.getDischargeParameters() != null && !discharge.getDischargeParameters().isEmpty()) {
            for (DischargeParameter param : discharge.getDischargeParameters()) {
                // Validar que no existe un parámetro con el mismo mes y origen para esta descarga
                if (dischargeParameterRepository.existsByDischargeAndMonthAndOrigin(savedDischarge, param.getMonth(), param.getOrigin())) {
                    throw new DuplicateResourceException("DischargeParameter", "month and origin", param.getMonth() + "/" + param.getOrigin());
                }
                
                // Establecer relaciones y auditoría
                param.setDischarge(savedDischarge);
                param.setCorporation(corporation);
                param.setCreatedBy(currentUser);
                param.setCreatedAt(LocalDateTime.now());
            }
            dischargeParameterRepository.saveAll(discharge.getDischargeParameters());
        }
        
        // Procesar DischargeMonitorings si existen
        if (discharge.getDischargeMonitorings() != null && !discharge.getDischargeMonitorings().isEmpty()) {
            for (DischargeMonitoring monitoring : discharge.getDischargeMonitorings()) {
                // Validar que la estación de monitoreo pertenece a la corporación (si se proporciona)
                if (monitoring.getMonitoringStation() != null && monitoring.getMonitoringStation().getId() != null) {
                    // La validación se hará en el servicio de DischargeMonitoring si es necesario
                    // Por ahora solo establecemos las relaciones
                }
                
                // Establecer relaciones y auditoría
                monitoring.setDischarge(savedDischarge);
                monitoring.setCorporation(corporation);
                monitoring.setCreatedBy(currentUser);
                monitoring.setCreatedAt(LocalDateTime.now());
            }
            dischargeMonitoringRepository.saveAll(discharge.getDischargeMonitorings());
        }
        
        return savedDischarge;
    }
    
    @Override
    public Discharge updateDischarge(Discharge discharge) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Discharge existingDischarge = dischargeRepository.findById(discharge.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Discharge", "id", discharge.getId()));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || existingDischarge.getCorporation() == null || !existingDischarge.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge does not belong to your corporation");
        }
        
        // Validar cambios en número y año
        if (discharge.getNumber() != null && discharge.getYear() != null) {
            if (!discharge.getNumber().equals(existingDischarge.getNumber()) || 
                !discharge.getYear().equals(existingDischarge.getYear())) {
                
                if (dischargeRepository.existsByCorporationAndNumberAndYearExcludingId(corporation, discharge.getNumber(), discharge.getYear(), existingDischarge.getId())) {
                    throw new IllegalArgumentException("A discharge with number " + discharge.getNumber() + 
                            " and year " + discharge.getYear() + " already exists in your corporation");
                }
            }
        }
        
        // Actualizar campos
        existingDischarge.setDischargeUser(discharge.getDischargeUser());
        existingDischarge.setBasinSection(discharge.getBasinSection());
        existingDischarge.setMunicipality(discharge.getMunicipality());
        existingDischarge.setDischargeType(discharge.getDischargeType());
        existingDischarge.setNumber(discharge.getNumber());
        existingDischarge.setYear(discharge.getYear());
        existingDischarge.setName(discharge.getName());
        existingDischarge.setDischargePoint(discharge.getDischargePoint());
        existingDischarge.setWaterResourceType(discharge.getWaterResourceType());
        existingDischarge.setBasinRehuse(discharge.isBasinRehuse());
        existingDischarge.setCcDboVert(discharge.getCcDboVert());
        existingDischarge.setCcSstVert(discharge.getCcSstVert());
        existingDischarge.setCcDboCap(discharge.getCcDboCap());
        existingDischarge.setCcSstCap(discharge.getCcSstCap());
        existingDischarge.setCcDboTotal(discharge.getCcDboTotal());
        existingDischarge.setCcSstTotal(discharge.getCcSstTotal());
        existingDischarge.setDqo(discharge.getDqo());
        existingDischarge.setSourceMonitored(discharge.isSourceMonitored());
        existingDischarge.setUpdatedBy(currentUser);
        existingDischarge.setUpdatedAt(LocalDateTime.now());
        
        // Procesar DischargeParameters
        if (discharge.getDischargeParameters() != null) {
            // Cargar los parámetros existentes
            List<DischargeParameter> existingParameters = dischargeParameterRepository.findByDischarge(existingDischarge);
            
            // Crear un mapa de parámetros existentes por ID para búsqueda rápida
            java.util.Map<Long, DischargeParameter> existingParamsMap = new java.util.HashMap<>();
            for (DischargeParameter param : existingParameters) {
                existingParamsMap.put(param.getId(), param);
            }
            
            // Procesar los nuevos parámetros
            List<DischargeParameter> paramsToSave = new java.util.ArrayList<>();
            for (DischargeParameter newParam : discharge.getDischargeParameters()) {
                if (newParam.getId() != null && existingParamsMap.containsKey(newParam.getId())) {
                    // Actualizar parámetro existente
                    DischargeParameter existingParam = existingParamsMap.get(newParam.getId());
                    // Validar que no existe otro parámetro con el mismo mes y origen (excluyendo el actual)
                    if (dischargeParameterRepository.existsByDischargeAndMonthAndOriginExcludingId(
                            existingDischarge, newParam.getMonth(), newParam.getOrigin(), existingParam.getId())) {
                        throw new DuplicateResourceException("DischargeParameter", "month and origin", 
                                newParam.getMonth() + "/" + newParam.getOrigin());
                    }
                    
                    // Actualizar campos
                    existingParam.setMonth(newParam.getMonth());
                    existingParam.setOrigin(newParam.getOrigin());
                    existingParam.setCaudalVolumen(newParam.getCaudalVolumen());
                    existingParam.setFrequency(newParam.getFrequency());
                    existingParam.setDuration(newParam.getDuration());
                    existingParam.setConcDbo(newParam.getConcDbo());
                    existingParam.setConcSst(newParam.getConcSst());
                    existingParam.setCcDbo(newParam.getCcDbo());
                    existingParam.setCcSst(newParam.getCcSst());
                    existingParam.setUpdatedBy(currentUser);
                    existingParam.setUpdatedAt(LocalDateTime.now());
                    paramsToSave.add(existingParam);
                    existingParamsMap.remove(newParam.getId()); // Marcar como procesado
                } else {
                    // Nuevo parámetro
                    // Validar que no existe un parámetro con el mismo mes y origen
                    if (dischargeParameterRepository.existsByDischargeAndMonthAndOrigin(
                            existingDischarge, newParam.getMonth(), newParam.getOrigin())) {
                        throw new DuplicateResourceException("DischargeParameter", "month and origin", 
                                newParam.getMonth() + "/" + newParam.getOrigin());
                    }
                    
                    newParam.setDischarge(existingDischarge);
                    newParam.setCorporation(corporation);
                    newParam.setCreatedBy(currentUser);
                    newParam.setCreatedAt(LocalDateTime.now());
                    paramsToSave.add(newParam);
                }
            }
            
            // Eliminar parámetros que ya no están en la lista
            for (DischargeParameter paramToDelete : existingParamsMap.values()) {
                dischargeParameterRepository.delete(paramToDelete);
            }
            
            // Guardar los parámetros actualizados y nuevos
            if (!paramsToSave.isEmpty()) {
                dischargeParameterRepository.saveAll(paramsToSave);
            }
        }
        
        // Procesar DischargeMonitorings
        if (discharge.getDischargeMonitorings() != null) {
            // Cargar los monitoreos existentes
            List<DischargeMonitoring> existingMonitorings = dischargeMonitoringRepository.findByDischarge(existingDischarge);
            
            // Crear un mapa de monitoreos existentes por ID para búsqueda rápida
            java.util.Map<Long, DischargeMonitoring> existingMonitoringsMap = new java.util.HashMap<>();
            for (DischargeMonitoring monitoring : existingMonitorings) {
                existingMonitoringsMap.put(monitoring.getId(), monitoring);
            }
            
            // Procesar los nuevos monitoreos
            List<DischargeMonitoring> monitoringsToSave = new java.util.ArrayList<>();
            for (DischargeMonitoring newMonitoring : discharge.getDischargeMonitorings()) {
                if (newMonitoring.getId() != null && existingMonitoringsMap.containsKey(newMonitoring.getId())) {
                    // Actualizar monitoreo existente
                    DischargeMonitoring existingMonitoring = existingMonitoringsMap.get(newMonitoring.getId());
                    
                    // Actualizar campos
                    existingMonitoring.setMonitoringStation(newMonitoring.getMonitoringStation());
                    existingMonitoring.setOd(newMonitoring.getOd());
                    existingMonitoring.setSst(newMonitoring.getSst());
                    existingMonitoring.setDqo(newMonitoring.getDqo());
                    existingMonitoring.setCe(newMonitoring.getCe());
                    existingMonitoring.setPh(newMonitoring.getPh());
                    existingMonitoring.setN(newMonitoring.getN());
                    existingMonitoring.setP(newMonitoring.getP());
                    existingMonitoring.setRnp(newMonitoring.getRnp());
                    existingMonitoring.setIod(newMonitoring.getIod());
                    existingMonitoring.setIsst(newMonitoring.getIsst());
                    existingMonitoring.setIdqo(newMonitoring.getIdqo());
                    existingMonitoring.setIce(newMonitoring.getIce());
                    existingMonitoring.setIph(newMonitoring.getIph());
                    existingMonitoring.setIrnp(newMonitoring.getIrnp());
                    existingMonitoring.setCaudalVolumen(newMonitoring.getCaudalVolumen());
                    existingMonitoring.setLatitude(newMonitoring.getLatitude());
                    existingMonitoring.setLongitude(newMonitoring.getLongitude());
                    existingMonitoring.setUpdatedBy(currentUser);
                    existingMonitoring.setUpdatedAt(LocalDateTime.now());
                    monitoringsToSave.add(existingMonitoring);
                    existingMonitoringsMap.remove(newMonitoring.getId()); // Marcar como procesado
                } else {
                    // Nuevo monitoreo
                    newMonitoring.setDischarge(existingDischarge);
                    newMonitoring.setCorporation(corporation);
                    newMonitoring.setCreatedBy(currentUser);
                    newMonitoring.setCreatedAt(LocalDateTime.now());
                    monitoringsToSave.add(newMonitoring);
                }
            }
            
            // Eliminar monitoreos que ya no están en la lista
            for (DischargeMonitoring monitoringToDelete : existingMonitoringsMap.values()) {
                dischargeMonitoringRepository.delete(monitoringToDelete);
            }
            
            // Guardar los monitoreos actualizados y nuevos
            if (!monitoringsToSave.isEmpty()) {
                dischargeMonitoringRepository.saveAll(monitoringsToSave);
            }
        }
        
        return dischargeRepository.save(existingDischarge);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discharge> getDischargeById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return dischargeRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<Discharge> getMyCorporationDischarges(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByDischargeUser(Long dischargeUserId) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        DischargeUser dischargeUser = dischargeUserRepository.findById(dischargeUserId)
                .orElseThrow(() -> new IllegalArgumentException("Discharge user not found"));
        
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Discharge user does not belong to your corporation");
        }
        
        return dischargeRepository.findByDischargeUser(dischargeUser);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> getDischargesByYear(Integer year) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndYear(corporation, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Discharge> getDischargeByNumberAndYear(Integer number, Integer year) {
        return dischargeRepository.findByNumberAndYear(number, year);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Discharge> searchDischargesByName(String name) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeRepository.findByCorporationAndNameContainingIgnoreCase(corporation, name);
    }
    
    @Override
    public boolean deleteDischarge(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Discharge discharge = dischargeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Discharge", "id", id));
        
        Corporation corporation = currentUser.getCorporation();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (corporation == null || discharge.getCorporation() == null || !discharge.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("Access denied: Discharge does not belong to your corporation");
        }
        
        // Verificar si hay parámetros de descarga asociados
        long dischargeParameterCount = dischargeParameterRepository.countByDischargeId(id);
        if (dischargeParameterCount > 0) {
            throw new ResourceInUseException("Discharge", "id", id, "DischargeParameter", dischargeParameterCount);
        }
        
        // Verificar si hay monitoreos de descarga asociados
        long dischargeMonitoringCount = dischargeMonitoringRepository.countByDischargeId(id);
        if (dischargeMonitoringCount > 0) {
            throw new ResourceInUseException("Discharge", "id", id, "DischargeMonitoring", dischargeMonitoringCount);
        }
        
        // Verificar si hay facturas asociadas
        long invoiceCount = invoiceRepository.countByDischargeId(id);
        if (invoiceCount > 0) {
            throw new ResourceInUseException("Discharge", "id", id, "Invoice", invoiceCount);
        }
        
        dischargeRepository.delete(discharge);
        return true;
    }
    
}
