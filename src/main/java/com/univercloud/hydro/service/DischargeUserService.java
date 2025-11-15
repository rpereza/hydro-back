package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.DischargeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Usuarios de Descarga.
 * Proporciona operaciones CRUD y lógica de negocio para usuarios de descarga.
 */
public interface DischargeUserService {
    
    /**
     * Crea un nuevo usuario de descarga.
     * @param dischargeUser el usuario de descarga a crear
     * @return el usuario de descarga creado
     * @throws IllegalArgumentException si los datos son inválidos
     */
    DischargeUser createDischargeUser(DischargeUser dischargeUser);
    
    /**
     * Actualiza un usuario de descarga existente.
     * @param dischargeUser el usuario de descarga a actualizar
     * @return el usuario de descarga actualizado
     * @throws IllegalArgumentException si el usuario de descarga no existe
     */
    DischargeUser updateDischargeUser(DischargeUser dischargeUser);
    
    /**
     * Obtiene un usuario de descarga por su ID.
     * @param id el ID del usuario de descarga
     * @return el usuario de descarga, si existe
     */
    Optional<DischargeUser> getDischargeUserById(Long id);
    
    /**
     * Obtiene todos los usuarios de descarga de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de usuarios de descarga
     */
    Page<DischargeUser> getMyCorporationDischargeUsers(Pageable pageable);
    
    /**
     * Obtiene todos los usuarios de descarga de la corporación del usuario autenticado.
     * @return lista de usuarios de descarga
     */
    List<DischargeUser> getAllMyCorporationDischargeUsers();
    
    /**
     * Obtiene usuarios de descarga activos de la corporación del usuario autenticado.
     * @return lista de usuarios de descarga activos
     */
    List<DischargeUser> getActiveMyCorporationDischargeUsers();
    
    /**
     * Obtiene usuarios de descarga por municipio.
     * @param municipalityId el ID del municipio
     * @return lista de usuarios de descarga del municipio
     */
    List<DischargeUser> getDischargeUsersByMunicipality(Long municipalityId);
    
    /**
     * Obtiene usuarios de descarga activos por municipio.
     * @param municipalityId el ID del municipio
     * @return lista de usuarios de descarga activos del municipio
     */
    List<DischargeUser> getActiveDischargeUsersByMunicipality(Long municipalityId);
    
    /**
     * Busca un usuario de descarga por nombre de empresa.
     * @param companyName el nombre de la empresa
     * @return el usuario de descarga si existe
     */
    Optional<DischargeUser> getDischargeUserByCompanyName(String companyName);
    
    /**
     * Busca usuarios de descarga por nombre de empresa (búsqueda parcial).
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga que coinciden
     */
    List<DischargeUser> searchDischargeUsersByCompanyName(String companyName);
    
    /**
     * Busca usuarios de descarga por municipio y nombre de empresa (búsqueda parcial).
     * @param municipalityId el ID del municipio
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga del municipio que coinciden
     */
    List<DischargeUser> searchDischargeUsersByMunicipalityAndCompanyName(Long municipalityId, String companyName);
    
    /**
     * Obtiene usuarios de descarga por tipo de documento.
     * @param documentType el tipo de documento
     * @return lista de usuarios de descarga con el tipo de documento
     */
    List<DischargeUser> getDischargeUsersByDocumentType(DischargeUser.DocumentType documentType);
    
    /**
     * Obtiene usuarios de descarga por número de documento.
     * @param documentNumber el número de documento
     * @return lista de usuarios de descarga con el número de documento
     */
    List<DischargeUser> getDischargeUsersByDocumentNumber(String documentNumber);
    
    /**
     * Busca un usuario de descarga por tipo y número de documento.
     * @param documentType el tipo de documento
     * @param documentNumber el número de documento
     * @return el usuario de descarga si existe
     */
    Optional<DischargeUser> getDischargeUserByDocumentTypeAndNumber(DischargeUser.DocumentType documentType, String documentNumber);
    
    /**
     * Obtiene usuarios de descarga creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de usuarios de descarga creados en el rango
     */
    List<DischargeUser> getDischargeUsersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta usuarios de descarga por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga del municipio
     */
    long countDischargeUsersByMunicipality(Long municipalityId);
    
    /**
     * Cuenta usuarios de descarga activos por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga activos del municipio
     */
    long countActiveDischargeUsersByMunicipality(Long municipalityId);
    
    /**
     * Cuenta el número de usuarios de descarga de la corporación del usuario autenticado.
     * @return número de usuarios de descarga
     */
    long countMyCorporationDischargeUsers();
    
    /**
     * Activa un usuario de descarga.
     * @param id el ID del usuario de descarga a activar
     * @return true si se activó correctamente
     * @throws IllegalArgumentException si el usuario de descarga no existe
     */
    boolean activateDischargeUser(Long id);
    
    /**
     * Desactiva un usuario de descarga.
     * @param id el ID del usuario de descarga a desactivar
     * @return true si se desactivó correctamente
     * @throws IllegalArgumentException si el usuario de descarga no existe
     */
    boolean deactivateDischargeUser(Long id);
    
    /**
     * Elimina un usuario de descarga.
     * @param id el ID del usuario de descarga a eliminar
     * @return true si se eliminó correctamente
     * @throws IllegalArgumentException si el usuario de descarga no existe
     */
    boolean deleteDischargeUser(Long id);
    
    /**
     * Verifica si existe un usuario de descarga con el tipo y número de documento especificados.
     * @param documentType el tipo de documento
     * @param documentNumber el número de documento
     * @return true si existe, false en caso contrario
     */
    boolean existsByDocumentTypeAndNumber(DischargeUser.DocumentType documentType, String documentNumber);
    
    /**
     * Obtiene usuarios de descarga ordenados por nombre de empresa.
     * @return lista de usuarios de descarga ordenados por nombre de empresa
     */
    List<DischargeUser> getDischargeUsersOrderByCompanyName();
    
    /**
     * Obtiene usuarios de descarga por municipio ordenados por nombre de empresa.
     * @param municipalityId el ID del municipio
     * @return lista de usuarios de descarga del municipio ordenados por nombre de empresa
     */
    List<DischargeUser> getDischargeUsersByMunicipalityOrderByCompanyName(Long municipalityId);
    
    /**
     * Obtiene usuarios de descarga activos ordenados por nombre de empresa.
     * @return lista de usuarios de descarga activos ordenados por nombre de empresa
     */
    List<DischargeUser> getActiveDischargeUsersOrderByCompanyName();
    
    /**
     * Obtiene usuarios de descarga ordenados por fecha de creación (más recientes primero).
     * @return lista de usuarios de descarga ordenados por fecha de creación descendente
     */
    List<DischargeUser> getDischargeUsersOrderByCreatedAtDesc();
    
    /**
     * Obtiene estadísticas de usuarios de descarga de la corporación del usuario autenticado.
     * @return estadísticas de usuarios de descarga
     */
    DischargeUserStats getMyCorporationDischargeUserStats();
    
    /**
     * Clase interna para estadísticas de usuarios de descarga
     */
    class DischargeUserStats {
        private long totalDischargeUsers;
        private long activeDischargeUsers;
        private long inactiveDischargeUsers;
        private long totalDischarges;
        private long totalInvoices;
        private long municipalitiesWithUsers;
        private long documentTypesUsed;
        
        // Constructors
        public DischargeUserStats() {}
        
        // Getters and Setters
        public long getTotalDischargeUsers() { return totalDischargeUsers; }
        public void setTotalDischargeUsers(long totalDischargeUsers) { this.totalDischargeUsers = totalDischargeUsers; }
        
        public long getActiveDischargeUsers() { return activeDischargeUsers; }
        public void setActiveDischargeUsers(long activeDischargeUsers) { this.activeDischargeUsers = activeDischargeUsers; }
        
        public long getInactiveDischargeUsers() { return inactiveDischargeUsers; }
        public void setInactiveDischargeUsers(long inactiveDischargeUsers) { this.inactiveDischargeUsers = inactiveDischargeUsers; }
        
        public long getTotalDischarges() { return totalDischarges; }
        public void setTotalDischarges(long totalDischarges) { this.totalDischarges = totalDischarges; }
        
        public long getTotalInvoices() { return totalInvoices; }
        public void setTotalInvoices(long totalInvoices) { this.totalInvoices = totalInvoices; }
        
        public long getMunicipalitiesWithUsers() { return municipalitiesWithUsers; }
        public void setMunicipalitiesWithUsers(long municipalitiesWithUsers) { this.municipalitiesWithUsers = municipalitiesWithUsers; }
        
        public long getDocumentTypesUsed() { return documentTypesUsed; }
        public void setDocumentTypesUsed(long documentTypesUsed) { this.documentTypesUsed = documentTypesUsed; }
    }
}
