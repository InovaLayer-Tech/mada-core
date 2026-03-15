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
        
        @NotNull(message = "O identificador do insumo (Arame) é obrigatório.")
        UUID arameId,

        // --- FASE 1 (IC) ---
        @NotNull(message = "O tempo de preparação é obrigatório.")
        @Positive(message = "O tempo de preparação deve ser maior que zero.")
        Double tempoPreparacaoMinutos,

        @NotNull(message = "O tempo de remoção é obrigatório.")
        @Positive(message = "O tempo de remoção deve ser maior que zero.")
        Double tempoRemocaoMinutos,

        // --- FASE 2 (DC) ---
        @NotNull(message = "O tempo de arco aberto é obrigatório.")
        @Positive(message = "O tempo de arco deve ser maior que zero.")
        Double tempoArcoMinutos,

        @NotNull(message = "A massa estimada é obrigatória.")
        @Positive(message = "A massa estimada deve ser maior que zero.")
        Double massaEstimadaKg,

        // --- FASE 3 (AC) --- Intenções
        @NotNull(message = "A intenção de Projeto CAD é obrigatória.")
        Boolean requerProjetoCAD,

        @NotNull(message = "A intenção de Usinagem Final é obrigatória.")
        Boolean requerUsinagemFinal,
        
        // Se requerUsinagemFinal = true, o Front deverá enviar este valor
        Double tempoUsinagemMinutos
) {}