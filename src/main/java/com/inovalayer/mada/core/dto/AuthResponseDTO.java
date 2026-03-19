package com.inovalayer.mada.core.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO de resposta para autenticação bem-sucedida.
 */
@Data
@Builder
public class AuthResponseDTO {
    private String token;
    private String email;
    private String nomeCompleto;
    private String role;
    private long expiresAt;
}
