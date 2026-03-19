package com.inovalayer.mada.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de resposta para Arame Metálico.
 * Utiliza Record para imutabilidade e performance (Java 14+).
 */
public record ArameMetalicoResponseDTO(
        UUID id,
        String nome,              // Adicionado para corrigir a quebra de interface
        String fornecedor,        // Sincronizado com Consumivel
        String codigoProduto,
        BigDecimal precoUnitarioBase,
        String ligaMetalica,
        Double diametroMm,
        Double densidadeGcm3,     // Padronizado com 'c' minúsculo
        Double eficiencia,        // Novo campo para cálculo de massa real
        Boolean ativo
) {
}