package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO para a fase de Processamento Metrológico (B2B).
 * Recebe os dados calculados pelo engenheiro (ou fatiador) para orquestrar o cálculo financeiro.
 */
public record OrcamentoCalculoRequestDTO(
        
        @NotNull(message = "O ID do arame é obrigatório.")
        UUID arameId,

        @NotNull(message = "O tempo de arco é obrigatório.")
        @Positive(message = "O tempo de arco deve ser positivo.")
        Double tempoArcoMinutos,

        @NotNull(message = "A massa estimada é obrigatória.")
        @Positive(message = "A massa deve ser positiva.")
        Double massaEstimadaKg,

        @NotNull(message = "O tempo de preparação é obrigatório.")
        @Positive(message = "O tempo de preparação deve ser positivo.")
        Double tempoPreparacaoMinutos,

        @NotNull(message = "O tempo de remoção é obrigatório.")
        @Positive(message = "O tempo de remoção deve ser positivo.")
        Double tempoRemocaoMinutos,

        @NotNull(message = "O tempo morto é obrigatório.")
        @Positive(message = "O tempo morto deve ser positivo.")
        Double tempoMortoMinutos,

        Boolean requerProjetoCAD,
        Boolean requerUsinagemFinal,
        Double tempoUsinagemMinutos
) {}
