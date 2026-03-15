package com.inovalayer.mada.core.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO de Saída (Progressive Disclosure) para o Painel de Auditoria no Angular.
 * Mapeia as 3 Fases metrológicas (IC, DC, AC) preservando a SSOT financeira.
 */
public record OrcamentoResponseDTO(
        UUID id,
        String status,
        LocalDateTime dataEmissao,
        
        // FASE 1: IC
        Double tempoPreparacaoMinutos,
        Double tempoRemocaoMinutos,
        BigDecimal custoSubstratoIC,
        BigDecimal custoPreparacaoIC,
        BigDecimal custoRemocaoIC,
        BigDecimal custoTotalIC,

        // FASE 2: DC
        Double tempoArcoMinutos,
        Double massaEstimadaKg,
        String nomeArameMetalico,
        BigDecimal custoMaterialDC,
        BigDecimal custoGasDC,
        BigDecimal custoEnergiaDC,
        BigDecimal custoMaquinaDC,
        BigDecimal custoTotalDC,

        // FASE 3: AC
        List<ServicoAdicionalDTO> servicosAC,
        BigDecimal custoTotalAC,

        // CONSOLIDAÇÃO
        BigDecimal custoDiretoFabricacao,
        BigDecimal margemComercialAplicada,
        BigDecimal impostosFaturamentoEstimados,
        BigDecimal precoFinalSugerido
) {
    public record ServicoAdicionalDTO(
            String descricaoServico,
            Double quantidadeHoras,
            BigDecimal custoServico
    ) {}
}
