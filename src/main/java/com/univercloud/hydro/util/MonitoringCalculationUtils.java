package com.univercloud.hydro.util;

import com.univercloud.hydro.enums.QualityClasification;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Utilidad para calcular índices y coeficientes ICA para monitoreos.
 * Esta clase contiene métodos estáticos reutilizables para calcular:
 * - Índices individuales (iod, isst, idqo, ice, iph, irnp)
 * - RNP (relación N/P)
 * - Número de variables ICA
 * - Coeficiente ICA
 * - Clasificación de calidad
 */
public class MonitoringCalculationUtils {
    
    private static final MathContext DEFAULT_MATH_CONTEXT = new MathContext(11, RoundingMode.HALF_UP);
    private static final MathContext ICA_COEFFICIENT_MATH_CONTEXT = new MathContext(3, RoundingMode.HALF_UP);
    
    /**
     * Interfaz para objetos que tienen los campos necesarios para cálculos ICA.
     * Permite trabajar con Monitoring y DischargeMonitoring de forma genérica.
     */
    public interface IcaCalculable {
        BigDecimal getOd();
        BigDecimal getSst();
        BigDecimal getDqo();
        BigDecimal getCe();
        BigDecimal getPh();
        BigDecimal getN();
        BigDecimal getP();
        BigDecimal getIod();
        BigDecimal getIsst();
        BigDecimal getIdqo();
        BigDecimal getIce();
        BigDecimal getIph();
        BigDecimal getIrnp();
        Integer getNumberIcaVariables();
        BigDecimal getIcaCoefficient();
        void setIod(BigDecimal iod);
        void setIsst(BigDecimal isst);
        void setIdqo(BigDecimal idqo);
        void setIce(BigDecimal ice);
        void setIph(BigDecimal iph);
        void setRnp(BigDecimal rnp);
        void setIrnp(BigDecimal irnp);
        void setNumberIcaVariables(Integer numberIcaVariables);
        void setIcaCoefficient(BigDecimal icaCoefficient);
        void setQualityClasification(QualityClasification qualityClasification);
    }
    
    /**
     * Calcula todos los índices y valores derivados para un objeto IcaCalculable.
     * 
     * @param calculable el objeto para el cual calcular los valores
     */
    public static void calculateAllValues(IcaCalculable calculable) {
        if (calculable == null) {
            return;
        }
        
        // Calcular iod basado en el valor de od
        if (calculable.getOd() != null) {
            calculable.setIod(calculateIod(calculable.getOd()));
        }
        
        // Calcular isst basado en el valor de sst
        if (calculable.getSst() != null) {
            calculable.setIsst(calculateIsst(calculable.getSst()));
        }
        
        // Calcular idqo basado en el valor de dqo
        if (calculable.getDqo() != null) {
            calculable.setIdqo(calculateIdqo(calculable.getDqo()));
        }
        
        // Calcular ice basado en el valor de ce
        if (calculable.getCe() != null) {
            calculable.setIce(calculateIce(calculable.getCe()));
        }
        
        // Calcular iph basado en el valor de ph
        if (calculable.getPh() != null) {
            calculable.setIph(calculateIph(calculable.getPh()));
        }
        
        // Calcular rnp e irnp basados en los valores de n y p
        calculateRnpAndIrnp(calculable);
        
        // Calcular numberIcaVariables basado en los índices calculados
        calculable.setNumberIcaVariables(calculateNumberIcaVariables(calculable));
        
        // Calcular icaCoefficient basado en numberIcaVariables
        calculable.setIcaCoefficient(calculateIcaCoefficient(calculable));
        
        // Calcular qualityClasification basado en icaCoefficient
        calculable.setQualityClasification(calculateQualityClasification(calculable.getIcaCoefficient()));
    }
    
    /**
     * Calcula el valor de IOD basado en el valor de OD.
     * 
     * @param od Valor de OD (Oxígeno Disuelto)
     * @return Valor calculado de IOD
     */
    public static BigDecimal calculateIod(BigDecimal od) {
        if (od == null) {
            return null;
        }
        
        // Constantes para el cálculo
        BigDecimal one = BigDecimal.ONE;
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal pointZeroOne = new BigDecimal("0.01");
        
        // Calcular 0.01 * od
        BigDecimal pointZeroOneTimesOd = pointZeroOne.multiply(od, DEFAULT_MATH_CONTEXT);
        
        // Comparar od con 100
        if (od.compareTo(hundred) > 0) {
            // Si od > 100: iod = 1 − (0,01 * od − 1)
            BigDecimal innerExpression = pointZeroOneTimesOd.subtract(one, DEFAULT_MATH_CONTEXT);
            return one.subtract(innerExpression, DEFAULT_MATH_CONTEXT);
        } else {
            // Si od <= 100: iod = 1 − (1 − 0,01 * od)
            BigDecimal innerExpression = one.subtract(pointZeroOneTimesOd, DEFAULT_MATH_CONTEXT);
            return one.subtract(innerExpression, DEFAULT_MATH_CONTEXT);
        }
    }
    
    /**
     * Calcula el valor de ISST basado en el valor de SST.
     * 
     * @param sst Valor de SST (Sólidos Suspendidos Totales)
     * @return Valor calculado de ISST
     */
    public static BigDecimal calculateIsst(BigDecimal sst) {
        if (sst == null) {
            return null;
        }
        
        // Constantes para el cálculo
        BigDecimal one = BigDecimal.ONE;
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal fourPointFive = new BigDecimal("4.5");
        BigDecimal threeHundredTwenty = new BigDecimal("320");
        BigDecimal zeroPointZeroZeroThree = new BigDecimal("0.003");
        
        // Si sst <= 4.5: isst = 1
        if (sst.compareTo(fourPointFive) <= 0) {
            return one;
        }
        
        // Si sst >= 320: isst = 0
        if (sst.compareTo(threeHundredTwenty) >= 0) {
            return zero;
        }
        
        // Si 4.5 < sst < 320: isst = 1 − (−0,02 + 0,003 * sst)
        // Simplificado: isst = 1 + 0.02 - 0.003 * sst = 1.02 - 0.003 * sst
        BigDecimal onePointZeroTwo = new BigDecimal("1.02");
        BigDecimal zeroPointZeroZeroThreeTimesSst = zeroPointZeroZeroThree.multiply(sst, DEFAULT_MATH_CONTEXT);
        return onePointZeroTwo.subtract(zeroPointZeroZeroThreeTimesSst, DEFAULT_MATH_CONTEXT);
    }
    
    /**
     * Calcula el valor de IDQO basado en el valor de DQO según rangos específicos.
     * 
     * @param dqo Valor de DQO (Demanda Química de Oxígeno)
     * @return Valor calculado de IDQO
     */
    public static BigDecimal calculateIdqo(BigDecimal dqo) {
        if (dqo == null) {
            return null;
        }
        
        // Constantes para los rangos y valores
        BigDecimal twenty = new BigDecimal("20");
        BigDecimal twentyFive = new BigDecimal("25");
        BigDecimal forty = new BigDecimal("40");
        BigDecimal eighty = new BigDecimal("80");
        
        BigDecimal value091 = new BigDecimal("0.91");
        BigDecimal value071 = new BigDecimal("0.71");
        BigDecimal value051 = new BigDecimal("0.51");
        BigDecimal value026 = new BigDecimal("0.26");
        BigDecimal value0125 = new BigDecimal("0.125");
        
        // Si dqo <= 20: idqo = 0.91
        if (dqo.compareTo(twenty) <= 0) {
            return value091;
        }
        
        // Si 20 < dqo <= 25: idqo = 0.71
        if (dqo.compareTo(twenty) > 0 && dqo.compareTo(twentyFive) <= 0) {
            return value071;
        }
        
        // Si 25 < dqo <= 40: idqo = 0.51
        if (dqo.compareTo(twentyFive) > 0 && dqo.compareTo(forty) <= 0) {
            return value051;
        }
        
        // Si 40 < dqo <= 80: idqo = 0.26
        if (dqo.compareTo(forty) > 0 && dqo.compareTo(eighty) <= 0) {
            return value026;
        }
        
        // Si dqo > 80: idqo = 0.125
        return value0125;
    }
    
    /**
     * Calcula el valor de ICE basado en el valor de CE.
     * Fórmula: ice = 1 - 10^(-3.26 + 1.34 * log10(ce))
     * Si el resultado es < 0, se ajusta a 0.
     * 
     * @param ce Valor de CE (Conductividad Eléctrica)
     * @return Valor calculado de ICE
     */
    public static BigDecimal calculateIce(BigDecimal ce) {
        if (ce == null) {
            return null;
        }
        
        // Convertir BigDecimal a double para usar funciones matemáticas
        double ceValue = ce.doubleValue();
        
        // Validar que ce sea positivo para el logaritmo
        if (ceValue <= 0) {
            throw new IllegalArgumentException("CE value must be greater than 0 for logarithm calculation");
        }
        
        // Calcular log10(ce)
        double log10Ce = Math.log10(ceValue);
        
        // Calcular -3.26 + 1.34 * log10(ce)
        double exponent = -3.26 + 1.34 * log10Ce;
        
        // Calcular 10^exponent
        double tenToExponent = Math.pow(10, exponent);
        
        // Calcular 1 - 10^exponent
        double iceValue = 1.0 - tenToExponent;
        
        // Si el resultado es < 0, ajustar a 0
        if (iceValue < 0) {
            iceValue = 0.0;
        }
        
        // Convertir de vuelta a BigDecimal con precisión adecuada
        return new BigDecimal(iceValue, DEFAULT_MATH_CONTEXT);
    }
    
    /**
     * Calcula el valor de IPH basado en el valor de PH según rangos específicos.
     * 
     * @param ph Valor de PH
     * @return Valor calculado de IPH
     */
    public static BigDecimal calculateIph(BigDecimal ph) {
        if (ph == null) {
            return null;
        }
        
        // Convertir BigDecimal a double para usar funciones matemáticas
        double phValue = ph.doubleValue();
        
        // Constantes para los rangos y valores
        BigDecimal four = new BigDecimal("4");
        BigDecimal seven = new BigDecimal("7");
        BigDecimal eight = new BigDecimal("8");
        BigDecimal eleven = new BigDecimal("11");
        BigDecimal zeroPointOne = new BigDecimal("0.1");
        BigDecimal one = BigDecimal.ONE;
        
        // Si ph < 4: iph = 0.1
        if (ph.compareTo(four) < 0) {
            return zeroPointOne;
        }
        
        // Si 4 <= ph <= 7: iph = 0.02628419 * e^(ph * 0.520025)
        if (ph.compareTo(four) >= 0 && ph.compareTo(seven) <= 0) {
            double exponent = phValue * 0.520025;
            double eToExponent = Math.exp(exponent);
            double iphValue = 0.02628419 * eToExponent;
            return new BigDecimal(iphValue, DEFAULT_MATH_CONTEXT);
        }
        
        // Si 7 < ph <= 8: iph = 1
        if (ph.compareTo(seven) > 0 && ph.compareTo(eight) <= 0) {
            return one;
        }
        
        // Si 8 < ph <= 11: iph = 1 * e^((ph - 8) * -0.5187742)
        if (ph.compareTo(eight) > 0 && ph.compareTo(eleven) <= 0) {
            double exponent = (phValue - 8.0) * -0.5187742;
            double eToExponent = Math.exp(exponent);
            double iphValue = 1.0 * eToExponent;
            return new BigDecimal(iphValue, DEFAULT_MATH_CONTEXT);
        }
        
        // Si ph > 11: iph = 0.1
        return zeroPointOne;
    }
    
    /**
     * Calcula RNP (división de N y P) e IRNP basado en RNP.
     * Si tanto N como P son nulos, RNP e IRNP se setean a nulo.
     * 
     * @param calculable Objeto IcaCalculable al que se le calcularán rnp e irnp
     */
    public static void calculateRnpAndIrnp(IcaCalculable calculable) {
        if (calculable == null) {
            return;
        }
        
        BigDecimal n = calculable.getN();
        BigDecimal p = calculable.getP();
        
        // Si tanto n como p son nulos, setear rnp e irnp a nulo
        if (n == null && p == null) {
            calculable.setRnp(null);
            calculable.setIrnp(null);
            return;
        }
        
        // Si alguno es nulo, no se puede calcular rnp
        if (n == null || p == null) {
            calculable.setRnp(null);
            calculable.setIrnp(null);
            return;
        }
        
        // Validar que p no sea cero para evitar división por cero
        if (p.compareTo(BigDecimal.ZERO) == 0) {
            calculable.setRnp(null);
            calculable.setIrnp(null);
            return;
        }
        
        // Calcular rnp = n / p
        BigDecimal rnp = n.divide(p, DEFAULT_MATH_CONTEXT);
        calculable.setRnp(rnp);
        
        // Calcular irnp basado en rnp
        calculable.setIrnp(calculateIrnp(rnp));
    }
    
    /**
     * Calcula el valor de IRNP basado en el valor de RNP según rangos específicos.
     * 
     * @param rnp Valor de RNP (Relación N/P)
     * @return Valor calculado de IRNP
     */
    public static BigDecimal calculateIrnp(BigDecimal rnp) {
        if (rnp == null) {
            return null;
        }
        
        // Constantes para los rangos y valores
        BigDecimal five = new BigDecimal("5");
        BigDecimal ten = new BigDecimal("10");
        BigDecimal fifteen = new BigDecimal("15");
        BigDecimal twenty = new BigDecimal("20");
        
        BigDecimal value035 = new BigDecimal("0.35");
        BigDecimal value06 = new BigDecimal("0.6");
        BigDecimal value08 = new BigDecimal("0.8");
        BigDecimal value015 = new BigDecimal("0.15");
        
        // Si rnp > 5 y <= 10: irnp = 0.35
        if (rnp.compareTo(five) > 0 && rnp.compareTo(ten) <= 0) {
            return value035;
        }
        
        // Si rnp > 10 y < 15: irnp = 0.6
        if (rnp.compareTo(ten) > 0 && rnp.compareTo(fifteen) < 0) {
            return value06;
        }
        
        // Si rnp >= 15 y <= 20: irnp = 0.8
        if (rnp.compareTo(fifteen) >= 0 && rnp.compareTo(twenty) <= 0) {
            return value08;
        }
        
        // Si rnp <= 5 o > 20: irnp = 0.15
        return value015;
    }
    
    /**
     * Calcula el número de variables ICA que tienen valor (no son nulas).
     * Evalúa los siguientes atributos: iod, isst, idqo, ice, iph, irnp
     * 
     * @param calculable Objeto IcaCalculable del cual se calculará el número de variables ICA
     * @return Número de variables ICA que tienen valor (0-6)
     */
    public static Integer calculateNumberIcaVariables(IcaCalculable calculable) {
        if (calculable == null) {
            return 0;
        }
        
        int count = 0;
        
        // Contar cuántos atributos son diferentes de null
        if (calculable.getIod() != null) {
            count++;
        }
        if (calculable.getIsst() != null) {
            count++;
        }
        if (calculable.getIdqo() != null) {
            count++;
        }
        if (calculable.getIce() != null) {
            count++;
        }
        if (calculable.getIph() != null) {
            count++;
        }
        if (calculable.getIrnp() != null) {
            count++;
        }
        
        return count;
    }
    
    /**
     * Calcula el coeficiente ICA basado en el número de variables ICA y los índices correspondientes.
     * 
     * @param calculable Objeto IcaCalculable del cual se calculará el coeficiente ICA
     * @return Valor calculado del coeficiente ICA
     * @throws IllegalArgumentException si numberIcaVariables no es 5 ni 6
     */
    public static BigDecimal calculateIcaCoefficient(IcaCalculable calculable) {
        if (calculable == null) {
            return null;
        }
        
        Integer numberIcaVariables = calculable.getNumberIcaVariables();
        
        if (numberIcaVariables == null) {
            return null;
        }
        
        if (numberIcaVariables == 5) {
            // Fórmula: (iod*0.2)+(isst*0.2)+(idqo*0.2)+(ice*0.2)+(iph*0.2)
            BigDecimal weight = new BigDecimal("0.2");
            BigDecimal result = BigDecimal.ZERO;
            
            if (calculable.getIod() != null) {
                result = result.add(calculable.getIod().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIsst() != null) {
                result = result.add(calculable.getIsst().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIdqo() != null) {
                result = result.add(calculable.getIdqo().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIce() != null) {
                result = result.add(calculable.getIce().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIph() != null) {
                result = result.add(calculable.getIph().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            
            return result;
        } else if (numberIcaVariables == 6) {
            // Fórmula: (iod*0.17)+(isst*0.17)+(idqo*0.17)+(ice*0.17)+(iph*0.15)+(irnp*0.17)
            BigDecimal weight017 = new BigDecimal("0.17");
            BigDecimal weight015 = new BigDecimal("0.15");
            BigDecimal result = BigDecimal.ZERO;
            
            if (calculable.getIod() != null) {
                result = result.add(calculable.getIod().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIsst() != null) {
                result = result.add(calculable.getIsst().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIdqo() != null) {
                result = result.add(calculable.getIdqo().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIce() != null) {
                result = result.add(calculable.getIce().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIph() != null) {
                result = result.add(calculable.getIph().multiply(weight015, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (calculable.getIrnp() != null) {
                result = result.add(calculable.getIrnp().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            
            return result;
        } else {
            throw new IllegalArgumentException("numberIcaVariables must be 5 or 6, but was: " + numberIcaVariables);
        }
    }
    
    /**
     * Calcula la clasificación de calidad basada en el coeficiente ICA.
     * 
     * @param icaCoefficient Valor del coeficiente ICA
     * @return Clasificación de calidad correspondiente
     * @throws IllegalArgumentException si el icaCoefficient está fuera del rango válido (0-1)
     */
    public static QualityClasification calculateQualityClasification(BigDecimal icaCoefficient) {
        if (icaCoefficient == null) {
            return null;
        }
        
        // Constantes para los rangos
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal zeroPoint25 = new BigDecimal("0.25");
        BigDecimal zeroPoint5 = new BigDecimal("0.5");
        BigDecimal zeroPoint7 = new BigDecimal("0.7");
        BigDecimal zeroPoint9 = new BigDecimal("0.9");
        BigDecimal one = BigDecimal.ONE;
        
        // Si icaCoefficient >= 0 y <= 0.25: MUY_MALA
        if (icaCoefficient.compareTo(zero) >= 0 && icaCoefficient.compareTo(zeroPoint25) <= 0) {
            return QualityClasification.MUY_MALA;
        }
        
        // Si icaCoefficient > 0.25 y <= 0.5: MALA
        if (icaCoefficient.compareTo(zeroPoint25) > 0 && icaCoefficient.compareTo(zeroPoint5) <= 0) {
            return QualityClasification.MALA;
        }
        
        // Si icaCoefficient > 0.5 y <= 0.7: REGULAR
        if (icaCoefficient.compareTo(zeroPoint5) > 0 && icaCoefficient.compareTo(zeroPoint7) <= 0) {
            return QualityClasification.REGULAR;
        }
        
        // Si icaCoefficient > 0.7 y <= 0.9: ACEPTABLE
        if (icaCoefficient.compareTo(zeroPoint7) > 0 && icaCoefficient.compareTo(zeroPoint9) <= 0) {
            return QualityClasification.ACEPTABLE;
        }
        
        // Si icaCoefficient > 0.9 y <= 1: BUENA
        if (icaCoefficient.compareTo(zeroPoint9) > 0 && icaCoefficient.compareTo(one) <= 0) {
            return QualityClasification.BUENA;
        }
        
        // Para otros valores: lanzar IllegalArgumentException
        throw new IllegalArgumentException("icaCoefficient must be between 0 and 1, but was: " + icaCoefficient);
    }
    
    /**
     * Calcula todos los valores para un objeto DischargeMonitoring.
     * Método de conveniencia que adapta DischargeMonitoring a IcaCalculable.
     * 
     * @param monitoring el DischargeMonitoring para el cual calcular los valores
     */
    public static void calculateDischargeMonitoringValues(com.univercloud.hydro.entity.DischargeMonitoring monitoring) {
        if (monitoring == null) {
            return;
        }
        
        // Calcular iod basado en el valor de od
        if (monitoring.getOd() != null) {
            monitoring.setIod(calculateIod(monitoring.getOd()));
        }
        
        // Calcular isst basado en el valor de sst
        if (monitoring.getSst() != null) {
            monitoring.setIsst(calculateIsst(monitoring.getSst()));
        }
        
        // Calcular idqo basado en el valor de dqo
        if (monitoring.getDqo() != null) {
            monitoring.setIdqo(calculateIdqo(monitoring.getDqo()));
        }
        
        // Calcular ice basado en el valor de ce
        if (monitoring.getCe() != null) {
            monitoring.setIce(calculateIce(monitoring.getCe()));
        }
        
        // Calcular iph basado en el valor de ph
        if (monitoring.getPh() != null) {
            monitoring.setIph(calculateIph(monitoring.getPh()));
        }
        
        // Calcular rnp e irnp basados en los valores de n y p
        BigDecimal n = monitoring.getN();
        BigDecimal p = monitoring.getP();
        
        if (n == null && p == null) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
        } else if (n == null || p == null) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
        } else if (p.compareTo(BigDecimal.ZERO) == 0) {
            monitoring.setRnp(null);
            monitoring.setIrnp(null);
        } else {
            BigDecimal rnp = n.divide(p, DEFAULT_MATH_CONTEXT);
            monitoring.setRnp(rnp);
            monitoring.setIrnp(calculateIrnp(rnp));
        }
        
        // Calcular numberIcaVariables basado en los índices calculados
        int count = 0;
        if (monitoring.getIod() != null) count++;
        if (monitoring.getIsst() != null) count++;
        if (monitoring.getIdqo() != null) count++;
        if (monitoring.getIce() != null) count++;
        if (monitoring.getIph() != null) count++;
        if (monitoring.getIrnp() != null) count++;
        monitoring.setNumberIcaVariables(count);
        
        // Calcular icaCoefficient basado en numberIcaVariables
        monitoring.setIcaCoefficient(calculateIcaCoefficientForDischargeMonitoring(monitoring));
        
        // Calcular qualityClasification basado en icaCoefficient
        monitoring.setQualityClasification(calculateQualityClasification(monitoring.getIcaCoefficient()));
    }
    
    /**
     * Calcula el coeficiente ICA para un DischargeMonitoring.
     * 
     * @param monitoring el DischargeMonitoring
     * @return el coeficiente ICA calculado
     */
    private static BigDecimal calculateIcaCoefficientForDischargeMonitoring(com.univercloud.hydro.entity.DischargeMonitoring monitoring) {
        Integer numberIcaVariables = monitoring.getNumberIcaVariables();
        
        if (numberIcaVariables == null) {
            return null;
        }
        
        if (numberIcaVariables == 5) {
            BigDecimal weight = new BigDecimal("0.2");
            BigDecimal result = BigDecimal.ZERO;
            
            if (monitoring.getIod() != null) {
                result = result.add(monitoring.getIod().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIsst() != null) {
                result = result.add(monitoring.getIsst().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIdqo() != null) {
                result = result.add(monitoring.getIdqo().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIce() != null) {
                result = result.add(monitoring.getIce().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIph() != null) {
                result = result.add(monitoring.getIph().multiply(weight, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            
            return result;
        } else if (numberIcaVariables == 6) {
            BigDecimal weight017 = new BigDecimal("0.17");
            BigDecimal weight015 = new BigDecimal("0.15");
            BigDecimal result = BigDecimal.ZERO;
            
            if (monitoring.getIod() != null) {
                result = result.add(monitoring.getIod().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIsst() != null) {
                result = result.add(monitoring.getIsst().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIdqo() != null) {
                result = result.add(monitoring.getIdqo().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIce() != null) {
                result = result.add(monitoring.getIce().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIph() != null) {
                result = result.add(monitoring.getIph().multiply(weight015, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            if (monitoring.getIrnp() != null) {
                result = result.add(monitoring.getIrnp().multiply(weight017, ICA_COEFFICIENT_MATH_CONTEXT), ICA_COEFFICIENT_MATH_CONTEXT);
            }
            
            return result;
        } else {
            throw new IllegalArgumentException("numberIcaVariables must be 5 or 6, but was: " + numberIcaVariables);
        }
    }
}

