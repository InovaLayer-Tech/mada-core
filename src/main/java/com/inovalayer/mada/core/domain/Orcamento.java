package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Criei esta entidade central para gerir os documentos de orçamento da metrologia.
 * Ela estabelece o relacionamento (Foreign Key) com os insumos físicos e
 * encapsula o rigor financeiro exigido para o cálculo de custos em ambiente industrial.
 */
@Entity
@Table(name = "tb_orcamento")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusOrcamento status;

    // Defini o relacionamento como LAZY para otimizar o consumo de memória do servidor.
    // O Hibernate apenas executará o JOIN com a tabela de Arames quando eu invocar explicitamente getArameMetalico().
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arame_metalico_id", nullable = false)
    private ArameMetalico arameMetalico;

    // Métricas físicas recolhidas pelo Front-end / Python
    @Column(name = "tempo_arco_minutos", nullable = false)
    private Double tempoArcoMinutos;

    @Column(name = "massa_estimada_kg", nullable = false)
    private Double massaEstimadaKg;

    // Apliquei precisão financeira de 12 dígitos totais e 2 casas decimais (Padrão Monetário).
    @Column(name = "custo_material", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoMaterial;

    @Column(name = "custo_operacional", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoOperacional;

    @Column(name = "custo_total_final", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalFinal;

    // Registo imutável do momento exato da emissão do documento para efeitos de auditoria.
    @CreationTimestamp
    @Column(name = "data_emissao", nullable = false, updatable = false)
    private LocalDateTime dataEmissao;
}