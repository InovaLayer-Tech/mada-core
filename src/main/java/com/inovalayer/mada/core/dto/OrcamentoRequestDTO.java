package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * Record DTO atuando como barreira de validação primária.
 * Recebe APENAS características FÍSICAS da peça e intenções de projeto.
 * Variáveis financeiras e taxas estão blindadas.
 */
public record OrcamentoRequestDTO(
        
        @NotNull(message = "{Validacao.Arame.NotNull}")
        UUID arameId,

        // --- FASE 1 (IC) ---
        @NotNull(message = "{Validacao.TempoPreparacao.NotNull}")
        @Positive(message = "{Validacao.TempoPreparacao.Positive}")
        Double tempoPreparacaoMinutos,

        @NotNull(message = "{Validacao.TempoRemocao.NotNull}")
        @Positive(message = "{Validacao.TempoRemocao.Positive}")
        Double tempoRemocaoMinutos,

        // --- FASE 2 (DC) ---
        @NotNull(message = "{Validacao.TempoArco.NotNull}")
        @Positive(message = "{Validacao.TempoArco.Positive}")
        Double tempoArcoMinutos,

        @NotNull(message = "{Validacao.MassaEstimada.NotNull}")
        @Positive(message = "{Validacao.MassaEstimada.Positive}")
        Double massaEstimadaKg,

        // --- FASE 3 (AC) --- Intenções
        @NotNull(message = "{Validacao.ProjetoCAD.NotNull}")
        Boolean requerProjetoCAD,

        @NotNull(message = "{Validacao.UsinagemFinal.NotNull}")
        Boolean requerUsinagemFinal,
        
        // Se requerUsinagemFinal = true, o Front deverá enviar este valor
        Double tempoUsinagemMinutos
) {}