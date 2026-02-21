package com.univercloud.hydro.service;

import com.univercloud.hydro.dto.InvoiceResponse;
import com.univercloud.hydro.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de Facturas.
 * Proporciona operaciones CRUD y lógica de negocio para facturas.
 */
public interface InvoiceService {
    
    /**
     * Obtiene una factura por su ID.
     * @param id el ID de la factura
     * @return la factura, si existe
     */
    Optional<Invoice> getInvoiceById(Long id);
    
    /**
     * Obtiene todas las facturas de la corporación del usuario autenticado.
     * @param pageable parámetros de paginación
     * @return página de facturas
     */
    Page<Invoice> getMyCorporationInvoices(Pageable pageable);
    
    /**
     * Obtiene facturas por descarga.
     * @param dischargeId el ID de la descarga
     * @return lista de facturas de la descarga
     */
    List<Invoice> getInvoicesByDischarge(Long dischargeId);
    
    /**
     * Busca una factura por número de factura.
     * @param number el número de factura
     * @return la factura si existe
     */
    Optional<Invoice> getInvoiceByNumber(Integer number);
    
    /**
     * Busca facturas por descarga y número de factura (búsqueda parcial).
     * @param dischargeId el ID de la descarga
     * @param number el número o parte del número de factura a buscar
     * @return lista de facturas de la descarga que coinciden
     */
    List<Invoice> searchInvoicesByDischargeAndNumber(Long dischargeId, Integer number);
    
    
    /**
     * Obtiene facturas creadas en un rango de fechas.
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     * @return lista de facturas creadas en el rango
     */
    List<Invoice> getInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Cuenta facturas por descarga.
     * @param dischargeId el ID de la descarga
     * @return número de facturas de la descarga
     */
    long countInvoicesByDischarge(Long dischargeId);
    
    /**
     * Cuenta el número de facturas de la corporación del usuario autenticado.
     * @return número de facturas
     */
    long countMyCorporationInvoices();
    
    /**
     * Verifica si existe una factura con el número especificado.
     * @param number el número de factura (entre 1 y 999999999)
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumber(int number);
    
    /**
     * Obtiene facturas ordenadas por fecha de creación (más recientes primero).
     * @return lista de facturas ordenadas por fecha de creación descendente
     */
    List<Invoice> getInvoicesOrderByCreatedAtDesc();
    
    /**
     * Obtiene facturas por descarga ordenadas por fecha de creación descendente.
     * @param dischargeId el ID de la descarga
     * @return lista de facturas de la descarga ordenadas por fecha descendente
     */
    List<Invoice> getInvoicesByDischargeOrderByCreatedAtDesc(Long dischargeId);
    
    /**
     * Obtiene facturas por año (basado en la fecha de creación).
     * @param year el año
     * @return lista de facturas del año
     */
    List<Invoice> getInvoicesByYear(int year);
    
    /**
     * Obtiene facturas por descarga y año.
     * @param dischargeId el ID de la descarga
     * @param year el año
     * @return lista de facturas de la descarga y año
     */
    List<Invoice> getInvoicesByDischargeAndYear(Long dischargeId, int year);
    
    /**
     * Obtiene estadísticas de facturas de la corporación del usuario autenticado.
     * @return estadísticas de facturas
     */
    InvoiceStats getMyCorporationInvoiceStats();
    
    /**
     * Clase interna para estadísticas de facturas
     */
    class InvoiceStats {
        private long totalInvoices;
        private BigDecimal totalAmount;
        private BigDecimal averageAmount;
        private BigDecimal minAmount;
        private BigDecimal maxAmount;
        private long invoicesThisYear;
        private long invoicesThisMonth;
        private long dischargesWithInvoices;
        private long dischargesWithoutInvoices;
        
        // Constructors
        public InvoiceStats() {}
        
        // Getters and Setters
        public long getTotalInvoices() { return totalInvoices; }
        public void setTotalInvoices(long totalInvoices) { this.totalInvoices = totalInvoices; }
        
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        
        public BigDecimal getAverageAmount() { return averageAmount; }
        public void setAverageAmount(BigDecimal averageAmount) { this.averageAmount = averageAmount; }
        
        public BigDecimal getMinAmount() { return minAmount; }
        public void setMinAmount(BigDecimal minAmount) { this.minAmount = minAmount; }
        
        public BigDecimal getMaxAmount() { return maxAmount; }
        public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
        
        public long getInvoicesThisYear() { return invoicesThisYear; }
        public void setInvoicesThisYear(long invoicesThisYear) { this.invoicesThisYear = invoicesThisYear; }
        
        public long getInvoicesThisMonth() { return invoicesThisMonth; }
        public void setInvoicesThisMonth(long invoicesThisMonth) { this.invoicesThisMonth = invoicesThisMonth; }
        
        public long getDischargesWithInvoices() { return dischargesWithInvoices; }
        public void setDischargesWithInvoices(long dischargesWithInvoices) { this.dischargesWithInvoices = dischargesWithInvoices; }
        
        public long getDischargesWithoutInvoices() { return dischargesWithoutInvoices; }
        public void setDischargesWithoutInvoices(long dischargesWithoutInvoices) { this.dischargesWithoutInvoices = dischargesWithoutInvoices; }
    }
    
    /**
     * Obtiene las facturas activas de la corporación del usuario autenticado para un año dado,
     * con filtro opcional por dischargeUserId y soporte de paginación.
     * @param year el año de las facturas
     * @param dischargeUserId ID del dischargeUser (opcional)
     * @param pageable parámetros de paginación
     * @return página de InvoiceResponse
     */
    Page<InvoiceResponse> getActiveInvoicesByYear(int year, Long dischargeUserId, Pageable pageable);

    /**
     * Genera una factura a partir de una descarga.
     * Calcula todos los atributos de la factura basándose en los datos de la descarga,
     * tarifa mínima y progreso del proyecto (si aplica).
     * @param dischargeId el ID de la descarga
     * @return la factura generada
     * @throws ResourceNotFoundException si la descarga no existe o no pertenece a la corporación
     * @throws IllegalArgumentException si faltan datos requeridos (tarifa mínima, monitoreos, etc.)
     */
    Invoice generateInvoiceFromDischarge(Long dischargeId);
}
