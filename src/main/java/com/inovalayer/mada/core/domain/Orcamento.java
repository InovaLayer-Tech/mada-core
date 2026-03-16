package com.inovalayer.mada.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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

    // --- Novos Campos Físicos (Portal do Cliente) ---
    @Column(nullable = false)
    private Integer quantidade; // Tamanho do lote para diluição de IC

    @Column(name = "dimensao_x", nullable = false)
    private Double dimensaoX;

    @Column(name = "dimensao_y", nullable = false)
    private Double dimensaoY;

    @Column(name = "dimensao_z", nullable = false)
    private Double dimensaoZ;

    @Column(length = 50)
    private String tolerancia;

    @Column(length = 100)
    private String acabamento;

    @Column(name = "nivel_inspecao", length = 100)
    private String nivelInspecao;

    @Column(name = "tratamento_termico", nullable = false)
    private Boolean tratamentoTermico = false;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_ic_id")
    private OrcamentoIC fase1IC;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "orcamento_dc_id")
    private OrcamentoDC fase2DC;

    @OneToMany(mappedBy = "orcamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrcamentoAC> fase3AC = new ArrayList<>();

    @Column(name = "custo_total_final", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalFinal;

    // Registo imutável do momento exato da emissão do documento para efeitos de auditoria.
    @CreationTimestamp
    @Column(name = "data_emissao", nullable = false, updatable = false)
    private LocalDateTime dataEmissao;
}