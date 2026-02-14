package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.Discharge;
import com.univercloud.hydro.entity.Invoice;
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
 * Repository para la entidad Invoice.
 * Proporciona métodos de consulta para gestionar facturas.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    /**
     * Busca facturas por descarga.
     * @param discharge la descarga
     * @return lista de facturas de la descarga
     */
    List<Invoice> findByDischarge(Discharge discharge);
    
    /**
     * Busca facturas por corporación.
     * @param corporation la corporación
     * @return lista de facturas de la corporación
     */
    List<Invoice> findByCorporation(Corporation corporation);
    
    /**
     * Busca facturas por corporación con paginación.
     * @param corporation la corporación
     * @param pageable parámetros de paginación
     * @return página de facturas de la corporación
     */
    Page<Invoice> findByCorporation(Corporation corporation, Pageable pageable);

    /**
     * Busca facturas por número de factura.
     * @param number el número de factura
     * @return la factura si existe
     */
    Optional<Invoice> findByNumber(Integer number);
    
    /**
     * Verifica si existe una factura con el número especificado.
     * @param number el número de factura
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumber(Integer number);
    
    /**
     * Busca facturas por número de factura (búsqueda parcial, case-insensitive).
     * @param number el número o parte del número de factura a buscar
     * @return lista de facturas que coinciden con el número
     */
    @Query("SELECT i FROM Invoice i WHERE i.number = :number")
    List<Invoice> findByNumber(@Param("number") int number);
    
    /**
     * Busca facturas por descarga y número de factura (búsqueda parcial, case-insensitive).
     * @param discharge la descarga
     * @param number el número de factura
     * @return lista de facturas de la descarga que coinciden con el número
     */
    @Query("SELECT i FROM Invoice i WHERE i.discharge = :discharge AND i.number = :number")
    List<Invoice> findByDischargeAndNumber(@Param("discharge") Discharge discharge, @Param("number") int number);
    
    /**
     * Busca facturas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de facturas creadas en el rango
     */
    List<Invoice> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta facturas por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de facturas de la descarga
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.discharge.id = :dischargeId")
    long countByDischargeId(@Param("dischargeId") Long dischargeId);
    
    /**
     * Cuenta facturas por corporación.
     * @param corporationId el ID de la corporación
     * @return número de facturas de la corporación
     */
    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.corporation.id = :corporationId")
    long countByCorporationId(@Param("corporationId") Long corporationId);
    
    /**
     * Busca facturas ordenadas por fecha de creación (más recientes primero).
     * @return lista de facturas ordenadas por fecha de creación descendente
     */
    @Query("SELECT i FROM Invoice i ORDER BY i.createdAt DESC")
    List<Invoice> findAllOrderByCreatedAtDesc();
    
    /**
     * Busca facturas por descarga ordenadas por fecha de creación descendente.
     * @param discharge la descarga
     * @return lista de facturas de la descarga ordenadas por fecha descendente
     */
    @Query("SELECT i FROM Invoice i WHERE i.discharge = :discharge ORDER BY i.createdAt DESC")
    List<Invoice> findByDischargeOrderByCreatedAtDesc(@Param("discharge") Discharge discharge);
    
    /**
     * Busca facturas por corporación ordenadas por fecha de creación descendente.
     * @param corporation la corporación
     * @return lista de facturas de la corporación ordenadas por fecha descendente
     */
    @Query("SELECT i FROM Invoice i WHERE i.corporation = :corporation ORDER BY i.createdAt DESC")
    List<Invoice> findByCorporationOrderByCreatedAtDesc(@Param("corporation") Corporation corporation);
    
    /**
     * Busca facturas por año (basado en la fecha de creación).
     * @param year el año
     * @return lista de facturas del año
     */
    @Query("SELECT i FROM Invoice i WHERE YEAR(i.createdAt) = :year")
    List<Invoice> findByYear(@Param("year") int year);
    
    /**
     * Busca facturas por descarga y año.
     * @param discharge la descarga
     * @param year el año
     * @return lista de facturas de la descarga y año
     */
    @Query("SELECT i FROM Invoice i WHERE i.discharge = :discharge AND YEAR(i.createdAt) = :year")
    List<Invoice> findByDischargeAndYear(@Param("discharge") Discharge discharge, @Param("year") int year);
    
    /**
     * Busca una factura por ID y corporación.
     * @param id el ID de la factura
     * @param corporationId el ID de la corporación
     * @return la factura si existe y pertenece a la corporación
     */
    @Query("SELECT i FROM Invoice i WHERE i.id = :id AND i.corporation.id = :corporationId")
    Optional<Invoice> findByIdAndCorporationId(@Param("id") Long id, @Param("corporationId") Long corporationId);

    /**
     * Busca la factura activa por descarga.
     * Debe haber una sola factura activa por cada dischargeId.
     * @param dischargeId el ID de la descarga
     * @return la factura activa si existe
     */
    @Query("SELECT i FROM Invoice i WHERE i.discharge.id = :dischargeId AND i.isActive = true")
    Optional<Invoice> findActiveByDischargeId(@Param("dischargeId") Long dischargeId);
}
