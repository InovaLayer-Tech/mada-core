package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_gas_protecao")
@Data
@EqualsAndHashCode(callSuper = true)
public class GasProtecao extends Consumivel {

    @Column(name = "tipo_gas", nullable = false, length = 50)
    private String tipoGas;

    @Column(nullable = false)
    private Boolean ativo = true;

    // Novo campo essencial para a conversão de consumo na Fase DC
    @Column(name = "vazao_padrao", nullable = false)
    private Double vazaoPadrao; // Representado em L/min (Litros por minuto)
}