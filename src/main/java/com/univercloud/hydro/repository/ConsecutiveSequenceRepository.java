package com.univercloud.hydro.repository;

import com.univercloud.hydro.entity.ConsecutiveSequence;
import com.univercloud.hydro.entity.SequenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

/**
 * Repository para la entidad ConsecutiveSequence.
 * Proporciona acceso a secuencias de consecutivos por corporación, año y tipo.
 */
@Repository
public interface ConsecutiveSequenceRepository extends JpaRepository<ConsecutiveSequence, Long> {

    /**
     * Busca la secuencia por corporación, año y tipo con bloqueo pesimista para actualización.
     * Evita que dos transacciones obtengan el mismo consecutivo en peticiones concurrentes.
     *
     * @param corporationId ID de la corporación
     * @param year          año
     * @param sequenceType  tipo de secuencia (DISCHARGE, INVOICE, etc.)
     * @return la secuencia si existe
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cs FROM ConsecutiveSequence cs WHERE cs.corporation.id = :corporationId AND cs.year = :year AND cs.sequenceType = :sequenceType")
    Optional<ConsecutiveSequence> findByCorporationIdAndYearAndSequenceType(
            @Param("corporationId") Long corporationId,
            @Param("year") Integer year,
            @Param("sequenceType") SequenceType sequenceType);
}
