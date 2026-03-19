package com.inovalayer.mada.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Entidade Singleton-like para armazenar as taxas voláteis da fábrica (energia, mão de obra).
 * O administrador atualizará esta linha em vez de fazer "hardcode" no sistema.
 */
@Getter
@Setter
@Entity
@Table(name = "parametro_global")
public class ParametroGlobal extends BaseEntity {

    // Utilizo BigDecimal para todos os valores financeiros e de precisão, conforme decisão arquitetural.
    // precision = 10 (total de dígitos), scale = 4 (casas decimais). 
    // Exemplo: 999999.9999. Essencial para custos de KWh que possuem muitas casas decimais.
    @Column(name = "custo_kwh", nullable = false, precision = 10, scale = 4)
    private BigDecimal custoKwh;

    @Column(name = "taxa_mao_de_obra_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaMaoDeObraHora;

    @Column(name = "taxa_engenheiro_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaEngenheiroHora;

    @Column(name = "taxa_usinagem_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaUsinagemHora;

    @Column(name = "taxa_depreciacao_maquina_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal taxaDepreciacaoMaquinaHora;

    @Column(name = "custo_tratamento_termico_fixo", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoTratamentoTermicoFixo;

    // Fatores K1 e K2 de risco/complexidade da metodologia WAAM
    @Column(name = "fator_risco_k1", nullable = false, precision = 5, scale = 2)
    private BigDecimal fatorRiscoK1;

    @Column(name = "fator_risco_k2", nullable = false, precision = 5, scale = 2)
    private BigDecimal fatorRiscoK2;

    @Column(name = "consumo_potencia_kw", nullable = false, precision = 6, scale = 2)
    private BigDecimal consumoPotenciaKw;

    @Column(name = "margem_lucro_percentual", nullable = false, precision = 5, scale = 2)
    private BigDecimal margemLucroPercentual;

    @Column(name = "taxa_impostos", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaImpostos = BigDecimal.ZERO;
}