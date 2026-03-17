package com.inovalayer.mada.core.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

/**
 * DTO para solicitação inicial do Cliente (B2C).
 * Focado no envelope físico e requisitos de aplicação.
 */
public record OrcamentoRequestDTO(
        
        @NotNull(message = "O nome do projeto é obrigatório.")
        String nomeProjeto,

        @NotNull(message = "A quantidade é obrigatória.")
        @Positive(message = "A quantidade deve ser positiva.")
        Integer quantidade,

        @NotNull(message = "Dimensão X é obrigatória.")
        Double dimensaoX,

        @NotNull(message = "Dimensão Y é obrigatória.")
        Double dimensaoY,

        @NotNull(message = "Dimensão Z é obrigatória.")
        Double dimensaoZ,

        String tolerancia,
        String acabamento,
        String nivelInspecao,
        
        @NotNull(message = "Informe se necessita tratamento térmico.")
        Boolean tratamentoTermico,

        String finalidadePeca,
        UUID materialDesejadoId // Sugestão do cliente
) {}