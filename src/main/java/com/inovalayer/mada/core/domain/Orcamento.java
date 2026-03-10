package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * A Entidade Raiz (Root Aggregate). Une os dados do Python com as taxas do Banco.
 * Localização: src/main/java/com/inovalayer/mada/core/domain/
 */
@Getter
@Setter
@Entity
@Table(name = "tb_orcamento")
public class Orcamento extends BaseEntity {

    // --- Relacionamentos (Chaves Estrangeiras) ---
    // Muito para Um: Vários orçamentos podem pertencer ao mesmo Cliente.
    @ManyToOne(optional = false) // optional = false garante que não existe orçamento sem cliente (Integridade)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // Referências aos consumíveis escolhidos para ESTA peça.
    @ManyToOne(optional = false)
    @JoinColumn(name = "arame_metalico_id", nullable = false)
    private ArameMetalico arameMetalico;

    // --- Dados Crutos Extraídos do JSON do Paulo (Python) ---
    @Column(name = "tempo_arco_aberto_minutos", nullable = false, precision = 10, scale = 2)
    private BigDecimal tempoArcoAbertoMinutos;

    @Column(name = "massa_estimada_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal massaEstimadaKg;

    // --- CAMPOS DE SNAPSHOT (A Proteção contra Inflação) ---
    // Ao invés de buscar o preço atual do arame na hora de exibir a tela, 
    // nós copiamos o valor para cá no momento do cálculo. (Conceito Imutabilidade)
    @Column(name = "snap_preco_kg_arame_aplicado", nullable = false, precision = 10, scale = 2)
    private BigDecimal snapPrecoKgArameAplicado;

    @Column(name = "snap_custo_kwh_aplicado", nullable = false, precision = 10, scale = 4)
    private BigDecimal snapCustoKwhAplicado;

    // --- Resultado Final Matemático ---
    @Column(name = "custo_total_calculado", nullable = false, precision = 12, scale = 2)
    private BigDecimal custoTotalCalculado;

    @Column(name = "status_orcamento", nullable = false, length = 30) // Ex: PENDENTE, APROVADO, REJEITADO (No futuro, usaremos Enum)
    private String statusOrcamento = "PENDENTE";
}