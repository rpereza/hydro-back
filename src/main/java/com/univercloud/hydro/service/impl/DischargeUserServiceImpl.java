package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.User;
import com.univercloud.hydro.entity.Municipality;
import com.univercloud.hydro.exception.DuplicateResourceException;
import com.univercloud.hydro.exception.ResourceInUseException;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.repository.DischargeUserRepository;
import com.univercloud.hydro.repository.DischargeRepository;
import com.univercloud.hydro.repository.ProjectProgressRepository;
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
    private ProjectProgressRepository projectProgressRepository;
    
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        // Verificar que el código no exista en la corporación
        if (dischargeUserRepository.existsByCodeAndCorporationId(dischargeUser.getCode(), corporation.getId())) {
            throw new DuplicateResourceException("DischargeUser", "code", dischargeUser.getCode());
        }
        
        // Verificar que el nombre de empresa no exista en la corporación
        if (dischargeUser.getCompanyName() != null && dischargeUserRepository.existsByCorporationAndCompanyName(corporation, dischargeUser.getCompanyName())) {
            throw new DuplicateResourceException("DischargeUser", "companyName", dischargeUser.getCompanyName());
        }
        
        // Verificar que el tipo y número de documento no existan en la corporación
        if (dischargeUserRepository.existsByDocumentTypeAndDocumentNumberAndCorporationId(
                dischargeUser.getDocumentType(), dischargeUser.getDocumentNumber(), corporation.getId())) {
            throw new DuplicateResourceException("DischargeUser", "documentType and documentNumber", dischargeUser.getDocumentType() + "/" + dischargeUser.getDocumentNumber());
        }
        
        // Verificar que el municipio pertenezca a la corporación
        if (dischargeUser.getMunicipality() != null) {
            Optional<Municipality> municipalityOpt = municipalityRepository.findById(dischargeUser.getMunicipality().getId());
            if (municipalityOpt.isEmpty()) {
                throw new IllegalArgumentException("Municipality does not exist");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<DischargeUser> existingOpt = dischargeUserRepository.findById(dischargeUser.getId());
        if (existingOpt.isEmpty()) {
            throw new ResourceNotFoundException("DischargeUser", "id", dischargeUser.getId());
        }
        
        DischargeUser existing = existingOpt.get();
        
        // Verificar que pertenezca a la corporación del usuario
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (existing.getCorporation() == null || !existing.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("You do not have permission to update this discharge user");
        }
        
        // Verificar que el código no exista en la corporación (si cambió)
        if (!existing.getCode().equals(dischargeUser.getCode())) {
            if (dischargeUserRepository.existsByCodeAndCorporationId(dischargeUser.getCode(), corporation.getId())) {
                throw new DuplicateResourceException("DischargeUser", "code", dischargeUser.getCode());
            }
        }
        
        // Verificar que el nombre de empresa no exista en la corporación (si cambió)
        if (dischargeUser.getCompanyName() != null && !existing.getCompanyName().equals(dischargeUser.getCompanyName())) {
            if (dischargeUserRepository.existsByCorporationAndCompanyNameExcludingId(corporation, dischargeUser.getCompanyName(), existing.getId())) {
                throw new DuplicateResourceException("DischargeUser", "companyName", dischargeUser.getCompanyName());
            }
        }
        
        // Verificar que el tipo y número de documento no existan en la corporación (si cambiaron)
        if (!existing.getDocumentType().equals(dischargeUser.getDocumentType()) || 
            !existing.getDocumentNumber().equals(dischargeUser.getDocumentNumber())) {
            if (dischargeUserRepository.existsByDocumentTypeAndDocumentNumberAndCorporationId(
                    dischargeUser.getDocumentType(), dischargeUser.getDocumentNumber(), corporation.getId())) {
                throw new DuplicateResourceException("DischargeUser", "documentType and documentNumber", dischargeUser.getDocumentType() + "/" + dischargeUser.getDocumentNumber());
            }
        }
        
        // Verificar que el municipio pertenezca a la corporación
        if (dischargeUser.getMunicipality() != null) {
            Optional<Municipality> municipalityOpt = municipalityRepository.findById(dischargeUser.getMunicipality().getId());
            if (municipalityOpt.isEmpty()) {
                throw new IllegalArgumentException("Municipality does not exist");
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
            throw new IllegalStateException("User must belong to a corporation");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return dischargeUserRepository.findByCorporation(corporation, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getAllMyCorporationDischargeUsers() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return dischargeUserRepository.findByCorporation(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getActiveMyCorporationDischargeUsers() {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return dischargeUserRepository.findByCorporationAndIsActiveTrue(corporation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> getDischargeUsersByMunicipality(Long municipalityId) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
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
            throw new IllegalStateException("User must belong to a corporation");
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
        User currentUser = authorizationUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        Corporation corporation = currentUser.getCorporation();
        if (corporation == null) {
            throw new IllegalStateException("User does not belong to a corporation");
        }
        
        return dischargeUserRepository.findByCorporationAndCompanyNameContainingIgnoreCase(corporation, companyName);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DischargeUser> searchDischargeUsersByMunicipalityAndCompanyName(Long municipalityId, String companyName) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        return dischargeUserRepository.countByCorporationId(corporation.getId());
    }
    
    @Override
    @Transactional
    public boolean activateDischargeUser(Long id) {
        User currentUser = authorizationUtils.getCurrentUser();
        Corporation corporation = currentUser.getCorporation();
        
        if (corporation == null) {
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new ResourceNotFoundException("DischargeUser", "id", id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("You do not have permission to activate this discharge user");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new ResourceNotFoundException("DischargeUser", "id", id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("You do not have permission to deactivate this discharge user");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        Optional<DischargeUser> dischargeUserOpt = dischargeUserRepository.findById(id);
        if (dischargeUserOpt.isEmpty()) {
            throw new ResourceNotFoundException("DischargeUser", "id", id);
        }
        
        DischargeUser dischargeUser = dischargeUserOpt.get();
        // Comparar por ID para evitar problemas con proxies de Hibernate
        if (dischargeUser.getCorporation() == null || !dischargeUser.getCorporation().getId().equals(corporation.getId())) {
            throw new IllegalStateException("You do not have permission to delete this discharge user");
        }
        
        // Verificar si hay descargas asociadas
        long dischargeCount = dischargeUser.getDischarges().size();
        if (dischargeCount > 0) {
            throw new ResourceInUseException("DischargeUser", "id", id, "Discharge", dischargeCount);
        }
        
        // Verificar si hay progresos de proyecto asociados
        long projectProgressCount = projectProgressRepository.countByDischargeUserId(id);
        if (projectProgressCount > 0) {
            throw new ResourceInUseException("DischargeUser", "id", id, "ProjectProgress", projectProgressCount);
        }
        
        dischargeUserRepository.delete(dischargeUser);
        return true;
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
            throw new IllegalStateException("User must belong to a corporation");
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
            throw new IllegalStateException("User must belong to a corporation");
        }
        
        DischargeUserStats stats = new DischargeUserStats();
        
        // Estadísticas básicas
        List<DischargeUser> allDischargeUsers = dischargeUserRepository.findByCorporation(corporation);
        long totalDischargeUsers = allDischargeUsers.size();
        long activeDischargeUsers = dischargeUserRepository.findByCorporationAndIsActiveTrue(corporation).size();
        long inactiveDischargeUsers = totalDischargeUsers - activeDischargeUsers;
        
        // Estadísticas de descargas
        long totalDischarges = allDischargeUsers.stream()
            .map(DischargeUser::getDischarges)
            .flatMap(List::stream)
            .count();
        
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
