package com.inovalayer.mada.core.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) para blindar a entidade ArameMetalico.
 * Garante que a interface Angular receba apenas os dados necessários para o formulário,
 * ocultando informações sensíveis de auditoria ou regras internas de banco.
 */
public record ArameMetalicoResponseDTO(
        UUID id,
        String nomeFabricante,
        String codigoProduto,
        BigDecimal precoUnitarioBase
) {
}