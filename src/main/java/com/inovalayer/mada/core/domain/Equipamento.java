package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade que representa os ativos físicos da fábrica (Robô WAAM, Fonte de Solda).
 * Os dados aqui contidos são cruciais para o cálculo de depreciação temporal (CapEx).
 */
@Getter
@Setter
@Entity
@Table(name = "tb_equipamento")
public class Equipamento extends BaseEntity {

    @Column(name = "nome_equipamento", nullable = false, length = 150)
    private String nomeEquipamento;

    @Column(name = "fabricante", length = 100)
    private String fabricante;

    // Valor de aquisição da máquina para cálculo de depreciação
    @Column(name = "valor_aquisicao", nullable = false, precision = 12, scale = 2)
    private BigDecimal valorAquisicao;

    // Tempo estimado pelo fabricante para a máquina se tornar obsoleta ou inutilizável
    @Column(name = "vida_util_anos", nullable = false)
    private Integer vidaUtilAnos;

    // Quantidade de horas que a fábrica opera por ano (ex: 1 turno = 2000h)
    @Column(name = "horas_trabalho_ano", nullable = false)
    private Integer horasTrabalhoAno;

    // Potência nominal para calcular o consumo elétrico cruzado com o custo do KWh
    @Column(name = "potencia_kva", nullable = false, precision = 6, scale = 2)
    private BigDecimal potenciaKva;
}