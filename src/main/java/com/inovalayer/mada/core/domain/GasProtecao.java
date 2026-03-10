package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Criei esta entidade para representar o segundo tipo de insumo físico (gás de proteção).
 * Assim como o arame, ela herda de Consumivel, aproveitando a estrutura comercial (nome, preço)
 * e estendendo com propriedades específicas de metrologia de fluidos.
 */
@Entity
@Table(name = "tb_gas_protecao")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class GasProtecao extends Consumivel {

    // Defini o tipo de gás (ex: Argônio puro, Mistura Ar/CO2) para controle de processo de soldagem.
    @Column(name = "tipo_gas", nullable = false, length = 100)
    private String tipoGas;

    // Utilizei Double para a grandeza de vazão volumétrica (litros por minuto).
    // Esta métrica é vital para que o motor de cálculo cruze com o "Tempo de Arco Aberto" e ache o custo final do gás.
    @Column(name = "fluxo_litros_min", nullable = false)
    private Double fluxoLitrosMin;
}