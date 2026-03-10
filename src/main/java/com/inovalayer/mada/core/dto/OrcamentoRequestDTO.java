package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Record DTO para receber os dados de entrada da criação de um orçamento.
 * Ele atua como um "filtro" (Prancheta da Recepção), garantindo que apenas 
 * os dados estritamente necessários entrem no sistema.
 */
public record OrcamentoRequestDTO(
        
        @NotNull(message = "O ID do cliente é obrigatório.")
        UUID clienteId,

        @NotNull(message = "O ID do arame metálico é obrigatório.")
        UUID arameId,

        @NotNull(message = "O tempo de arco aberto é obrigatório.")
        @Positive(message = "O tempo de arco aberto deve ser maior que zero.")
        BigDecimal tempoArcoMinutos,

        @NotNull(message = "A massa estimada é obrigatória.")
        @Positive(message = "A massa estimada deve ser maior que zero.")
        BigDecimal massaEstimadaKg
) {
    // Um record não precisa de construtor, getters ou setters explícitos. 
    // O compilador Java gera tudo automaticamente.
}