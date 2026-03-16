package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Fase 2 do Orçamento: Custos de Deposição (DC).
 * Reúne métricas físicas do arame/gás e snapshots de custos base.
 */
@Entity
@Table(name = "tb_orcamento_dc")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrcamentoDC {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arame_metalico_id", nullable = false)
    private ArameMetalico arameMetalico;

    // Caso o modelo passe a contemplar o Gás explicitamente:
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "gas_protecao_id")
    // private GasProtecao gasProtecao;

    @Column(name = "tempo_arco_minutos", nullable = false)
    private Double tempoArcoMinutos;

    @Column(name = "massa_estimada_kg", nullable = false)
    private Double massaEstimadaKg;

    @Column(name = "custo_material", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoMaterial;

    @Column(name = "custo_gas", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoGas;

    @Column(name = "custo_energia", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoEnergia;

    @Column(name = "custo_maquina", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoMaquina;

    @Column(name = "custo_total_dc", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalDC;

    // Snapshots relacionais de propriedades físicas e financeiras (Auditoria)
    @Column(name = "arame_preco_kg_snapshot", nullable = false, precision = 12, scale = 2)
    private BigDecimal aramePrecoKgSnapshot;

    @Column(name = "arame_densidade_snapshot", nullable = false)
    private Double arameDensidadeSnapshot;

    @Column(name = "arame_eficiencia_snapshot", nullable = false)
    private Double arameEficienciaSnapshot;

    @Column(name = "gas_preco_m3_snapshot", nullable = false, precision = 12, scale = 2)
    private BigDecimal gasPrecoM3Snapshot;

    @Column(name = "gas_vazao_snapshot", nullable = false)
    private Double gasVazaoSnapshot;

    @Column(name = "taxa_energia_snapshot", nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaEnergiaSnapshot;

    @Column(name = "taxa_maquina_snapshot", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaMaquinaSnapshot;
}
