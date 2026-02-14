package com.univercloud.hydro.controller;

import com.univercloud.hydro.entity.Invoice;
import com.univercloud.hydro.exception.ResourceNotFoundException;
import com.univercloud.hydro.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
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
     * Crea una nueva factura.
     * 
     * @param invoice la factura a crear
     * @return la factura creada
     */
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Invoice> createInvoice(@Valid @RequestBody Invoice invoice) {
        try {
            Invoice createdInvoice = invoiceService.createInvoice(invoice);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInvoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    /**
     * Actualiza una factura existente.
     * 
     * @param id el ID de la factura
     * @param invoice la factura actualizada
     * @return la factura actualizada
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable Long id, @Valid @RequestBody Invoice invoice) {
        try {
            invoice.setId(id);
            Invoice updatedInvoice = invoiceService.updateInvoice(invoice);
            return ResponseEntity.ok(updatedInvoice);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
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
     * Obtiene todas las facturas de la corporación.
     * 
     * @return lista de facturas
     */
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try {
            List<Invoice> invoices = invoiceService.getAllMyCorporationInvoices();
            return ResponseEntity.ok(invoices);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
    
    
    /**
     * Elimina una factura.
     * 
     * @param id el ID de la factura a eliminar
     * @return true si se eliminó correctamente
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Boolean> deleteInvoice(@PathVariable Long id) {
        try {
            boolean deleted = invoiceService.deleteInvoice(id);
            return ResponseEntity.ok(deleted);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
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
    public ResponseEntity<?> generateInvoiceFromDischarge(@PathVariable Long dischargeId) {
        try {
            Invoice invoice = invoiceService.generateInvoiceFromDischarge(dischargeId);
            return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage() != null ? e.getMessage() : "Error interno al generar la factura");
        }
    }
    
}
