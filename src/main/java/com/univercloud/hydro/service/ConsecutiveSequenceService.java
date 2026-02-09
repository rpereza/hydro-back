package com.univercloud.hydro.service;

import com.univercloud.hydro.entity.SequenceType;

/**
 * Servicio para la gestión de consecutivos por año y corporación.
 * Proporciona el siguiente número disponible por tipo de secuencia (DISCHARGE, INVOICE, etc.)
 * con control de concurrencia.
 */
public interface ConsecutiveSequenceService {

    /**
     * Obtiene el siguiente consecutivo para la corporación, año y tipo indicados.
     * Si no existe secuencia para ese (corporación, año, tipo), se crea y se devuelve 1.
     * Los consecutivos no se repiten y el método es seguro ante peticiones concurrentes.
     *
     * @param corporationId ID de la corporación
     * @param year          año
     * @param sequenceType  tipo de secuencia (DISCHARGE, INVOICE, etc.)
     * @return el siguiente consecutivo (1, 2, 3, ...)
     */
    int getNextConsecutive(Long corporationId, Integer year, SequenceType sequenceType);
}
