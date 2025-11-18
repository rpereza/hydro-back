package com.univercloud.hydro.enums;

/**
 * Enum para la clasificación de calidad del agua basada en el ICA (Índice de Calidad del Agua).
 * Este enum se utiliza tanto en Monitoring como en DischargeMonitoring.
 */
public enum QualityClasification {
    BUENA("Buena"),
    ACEPTABLE("Aceptable"),
    REGULAR("Regular"),
    MALA("Mala"),
    MUY_MALA("Muy Mala");
    
    private final String displayName;
    
    QualityClasification(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

