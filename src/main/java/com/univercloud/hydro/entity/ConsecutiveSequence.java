package com.univercloud.hydro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Entidad para el manejo de consecutivos por año y corporación.
 * Una fila por (corporación, año, tipo) guarda el próximo valor a asignar.
 * Permite secuencias independientes por tipo (p. ej. DISCHARGE, INVOICE).
 */
@Entity
@Table(name = "consecutive_sequences",
       uniqueConstraints = { @UniqueConstraint(columnNames = {"corporation_id", "year", "sequence_type"}) })
public class ConsecutiveSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corporation_id", nullable = false)
    private Corporation corporation;

    @NotNull
    @Column(name = "year", nullable = false)
    private Integer year;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sequence_type", nullable = false, length = 50)
    private SequenceType sequenceType;

    @NotNull
    @Column(name = "next_value", nullable = false)
    private Integer nextValue;

    public ConsecutiveSequence() {
    }

    public ConsecutiveSequence(Corporation corporation, Integer year, SequenceType sequenceType, Integer nextValue) {
        this.corporation = corporation;
        this.year = year;
        this.sequenceType = sequenceType;
        this.nextValue = nextValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Corporation getCorporation() {
        return corporation;
    }

    public void setCorporation(Corporation corporation) {
        this.corporation = corporation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SequenceType getSequenceType() {
        return sequenceType;
    }

    public void setSequenceType(SequenceType sequenceType) {
        this.sequenceType = sequenceType;
    }

    public Integer getNextValue() {
        return nextValue;
    }

    public void setNextValue(Integer nextValue) {
        this.nextValue = nextValue;
    }
}
