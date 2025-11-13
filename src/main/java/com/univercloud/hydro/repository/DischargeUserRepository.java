package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.DischargeUser;
import com.univercloud.hydro.entity.Municipality;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad DischargeUser.
 * Proporciona métodos de consulta para gestionar usuarios de descarga.
 */
@Repository
public interface DischargeUserRepository extends JpaRepository<DischargeUser, Long> {
    
    /**
     * Busca usuarios de descarga por municipio.
     * @param municipality el municipio
     * @return lista de usuarios de descarga del municipio
     */
    List<DischargeUser> findByMunicipality(Municipality municipality);
    
    /**
     * Busca usuarios de descarga por corporación.
     * @param corporation la corporación
     * @return lista de usuarios de descarga de la corporación
     */
    List<DischargeUser> findByCorporation(Corporation corporation);
    
    /**
     * Busca usuarios de descarga activos.
     * @return lista de usuarios de descarga activos
     */
    List<DischargeUser> findByIsActiveTrue();
    
    /**
     * Busca usuarios de descarga inactivos.
     * @return lista de usuarios de descarga inactivos
     */
    List<DischargeUser> findByIsActiveFalse();
    
    /**
     * Busca usuarios de descarga activos por municipio.
     * @param municipality el municipio
     * @return lista de usuarios de descarga activos del municipio
     */
    List<DischargeUser> findByMunicipalityAndIsActiveTrue(Municipality municipality);
    
    /**
     * Busca un usuario de descarga por nombre de empresa.
     * @param companyName el nombre de la empresa
     * @return el usuario de descarga si existe
     */
    Optional<DischargeUser> findByCompanyName(String companyName);
    
    /**
     * Verifica si existe un usuario de descarga con el nombre de empresa especificado.
     * @param companyName el nombre de la empresa
     * @return true si existe, false en caso contrario
     */
    boolean existsByCompanyName(String companyName);
    
    /**
     * Busca usuarios de descarga por nombre de empresa (búsqueda parcial, case-insensitive).
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga que coinciden con el nombre
     */
    @Query("SELECT du FROM DischargeUser du WHERE LOWER(du.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<DischargeUser> findByCompanyNameContainingIgnoreCase(@Param("companyName") String companyName);
    
    /**
     * Busca usuarios de descarga por municipio y nombre de empresa (búsqueda parcial, case-insensitive).
     * @param municipality el municipio
     * @param companyName el nombre o parte del nombre de empresa a buscar
     * @return lista de usuarios de descarga del municipio que coinciden con el nombre
     */
    @Query("SELECT du FROM DischargeUser du WHERE du.municipality = :municipality AND LOWER(du.companyName) LIKE LOWER(CONCAT('%', :companyName, '%'))")
    List<DischargeUser> findByMunicipalityAndCompanyNameContainingIgnoreCase(@Param("municipality") Municipality municipality, @Param("companyName") String companyName);
    
    /**
     * Busca usuarios de descarga por tipo de documento.
     * @param documentType el tipo de documento
     * @return lista de usuarios de descarga con el tipo de documento
     */
    List<DischargeUser> findByDocumentType(DischargeUser.DocumentType documentType);
    
    /**
     * Busca usuarios de descarga por número de documento.
     * @param documentNumber el número de documento
     * @return lista de usuarios de descarga con el número de documento
     */
    List<DischargeUser> findByDocumentNumber(String documentNumber);
    
    /**
     * Busca un usuario de descarga por tipo y número de documento.
     * @param documentType el tipo de documento
     * @param documentNumber el número de documento
     * @return el usuario de descarga si existe
     */
    Optional<DischargeUser> findByDocumentTypeAndDocumentNumber(DischargeUser.DocumentType documentType, String documentNumber);
    
    /**
     * Verifica si existe un usuario de descarga con el tipo y número de documento especificados.
     * @param documentType el tipo de documento
     * @param documentNumber el número de documento
     * @return true si existe, false en caso contrario
     */
    boolean existsByDocumentTypeAndDocumentNumber(DischargeUser.DocumentType documentType, String documentNumber);
    
    /**
     * Verifica si existe un usuario de descarga con el código especificado en la corporación.
     * @param code el código del usuario de descarga
     * @param corporationId el ID de la corporación
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(du) > 0 THEN true ELSE false END FROM DischargeUser du WHERE du.code = :code AND du.corporation.id = :corporationId")
    boolean existsByCodeAndCorporationId(@Param("code") String code, @Param("corporationId") Long corporationId);
    
    /**
     * Verifica si existe un usuario de descarga con el tipo y número de documento especificados en la corporación.
     * @param documentType el tipo de documento
     * @param documentNumber el número de documento
     * @param corporationId el ID de la corporación
     * @return true si existe, false en caso contrario
     */
    @Query("SELECT CASE WHEN COUNT(du) > 0 THEN true ELSE false END FROM DischargeUser du WHERE du.documentType = :documentType AND du.documentNumber = :documentNumber AND du.corporation.id = :corporationId")
    boolean existsByDocumentTypeAndDocumentNumberAndCorporationId(@Param("documentType") DischargeUser.DocumentType documentType, @Param("documentNumber") String documentNumber, @Param("corporationId") Long corporationId);
    
    /**
     * Busca usuarios de descarga creados en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de usuarios de descarga creados en el rango
     */
    List<DischargeUser> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta usuarios de descarga por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga del municipio
     */
    @Query("SELECT COUNT(du) FROM DischargeUser du WHERE du.municipality.id = :municipalityId")
    long countByMunicipalityId(@Param("municipalityId") Long municipalityId);
    
    /**
     * Cuenta usuarios de descarga activos por municipio.
     * @param municipalityId el ID del municipio
     * @return número de usuarios de descarga activos del municipio
     */
    @Query("SELECT COUNT(du) FROM DischargeUser du WHERE du.municipality.id = :municipalityId AND du.isActive = true")
    long countActiveByMunicipalityId(@Param("municipalityId") Long municipalityId);
    
    /**
     * Cuenta usuarios de descarga por corporación.
     * @param corporationId el ID de la corporación
     * @return número de usuarios de descarga de la corporación
     */
    @Query("SELECT COUNT(du) FROM DischargeUser du WHERE du.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca usuarios de descarga ordenados por nombre de empresa.
     * @return lista de usuarios de descarga ordenados por nombre de empresa
     */
    @Query("SELECT du FROM DischargeUser du ORDER BY du.companyName ASC")
    List<DischargeUser> findAllOrderByCompanyName();
    
    /**
     * Busca usuarios de descarga por municipio ordenados por nombre de empresa.
     * @param municipality el municipio
     * @return lista de usuarios de descarga del municipio ordenados por nombre de empresa
     */
    @Query("SELECT du FROM DischargeUser du WHERE du.municipality = :municipality ORDER BY du.companyName ASC")
    List<DischargeUser> findByMunicipalityOrderByCompanyName(@Param("municipality") Municipality municipality);
    
    /**
     * Busca usuarios de descarga activos ordenados por nombre de empresa.
     * @return lista de usuarios de descarga activos ordenados por nombre de empresa
     */
    @Query("SELECT du FROM DischargeUser du WHERE du.isActive = true ORDER BY du.companyName ASC")
    List<DischargeUser> findActiveOrderByCompanyName();
    
    /**
     * Busca usuarios de descarga ordenados por fecha de creación (más recientes primero).
     * @return lista de usuarios de descarga ordenados por fecha de creación descendente
     */
    @Query("SELECT du FROM DischargeUser du ORDER BY du.createdAt DESC")
    List<DischargeUser> findAllOrderByCreatedAtDesc();

    /**
     * Busca usuarios de descarga activos por corporación.
     * @param corporation la corporación
     * @return lista de usuarios de descarga activos de la corporación
     */
    List<DischargeUser> findByCorporationAndIsActiveTrue(Corporation corporation);

    /**
     * Busca usuarios de descarga por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de usuarios de descarga de la corporación
     */
    Page<DischargeUser> findByCorporation(Corporation corporation, Pageable pageable);
    
    /**
     * Busca un usuario de descarga por ID y corporación.
     * @param id el ID del usuario de descarga
     * @param corporationId el ID de la corporación
     * @return el usuario de descarga si existe y pertenece a la corporación
     */
    @Query("SELECT du FROM DischargeUser du WHERE du.id = :id AND du.corporation.id = :corporationId")
    Optional<DischargeUser> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);
}
