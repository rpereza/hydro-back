package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.repository.DischargeUserRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.InvoiceRepository;
import com.univercloud.hydro.repository.MunicipalityRepository;
import com.univercloud.hydro.service.DischargeUserService;
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
 * Implementación del servicio para la gestión de Usuarios de Descarga.
 * Proporciona operaciones CRUD y lógica de negocio para usuarios de descarga.
 */
@Service
@Transactional
public class DischargeUserServiceImpl implements DischargeUserService {
    
    @Autowired
    private DischargeUserRepository dischargeUserRepository;
    
    @Autowired
    private MunicipalityRepository municipalityRepository;
    
    @Autowired
    private DischargeRepository dischargeRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;
    
    @Autowired
    private AuthorizationUtils authorizationUtils;
    
    @Override
    @Transactional
    public DischargeUser createDischargeUser(DischargeUser dischargeUser) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Verificar que el código no exista en la corporación
        if (dischargeUserRepository.existsByCodeAndCorporationId(dischargeUser.getCode(), corporation.getId())) {
            throw new IllegalArgumentException("Ya existe un usuario de descarga con el código '" + dischargeUser.getCode() + "' en esta corporación");
        }
        
        // Verificar que el nombre de empresa no exista
        if (existsByCompanyName(dischargeUser.getCompanyName())) {
            throw new IllegalArgumentException("Ya existe un usuario de descarga con el nombre de empresa: " + dischargeUser.getCompanyName());
        }
        
        // Verificar que el tipo y número de documento no existan en la corporación
        if (dischargeUserRepository.existsByDocumentTypeAndDocumentNumberAndCorporationId(
                dischargeUser.getDocumentType(), dischargeUser.getDocumentNumber(), corporation.getId())) {
            throw new IllegalArgumentException("Ya existe un usuario de descarga con el tipo y número de documento especificados en esta corporación");
        }
        
        // Verificar que el municipio pertenezca a la corporación
        if (dischargeUser.getMunicipality() != null) {
            Optional<Municipality> municipalityOpt = municipalityRepository.findById(dischargeUser.getMunicipality().getId());
            if (municipalityOpt.isEmpty()) {
                throw new IllegalArgumentException("El municipio no existe");
            }
        }
        
        dischargeUser.setCorporation(corporation);
        dischargeUser.setCreatedBy(currentUser);
        dischargeUser.setCreatedAt(LocalDateTime.now());
        
        return dischargeUserRepository.save(dischargeUser);
    }
    
    @Override
    @Transactional
    public DischargeUser updateDischargeUser(DischargeUser dischargeUser) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<DischargeUser> existingOpt = dischargeUserRepository.findById(dischargeUser.getId());
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el usuario de descarga con ID: " + dischargeUser.getId());
        }
        
        DischargeUser existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        if (!existing.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para actualizar este usuario de descarga");
        }
        
        // Verificar que el código no exista en la corporación (si cambió)
        if (!existing.getCode().equals(dischargeUser.getCode())) {
            if (dischargeUserRepository.existsByCodeAndCorporationId(dischargeUser.getCode(), corporation.getId())) {
                throw new IllegalArgumentException("Ya existe un usuario de descarga con el código '" + dischargeUser.getCode() + "' en esta corporación");
            }
        }
        
        // Verificar que el nombre de empresa no exista (si cambió)
        if (!existing.getCompanyName().equals(dischargeUser.getCompanyName()) && existsByCompanyName(dischargeUser.getCompanyName())) {
            throw new IllegalArgumentException("Ya existe un usuario de descarga con el nombre de empresa: " + dischargeUser.getCompanyName());
        }
        
        // Verificar que el tipo y número de documento no existan en la corporación (si cambiaron)
        if (!existing.getDocumentType().equals(dischargeUser.getDocumentType()) || 
            !existing.getDocumentNumber().equals(dischargeUser.getDocumentNumber())) {
            if (dischargeUserRepository.existsByDocumentTypeAndDocumentNumberAndCorporationId(
                    dischargeUser.getDocumentType(), dischargeUser.getDocumentNumber(), corporation.getId())) {
                throw new IllegalArgumentException("Ya existe un usuario de descarga con el tipo y número de documento especificados en esta corporación");
            }
        }
        
        // Verificar que el municipio pertenezca a la corporación
        if (dischargeUser.getMunicipality() != null) {
            Optional<Municipality> municipalityOpt = municipalityRepository.findById(dischargeUser.getMunicipality().getId());
            if (municipalityOpt.isEmpty()) {
                throw new IllegalArgumentException("El municipio no existe");
            }
        }
        
        existing.setCompanyName(dischargeUser.getCompanyName());
        existing.setContactPerson(dischargeUser.getContactPerson());
        existing.setPhone(dischargeUser.getPhone());
        existing.setEmail(dischargeUser.getEmail());
        existing.setDocumentType(dischargeUser.getDocumentType());
        existing.setDocumentNumber(dischargeUser.getDocumentNumber());
        existing.setMunicipality(dischargeUser.getMunicipality());
        existing.setActive(dischargeUser.isActive());
        existing.setUpdatedBy(currentUser);
        existing.setUpdatedAt(LocalDateTime.now());
        existing.setEconomicActivity(dischargeUser.getEconomicActivity());
        existing.setAuthorizationType(dischargeUser.getAuthorizationType());
        existing.setPublicServiceCompany(dischargeUser.isPublicServiceCompany());
        existing.setHasPtar(dischargeUser.isHasPtar());
        existing.setEfficiencyPercentage(dischargeUser.getEfficiencyPercentage());
        existing.setFileNumber(dischargeUser.getFileNumber());
        existing.setAlternativeEmail(dischargeUser.getAlternativeEmail());
        existing.setAlternativePhone(dischargeUser.getAlternativePhone());
        existing.setAddress(dischargeUser.getAddress());
        existing.setCode(dischargeUser.getCode());
        
        return dischargeUserRepository.save(existing);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DischargeUser> getDischargeUserById(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        // Buscar directamente por ID y corporationId para evitar problemas con lazy loading
        return dischargeUserRepository.findByIdAndCorporationId(id, corporation.getId());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<DischargeUser> getMyCorporationDischargeUsers(Pageable pageable) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return dischargeUserRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getAllMyCorporationDischargeUsers() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return dischargeUserRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getActiveMyCorporationDischargeUsers() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return dischargeUserRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByMunicipality(Long municipalityId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(municipalityId);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("El municipio no existe");
        }
        
        return dischargeUserRepository.findByMunicipality(municipalityOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getActiveDischargeUsersByMunicipality(Long municipalityId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(municipalityId);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("El municipio no existe");
        }
        
        return dischargeUserRepository.findByMunicipalityAndIsActiveTrue(municipalityOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DischargeUser> getDischargeUserByCompanyName(String companyName) {
        return dischargeUserRepository.findByCompanyName(companyName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> searchDischargeUsersByCompanyName(String companyName) {
        return dischargeUserRepository.findByCompanyNameContainingIgnoreCase(companyName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> searchDischargeUsersByMunicipalityAndCompanyName(Long municipalityId, String companyName) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(municipalityId);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("El municipio no existe");
        }
        
        return dischargeUserRepository.findByMunicipalityAndCompanyNameContainingIgnoreCase(municipalityOpt.get(), companyName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByDocumentType(DischargeUser.DocumentType documentType) {
        return dischargeUserRepository.findByDocumentType(documentType);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByDocumentNumber(String documentNumber) {
        return dischargeUserRepository.findByDocumentNumber(documentNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<DischargeUser> getDischargeUserByDocumentTypeAndNumber(DischargeUser.DocumentType documentType, String documentNumber) {
        return dischargeUserRepository.findByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return dischargeUserRepository.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countDischargeUsersByMunicipality(Long municipalityId) {
        return dischargeUserRepository.countByMunicipalityId(municipalityId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countActiveDischargeUsersByMunicipality(Long municipalityId) {
        return dischargeUserRepository.countActiveByMunicipalityId(municipalityId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long countMyCorporationDischargeUsers() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        return dischargeUserRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional
    public boolean activateDischargeUser(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el usuario de descarga con ID: " + id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        if (!dischargeUser.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para activar este usuario de descarga");
        }
        
        dischargeUser.setActive(true);
        dischargeUser.setUpdatedBy(currentUser);
        dischargeUser.setUpdatedAt(LocalDateTime.now());
        
        dischargeUserRepository.save(dischargeUser);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deactivateDischargeUser(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el usuario de descarga con ID: " + id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        if (!dischargeUser.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para desactivar este usuario de descarga");
        }
        
        dischargeUser.setActive(false);
        dischargeUser.setUpdatedBy(currentUser);
        dischargeUser.setUpdatedAt(LocalDateTime.now());
        
        dischargeUserRepository.save(dischargeUser);
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteDischargeUser(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new IllegalArgumentException("No se encontró el usuario de descarga con ID: " + id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        if (!dischargeUser.getCorporation().equals(corporation)) {
            throw new IllegalArgumentException("No tiene permisos para eliminar este usuario de descarga");
        }
        
        dischargeUserRepository.delete(dischargeUser);
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByCompanyName(String companyName) {
        return dischargeUserRepository.existsByCompanyName(companyName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByDocumentTypeAndNumber(DischargeUser.DocumentType documentType, String documentNumber) {
        return dischargeUserRepository.existsByDocumentTypeAndDocumentNumber(documentType, documentNumber);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersOrderByCompanyName() {
        return dischargeUserRepository.findAllOrderByCompanyName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByMunicipalityOrderByCompanyName(Long municipalityId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        Optional<Municipality> municipalityOpt = municipalityRepository.findById(municipalityId);
        if (municipalityOpt.isEmpty()) {
            throw new IllegalArgumentException("El municipio no existe");
        }
        
        return dischargeUserRepository.findByMunicipalityOrderByCompanyName(municipalityOpt.get());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getActiveDischargeUsersOrderByCompanyName() {
        return dischargeUserRepository.findActiveOrderByCompanyName();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersOrderByCreatedAtDesc() {
        return dischargeUserRepository.findAllOrderByCreatedAtDesc();
    }
    
    @Override
    @Transactional(readOnly = true)
    public DischargeUserStats getMyCorporationDischargeUserStats() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("El usuario debe pertenecer a una corporación");
        }
        
        DischargeUserStats stats = new DischargeUserStats();
        
        // Estadísticas básicas
        List<DischargeUser> allDischargeUsers = dischargeUserRepository.findByCorporation(corporation);
        long totalDischargeUsers = allDischargeUsers.size();
        long activeDischargeUsers = dischargeUserRepository.findByCorporationAndIsActiveTrue(corporation).size();
        long inactiveDischargeUsers = totalDischargeUsers - activeDischargeUsers;
        
        // Estadísticas de descargas
        long totalDischarges = dischargeRepository.countByCorporationId(corporation.getId());
        
        // Estadísticas de facturas
        long totalInvoices = invoiceRepository.countByCorporationId(corporation.getId());
        
        // Estadísticas de municipios con usuarios
        List<Municipality> municipalities = municipalityRepository.findAll();
        long municipalitiesWithUsers = 0;
        for (Municipality municipality : municipalities) {
            if (dischargeUserRepository.countByMunicipalityId(municipality.getId()) > 0) {
                municipalitiesWithUsers++;
            }
        }
        
        // Estadísticas de tipos de documento utilizados
        long documentTypesUsed = allDischargeUsers.stream()
            .map(DischargeUser::getDocumentType)
            .distinct()
            .count();
        
        stats.setTotalDischargeUsers(totalDischargeUsers);
        stats.setActiveDischargeUsers(activeDischargeUsers);
        stats.setInactiveDischargeUsers(inactiveDischargeUsers);
        stats.setTotalDischarges(totalDischarges);
        stats.setTotalInvoices(totalInvoices);
        stats.setMunicipalitiesWithUsers(municipalitiesWithUsers);
        stats.setDocumentTypesUsed(documentTypesUsed);
        
        return stats;
    }
}
