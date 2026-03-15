package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Fase 1 do Orçamento: Custos Iniciais (IC).
 */
@Entity
@Table(name = "tb_orcamento_ic")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrcamentoIC {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "tempo_preparacao_minutos", nullable = false)
    private Double tempoPreparacaoMinutos;

    @Column(name = "tempo_remocao_minutos", nullable = false)
    private Double tempoRemocaoMinutos;

    @Column(name = "custo_substrato", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoSubstrato;

    @Column(name = "custo_preparacao", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoPreparacao;

    @Column(name = "custo_remocao", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoRemocao;

    @Column(name = "custo_total_ic", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalIC;

    // Snapshot relacional da taxa de mão de obra (garantia de auditoria)
    @Column(name = "taxa_mao_de_obra_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaMaoDeObraSnapshot;
}
