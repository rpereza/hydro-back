package com.univercloud.hydro.controller;

import com.univercloud.hydro.dto.InvoiceResponse;
import com.univercloud.hydro.entity.Invoice;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


/**
 * Controlador REST para la gestión de Facturas.
 * Proporciona endpoints para operaciones CRUD y consultas de facturas.
 */
@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {
    
    @Autowired
    private InvoiceService invoiceService;
    
    /**
     * Obtiene una factura por su ID.
     * 
     * @param id el ID de la factura
     * @return la factura si existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Optional<Invoice> invoice = invoiceService.getInvoiceById(id);
        return invoice.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtiene las facturas activas de la corporación para un año dado,
     * con filtro opcional por dischargeUserId y soporte de paginación.
     *
     * @param year el año de las facturas
     * @param dischargeUserId ID del usuario de descarga (opcional)
     * @param pageable parámetros de paginación (page, size, sort)
     * @return página de facturas activas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<InvoiceResponse>> getActiveInvoicesByYear(
            @RequestParam int year,
            @RequestParam(required = false) Long dischargeUserId,
            Pageable pageable) {
        try {
            Page<InvoiceResponse> invoices = invoiceService.getActiveInvoicesByYear(year, dischargeUserId, pageable);
            return ResponseEntity.ok(invoices);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    /**
     * Genera una factura a partir de una descarga.
     * Calcula todos los atributos de la factura basándose en los datos de la descarga,
     * tarifa mínima y progreso del proyecto (si aplica).
     * 
     * @param dischargeId el ID de la descarga
     * @return respuesta HTTP con la factura generada
     */
    @PostMapping("/generate-from-discharge/{dischargeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<InvoiceResponse> generateInvoiceFromDischarge(@PathVariable Long dischargeId) {
        try {
            InvoiceResponse invoice = invoiceService.generateInvoiceFromDischarge(dischargeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
