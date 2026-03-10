package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade filha (Especialização) de Consumível, focada nos parâmetros atmosféricos.
 * Localização: src/main/java/com/inovalayer/mada/core/domain/
 */
@Getter
@Setter
@Entity
@Table(name = "tb_gas_protecao")
public class GasProtecao extends Consumivel {

    @Column(name = "mistura_quimica", nullable = false, length = 100) // Ex: 100% Argônio, 80% Ar 20% CO2
    private String misturaQuimica;

    // A vazão recomendada. Se o processo durar 60 min e a vazão for 15L/min, 
    // saberemos que gastamos 900 Litros de gás.
    @Column(name = "fluxo_litro_minuto_padrao", nullable = false, precision = 5, scale = 2)
    private BigDecimal fluxoLitroMinutoPadrao;
}