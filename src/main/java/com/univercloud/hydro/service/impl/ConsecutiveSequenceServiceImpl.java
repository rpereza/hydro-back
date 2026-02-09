package com.univercloud.hydro.service.impl;

import com.univercloud.hydro.entity.ConsecutiveSequence;
import com.univercloud.hydro.entity.Corporation;
import com.univercloud.hydro.entity.SequenceType;
import com.univercloud.hydro.repository.ConsecutiveSequenceRepository;
import com.univercloud.hydro.repository.CorporationRepository;
import com.univercloud.hydro.service.ConsecutiveSequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementación del servicio de consecutivos por año y corporación.
 * Controla concurrencia mediante bloqueo pesimista y retry ante violación de unicidad.
 */
@Service
@Transactional
public class ConsecutiveSequenceServiceImpl implements ConsecutiveSequenceService {

    @Autowired
    private ConsecutiveSequenceRepository consecutiveSequenceRepository;

    @Autowired
    private CorporationRepository corporationRepository;

    @Override
    @Transactional
    public int getNextConsecutive(Long corporationId, Integer year, SequenceType sequenceType) {
        Optional<ConsecutiveSequence> existing = consecutiveSequenceRepository
                .findByCorporationIdAndYearAndSequenceType(corporationId, year, sequenceType);

        if (existing.isPresent()) {
            ConsecutiveSequence seq = existing.get();
            int value = seq.getNextValue();
            seq.setNextValue(value + 1);
            consecutiveSequenceRepository.save(seq);
            return value;
        }

        try {
            Corporation corporation = corporationRepository.findById(corporationId)
                    .orElseThrow(() -> new IllegalArgumentException("Corporation not found: " + corporationId));
            ConsecutiveSequence newSeq = new ConsecutiveSequence(corporation, year, sequenceType, 2);
            consecutiveSequenceRepository.save(newSeq);
            return 1;
        } catch (DataIntegrityViolationException e) {
            // Otro hilo insertó la misma terna; reintentar find con lock e incrementar
            Optional<ConsecutiveSequence> retry = consecutiveSequenceRepository
                    .findByCorporationIdAndYearAndSequenceType(corporationId, year, sequenceType);
            if (retry.isPresent()) {
                ConsecutiveSequence seq = retry.get();
                int value = seq.getNextValue();
                seq.setNextValue(value + 1);
                consecutiveSequenceRepository.save(seq);
                return value;
            }
            throw new IllegalStateException("Concurrent insert conflict for consecutive sequence", e);
        }
    }
}
