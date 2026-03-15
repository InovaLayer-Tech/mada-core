package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Fase 3 do Orçamento: Custos Adicionais (AC).
 * Serviços opcionais de engenharia, usinagem, etc.
 */
@Entity
@Table(name = "tb_orcamento_ac")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrcamentoAC {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orcamento_id", nullable = false)
    private Orcamento orcamento;

    @Column(name = "descricao_servico", nullable = false, length = 100)
    private String descricaoServico;

    @Column(name = "quantidade_horas", nullable = false)
    private Double quantidadeHoras;

    @Column(name = "taxa_aplicada_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaAplicadaSnapshot;

    @Column(name = "custo_total_ac", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalAC;
}
