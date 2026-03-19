package com.inovalayer.mada.core.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO de resposta para Gás de Proteção.
 * Utiliza Record para imutabilidade e performance.
 */
public record GasProtecaoResponseDTO(
        UUID id,
        String nome,
        String fornecedor,
        String codigoProduto,
        String tipoGas,
        BigDecimal precoUnitarioBase,
        Boolean ativo,
        Double vazaoPadrao
) {
}
